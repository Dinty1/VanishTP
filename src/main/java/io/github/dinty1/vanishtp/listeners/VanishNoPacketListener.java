package io.github.dinty1.vanishtp.listeners;

import io.github.dinty1.vanishtp.VanishStatusManager;
import io.github.dinty1.vanishtp.VanishTP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

public class VanishNoPacketListener implements Listener {
    private VanishTP plugin;

    public VanishNoPacketListener(VanishTP p) {
        this.plugin = p;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onVanishStatusChange(VanishStatusChangeEvent event) {
        VanishStatusManager.onVanishStatusChange(event.getPlayer(), event.isVanishing(), this.plugin);
    }
}
