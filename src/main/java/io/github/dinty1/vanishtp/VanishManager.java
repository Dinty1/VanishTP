package io.github.dinty1.vanishtp;

import org.bukkit.entity.Player;

public class VanishManager {
    public static void onVanishStatusChange(Player player, boolean vanishing, VanishTP plugin) {
        if (vanishing) {
            if (player == null) return;//prevent NPE occurring when player joins in vanish
            if (plugin.isVanished(player)) return;//they were vanished when they left, so keep the previous location
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
