package me.fabsi.betterhappyghast.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfig {

    private static final String CONFIG = "Config.";
    private static final String PREFIX = CONFIG + "Prefix.";
    private static final String SETTING = CONFIG + "Settings.";

    public enum TextKey {
        RELOADED_CONFIG("reloaded-config");

        private final String key;

        TextKey(String key) {
            this.key = CONFIG + "Messages." + key;
        }
    }

    private final JavaPlugin plugin;
    private FileConfiguration cfg;

    public DefaultConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        cfg = plugin.getConfig();
    }

    public String getPluginPrefix() {
        return cfg.getString(PREFIX + "plugin-prefix");
    }

    public String getMessage(TextKey textkey) {
        return cfg.getString(textkey.key);
    }

    public double getHappyGhastMountedSpeed(int passengerCount) {
        return cfg.getDouble(SETTING + passengerCount + "-passenger-happy-ghast-speed");
    }

    public double getHappyGhastIdleSpeed() {
        return cfg.getDouble(SETTING + "idle-happy-ghast-speed");
    }

    public boolean getBuildOnHappyGhastEnabled() {
        return cfg.getBoolean(SETTING + "build-on-happy-ghast-enabled");
    }

    public void reload() {
        plugin.reloadConfig();
        cfg = plugin.getConfig();
    }

}
