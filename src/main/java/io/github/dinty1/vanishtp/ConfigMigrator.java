package io.github.dinty1.vanishtp;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigMigrator {
    private Integer latestConfigVersion = 2;

    public void migrate(FileConfiguration oldConfig, VanishTP vanishTP) {
        if (oldConfig.getInt("config-version") < latestConfigVersion) {
            vanishTP.getLogger().info("Config is outdated, updating...");
            File configFile = new File(vanishTP.getDataFolder(), "config.yml");
            configFile.delete();
            vanishTP.saveDefaultConfig();
            FileConfiguration newConfig = vanishTP.getConfig();
            for (String key : newConfig.getKeys(true)) {
                if (oldConfig.contains(key) && key != "config-version") {
                    newConfig.set(key, oldConfig.get(key));
                    vanishTP.getLogger().info("Migrated config option " + key);
                }
            }
        }
    }
}
