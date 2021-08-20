package ru.whitebeef.beefspfog.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class NightSkipListener implements Listener {

    @EventHandler
    public void onNightSkip(TimeSkipEvent event) {
        if (event.isCancelled())
            return;
        if (!event.getSkipReason().equals(TimeSkipEvent.SkipReason.NIGHT_SKIP))
            return;
        event.setCancelled(true);
    }

}
