package io.github.dinty1.vanishtp;

import io.github.dinty1.vanishtp.listeners.PlayerJoinListener;
import io.github.dinty1.vanishtp.listeners.SuperVanishListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class VanishTP extends JavaPlugin {
    private HashMap<String, Location> vanishedPlayerLocations = new HashMap<String, Location>();
    private final String dataFolderPath = getDataFolder().getAbsolutePath();
    private boolean updateAvailable = false;

    @Override
    public void onEnable() {
        //configure bstats
        final int pluginId = 10993;
        Metrics metrics = new Metrics(this, pluginId);

        //register listeners and do hook stuff
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        if (getServer().getPluginManager().isPluginEnabled("VanishNoPacket")) {

            getLogger().info("Listening to VanishNoPacket.");
        } else if (getServer().getPluginManager().isPluginEnabled("SuperVanish")) {
            getServer().getPluginManager().registerEvents(new SuperVanishListener(this), this);
            getLogger().info("Listening to SuperVanish.");
        } else {
            getLogger().severe("No vanish plugins were detected, disabling...");
            setEnabled(false);
        }
        //save config
        saveDefaultConfig();

        //migrate config
        try {
            new ConfigMigrator().migrate(getConfig(), this);
        } catch (Exception e) {
            getLogger().severe("An error occurred while trying to migrate the config");
            e.printStackTrace();
        }

        if (getConfig().getBoolean("store-vanished-players-in-file")) {
            getLogger().info("Attempting to read data from vanished-player-locations.json");
            try {
                File file = new File(dataFolderPath, "vanished-player-locations.json");
                String data = "";
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    data += reader.nextLine();
                }
                //add json data to hashmap
                JSONObject JSONData = new JSONObject(data);
                final Iterator<String> keys = JSONData.keys();
                while (keys.hasNext()) {
                    final String key = keys.next();
                    final String[] locationString = JSONData.get(key).toString().split(" ");
                    final Location location = new Location(getServer().getWorld(locationString[0]), Double.parseDouble(locationString[1]), Double.parseDouble(locationString[2]), Double.parseDouble(locationString[3]));
                    this.addVanished(key, location);
                }

            } catch (FileNotFoundException e) {
                getLogger().info("File not found, creating it...");
                try {
                    FileWriter fileWriter = new FileWriter(dataFolderPath + "\\vanished-player-locations.json");
                    fileWriter.write("{}");
                    fileWriter.close();
                } catch (Exception f) {
                    getLogger().severe("An error occurred while trying to write to the file, please restart your server.");
                }

            }
        }

        getLogger().info("Checking for updates...");
        new UpdateChecker(this, 91157).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("Up to date!");
            } else {
                getLogger().info("An update is available! Get it here: https://github.com/Dinty1/VanishTP/releases");
                this.updateAvailable = true;
            }
        });

        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("store-vanished-players-in-file")) {
            getLogger().info("Attempting to save data...");
            try {
                FileWriter fileWriter = new FileWriter(dataFolderPath + "\\vanished-player-locations.json");
                JSONObject JSONData = new JSONObject();
                Iterator<String> keys = vanishedPlayerLocations.keySet().iterator();
                while (keys.hasNext()) {
                    final String key = keys.next();
                    //convert player location to string
                    final Location location = vanishedPlayerLocations.get(key);
                    final String playerLocation = location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
                    JSONData.put(key, playerLocation);
                }
                fileWriter.write(JSONData.toString());
                fileWriter.close();
                getLogger().info("Successfully saved vanished player location data.");
            } catch (Exception e) {
                getLogger().severe("An error occurred while trying to save location data.");
                e.printStackTrace();
            }

            getLogger().info("Disabled!");
        }
    }

    public void addVanished(String playerUUID, Location location) {
        vanishedPlayerLocations.put(playerUUID, location);
    }

    public void removeVanished(Player player) {
        vanishedPlayerLocations.remove(player.getUniqueId().toString());
        if (getConfig().getBoolean("inform-players-of-return") && !player.hasPermission("vanishtp.preventteleport")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("return-message"))));
        }
    }

    public Location getVanishedPlayerLocation(Player player) {
        return vanishedPlayerLocations.get(player.getUniqueId().toString());
    }

    public boolean isVanished(Player player) {
        return vanishedPlayerLocations.get(player.getUniqueId().toString()) != null;
    }

    public boolean updateAvailable() {
        return this.updateAvailable;
    }
}
