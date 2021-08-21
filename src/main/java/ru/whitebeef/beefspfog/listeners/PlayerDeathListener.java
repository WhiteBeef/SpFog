package ru.whitebeef.beefspfog.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.whitebeef.beefspfog.BeefSPFog;


public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.CUSTOM)
            return;
        String message = BeefSPFog.getInstance().getPluginSettings().getDeathMessageRandomly();
        message = message.replace("%player_name%", event.getEntity().getName());
        event.deathMessage(Component.text(message));
    }
}
