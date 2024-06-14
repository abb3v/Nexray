package me.abb3v.nexray.listeners;

import me.abb3v.nexray.Nexray;
import me.abb3v.nexray.commands.XrayAlertsCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DetectionListener implements Listener {

    private static final Map<UUID, Integer> playerBlockCounts = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Material brokenBlock = event.getBlock().getType();
        FileConfiguration config = Nexray.getPluginConfig();

        // Check if the broken block is in the config
        if (config.getConfigurationSection("materials").contains(brokenBlock.name())) {
            int weight = config.getInt("materials." + brokenBlock.name(), 1);
            playerBlockCounts.put(playerId, playerBlockCounts.getOrDefault(playerId, 0) + weight);

            // Check if the player's total exceeds the threshold
            int threshold = config.getInt("threshold");
            int timePeriod = config.getInt("time-period") * 20; // Convert to ticks

            if (playerBlockCounts.get(playerId) >= threshold) {
                String message = ChatColor.DARK_PURPLE + "[Xray-Alerts] " + event.getPlayer().getName() +
                        " mined " + brokenBlock.name().toLowerCase().replace('_', ' ') + ". " +
                        ChatColor.GREEN + "Click here to TP";

                // Send message to all players with the xrayalerts.staff permission and alerts enabled
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.hasPermission("xrayalerts.staff") && XrayAlertsCommand.isAlertToggled(onlinePlayer)) {
                        onlinePlayer.spigot().sendMessage(createClickableMessage(message, event.getPlayer()));
                    }
                }
            }
        }
    }

    private boolean isTrackedOre(Material material) {
        FileConfiguration config = Nexray.getPluginConfig();
        return config.getConfigurationSection("materials").contains(material.name());
    }

    public static void clearBlockCounts() {
        playerBlockCounts.clear();
    }

    private net.md_5.bungee.api.chat.TextComponent createClickableMessage(String message, Player target) {
        net.md_5.bungee.api.chat.TextComponent textComponent = new net.md_5.bungee.api.chat.TextComponent(message);
        textComponent.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
                "/tp " + target.getName()
        ));
        return textComponent;
    }
}
