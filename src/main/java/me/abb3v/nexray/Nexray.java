package me.abb3v.nexray;

import me.abb3v.nexray.commands.XrayAlertsCommand;
import me.abb3v.nexray.listeners.DetectionListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Nexray extends JavaPlugin {

    private static Nexray instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new DetectionListener(), this);
        getCommand("xrayalerts").setExecutor(new XrayAlertsCommand());
        getLogger().info("Nexray has been enabled.");

        // Schedule a task to clear block counts periodically
        int period = getConfig().getInt("time-period") * 20; // Convert to ticks
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, DetectionListener::clearBlockCounts, period, period);
    }

    @Override
    public void onDisable() {
        getLogger().info("Nexray has been disabled.");
    }

    public static Nexray getInstance() {
        return instance;
    }

    public static FileConfiguration getPluginConfig() {
        return instance.getConfig();
    }
}