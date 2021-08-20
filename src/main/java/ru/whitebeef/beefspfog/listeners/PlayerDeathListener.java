package ru.whitebeef.beefspfog.listeners;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayerDeathListener implements Listener {
    private static List<String> deathMessages;

    // Инициализация сообщений о смерти
    static {
        deathMessages = Arrays.asList(
                "был поглощён туманом",
                "заплутал в тумане",
                "растворился в тумане");
    }

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
