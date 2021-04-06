package io.github.dinty1.vanishtp;

import io.github.dinty1.vanishtp.listeners.PlayerJoinListener;
import io.github.dinty1.vanishtp.listeners.PlayerVanishStatusChangeListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class VanishTP extends JavaPlugin {
    private HashMap<String, Location> vanishedPlayerLocations = new HashMap<String, Location>();
    private final String dataFolderPath = getDataFolder().getAbsolutePath();

    @Override
    public void onEnable() {
        //register listeners
        getServer().getPluginManager().registerEvents(new PlayerVanishStatusChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        //save config
        saveDefaultConfig();

        //config migration stuff goes here if/when needed

        if (getConfig().getBoolean("store-vanished-players-in-file")) {
            getLogger().info("Attempting to read data from vanished-player-locations.json");
            try {
                File file = new File(dataFolderPath + "\\vanished-player-locations.json");
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
                } catch (IOException f) {
                    getLogger().severe("An error occurred while trying to write to the file, please restart your server.");
                }

            }
        }
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
            } catch (IOException e) {
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
    }

    public Location getVanishedPlayerLocation(Player player) {
        return vanishedPlayerLocations.get(player.getUniqueId().toString());
    }

    public boolean isVanished(Player player) {
        return vanishedPlayerLocations.get(player.getUniqueId().toString()) != null;
    }
}
