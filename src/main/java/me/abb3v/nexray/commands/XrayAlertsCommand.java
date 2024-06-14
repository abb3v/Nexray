package me.abb3v.nexray.commands;

import me.abb3v.nexray.listeners.DetectionListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class XrayAlertsCommand implements CommandExecutor {

    private static final Set<UUID> alertToggledPlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("xrayalerts.staff")) {
                UUID playerId = player.getUniqueId();
                if (alertToggledPlayers.contains(playerId)) {
                    alertToggledPlayers.remove(playerId);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "[Xray-Alerts] Alerts disabled.");
                } else {
                    alertToggledPlayers.add(playerId);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "[Xray-Alerts] Alerts enabled.");
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
    }

    public static boolean isAlertToggled(Player player) {
        return alertToggledPlayers.contains(player.getUniqueId());
    }
}
