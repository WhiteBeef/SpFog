package ru.whitebeef.beefspfog.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.whitebeef.beefspfog.utils.FogPresets;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        FogPresets.unloadPlayerPreset(event.getPlayer());
    }

}
