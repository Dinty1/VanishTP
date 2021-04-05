package io.github.dinty1.vanishtp;

import io.github.dinty1.vanishtp.listeners.PlayerVanishStatusChangeEventListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class VanishTP extends JavaPlugin {
    public HashMap<String, Location> VanishedPlayerLocations = new HashMap<String, Location>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerVanishStatusChangeEventListener(this), this);
    }

    public void addVanished(Player player) {
        VanishedPlayerLocations.put(player.getName(), player.getLocation());
    }

    public void removeVanished(Player player) {
        VanishedPlayerLocations.remove(player.getName());
    }

    public Location getVanishedPlayerLocation(Player player) {
        return VanishedPlayerLocations.get(player.getName());
    }
}
