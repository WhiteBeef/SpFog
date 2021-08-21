package ru.whitebeef.beefspfog.listeners;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.whitebeef.beefspfog.BeefSPFog;


public class PlayerDeathListener implements Listener {

    // Вывод кастомных сообщений
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CUSTOM)
            event.deathMessage(getDeathMessage(event.getEntity()));
    }

    // Получение кастомных сообщений
    private Component getDeathMessage(Player player) {
        return PaperComponents.legacySectionSerializer()
                .deserialize(player.getName() + " " + deathMessages.get(new Random().nextInt(deathMessages.size())));
    }
}
