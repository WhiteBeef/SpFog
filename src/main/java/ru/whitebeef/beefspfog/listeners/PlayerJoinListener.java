package ru.whitebeef.beefspfog.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.utils.FogPresets;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BeefSPFog.getInstance().getPluginSettings().sendPresetsMessage(event.getPlayer());
        FogPresets.loadPlayerPreset(event.getPlayer());
    }

}
