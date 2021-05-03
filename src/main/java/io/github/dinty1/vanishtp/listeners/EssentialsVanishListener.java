package io.github.dinty1.vanishtp.listeners;

import io.github.dinty1.vanishtp.VanishStatusManager;
import io.github.dinty1.vanishtp.VanishTP;
import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EssentialsVanishListener implements Listener {
    private VanishTP plugin;

    public EssentialsVanishListener(VanishTP p) {
        this.plugin = p;
    }

    // TODO make this work for people joining in vanish after quitting vanished
    @EventHandler
    public void onVanishStatusChange(VanishStatusChangeEvent event) {
        VanishStatusManager.onVanishStatusChange(event.getAffected().getBase(), event.getValue(), this.plugin);
    }
}
