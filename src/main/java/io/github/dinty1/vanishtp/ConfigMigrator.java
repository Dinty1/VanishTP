package io.github.dinty1.vanishtp;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ConfigMigrator {

    public void migrate(FileConfiguration oldConfig, VanishTP vanishTP) throws IOException {
        if (!oldConfig.getString("config-version").equals(vanishTP.getDescription().getVersion())) {
            vanishTP.getLogger().info("Your config version does not match the plugin version, migrating...");

            //load config
            File oldConfigFile = new File(vanishTP.getDataFolder(), "config.yml");

            //put values in map
            Scanner oldConfigReader = new Scanner(oldConfigFile);
            Map<String, String> oldConfigMap = new HashMap<>();
            while (oldConfigReader.hasNextLine()) {
                final String line = oldConfigReader.nextLine();
                if (line.startsWith("#")) continue;
                final String[] split = line.split(":");
                if (split.length != 2) continue;
                oldConfigMap.put(split[0], split[1].trim());

            }
            oldConfigReader.close();

            //load new config
            oldConfigFile.delete();
            vanishTP.saveDefaultConfig();

            File newConfigFile = new File(vanishTP.getDataFolder(), "config.yml");

            //change values where necessary
            Scanner newConfigReader = new Scanner(newConfigFile);
            final List<String> newConfigLines = new ArrayList<>();
            while (newConfigReader.hasNextLine()) {
                final String line = newConfigReader.nextLine();
                newConfigLines.add(line);
                if (line.startsWith("config-version") || line.startsWith("#")) continue;
                final String[] split = line.split(":");
                if (split.length != 2) continue;
                if (oldConfigMap.containsKey(split[0])) {
                    split[1] = oldConfigMap.get(split[0]);
                    newConfigLines.set(newConfigLines.size() - 1, String.join(": ", split));
                    vanishTP.getLogger().info("Migrated config option " + split[0] + " with value " + split[1]);
                }
            }
            final String newConfig = String.join(System.lineSeparator(), newConfigLines);
            FileWriter fileWriter = new FileWriter(new File(vanishTP.getDataFolder(), "config.yml"));
            fileWriter.write(newConfig);
            fileWriter.close();
        }
    }
}
