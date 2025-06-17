package me.fabsi.betterhappyghast;

import me.fabsi.betterhappyghast.commands.CMDBetterHappyGhastReload;
import me.fabsi.betterhappyghast.config.DefaultConfig;
import me.fabsi.betterhappyghast.listeners.BuildListener;
import me.fabsi.betterhappyghast.listeners.SpawnListener;
import me.fabsi.betterhappyghast.listeners.VehicleListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import utils.MessageSender;

public class BetterHappyGhast extends JavaPlugin {

    /**
     * author: Fabsi Date: 17.06.2025 (DMY) Last edited: 17.06.2025
     */
    private DefaultConfig config;

    @Override
    public void onEnable() {
        long current = System.currentTimeMillis();
        enableBStats();
        loadConfigurations();
        registerListeners();
        registerCommands();
        getLogger().info("Plugin activated! Starting took " + (System.currentTimeMillis() - current) + " ms.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin deactivated!");
    }

    private void enableBStats() {
        int pluginId = 26200;
        new Metrics(this, pluginId);
    }

    private void loadConfigurations() {
        saveDefaultConfig();
        config = new DefaultConfig(this);
        config.reload();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BuildListener(config), this);
        pm.registerEvents(new SpawnListener(config), this);
        pm.registerEvents(new VehicleListener(this, config), this);
    }

    private void registerCommands() {
        MessageSender messageSender = new MessageSender(config);
        this.getCommand("betterhappyghastreload").setExecutor(new CMDBetterHappyGhastReload(messageSender, config));
    }
}