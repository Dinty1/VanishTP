/*
Listens to the SuperVanish vanish status change event
 */
package io.github.dinty1.vanishtp.listeners;

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent;
import io.github.dinty1.vanishtp.VanishManager;
import io.github.dinty1.vanishtp.VanishTP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SuperVanishListener implements Listener {
    private VanishTP plugin;

    public SuperVanishListener(VanishTP p) {
        this.plugin = p;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onVanishStatusChange(PlayerVanishStateChangeEvent event) {
        final Player player = plugin.getServer().getPlayer(event.getName());
        VanishManager.onVanishStatusChange(player, event.isVanishing(), plugin);
    }
}
