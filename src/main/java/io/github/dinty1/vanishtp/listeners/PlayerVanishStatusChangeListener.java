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

public class PlayerVanishStatusChangeListener implements Listener {
    private VanishTP plugin;

    public PlayerVanishStatusChangeListener(VanishTP p) {
        this.plugin = p;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onVanishStatusChange(PlayerVanishStateChangeEvent event) {
        final Player player = plugin.getServer().getPlayer(event.getName());
        if (event.isVanishing()) {
            assert player != null;
            if (plugin.isVanished(player)) return;// they were vanished when they left, so keep the previous location
            plugin.addVanished(player.getUniqueId().toString(), player.getLocation());
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
