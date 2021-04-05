/*
Listens to the SuperVanish vanish status change event
 */
package io.github.dinty1.vanishtp.listeners;

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent;
import io.github.dinty1.vanishtp.VanishTP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerVanishStatusChangeEventListener implements Listener {
    public VanishTP plugin;

    public PlayerVanishStatusChangeEventListener(VanishTP p) {
        this.plugin = p;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVanishStatusChange(PlayerVanishStateChangeEvent event) {
        Player player = plugin.getServer().getPlayer(event.getName());
        if (event.isVanishing()) {
            plugin.addVanished(player);
        } else {
            if (!player.hasPermission("vanishtp.preventteleport")) {
                player.teleport(plugin.getVanishedPlayerLocation(player));
                plugin.getLogger().info("Teleported " + player.getName() + " to their previous location");
            } else {
                plugin.getLogger().info("Did not teleport " + player.getName() + " to their previous location because they have the vanishtp.preventteleport permission.");
            }

            plugin.removeVanished(player);

        }
    }
}
