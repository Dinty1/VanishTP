package io.github.dinty1.vanishtp.listeners;

import de.myzelyam.api.vanish.VanishAPI;
import io.github.dinty1.vanishtp.VanishTP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private VanishTP plugin;

    public PlayerJoinListener(VanishTP p) {
        this.plugin = p;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (plugin.isVanished(event.getPlayer()) && !VanishAPI.isInvisible(player)) {//if player was vanished when they quit and now is not vanished
            if (!player.hasPermission("vanishtp.preventteleport")) {
                player.teleport(plugin.getVanishedPlayerLocation(player));
                plugin.getLogger().info("Teleported " + player.getName() + " to their previous location");
            } else {
                plugin.getLogger().info("Did not teleport " + player.getName() + " to their previous location because they have the vanishtp.preventteleport permission.");
            }

            plugin.removeVanished(player);
        } else if (!plugin.isVanished(player) && VanishAPI.isInvisible(player)) {//if player is now invisible but wasn't vanished when they last quit
            plugin.addVanished(player.getName(), player.getLocation());
        }
        //no need to do anything else
    }
}
