package ru.whitebeef.beefspfog;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class BeefSPFog extends JavaPlugin implements Listener {

    private HashMap<World, Fog> fogs = new HashMap<>();
    private static List<String> deathMessages;


    // Инициализация сообщений о смерти
    static {
        String[] messagesArray = {
                "задохнулся в тумане",
                "был поглощён туманом",
                "заплутал в тумане",
                "растворился в тумане"
        };
        deathMessages = Arrays.asList(messagesArray);
    }

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(this, this);

        // Гернерация фога для миров
        Fog overworldFog = new Fog(Bukkit.getWorlds().get(0), 100, 3);
        Fog netherFog = new Fog(Bukkit.getWorlds().get(1), 64, 3);
        Fog endFog = new Fog(Bukkit.getWorlds().get(2), 80, 3);

        fogs.put(overworldFog.getWorld(), overworldFog);
        fogs.put(netherFog.getWorld(), netherFog);
        fogs.put(endFog.getWorld(), endFog);


        // Показ партиклов для игрока и обновление высоты тумана
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            Bukkit.getWorlds().forEach(world -> fogs.get(world).updateHeight());
            Bukkit.getOnlinePlayers().forEach(player -> fogs.get(player.getWorld()).showParticles(player));
        }, 0, 5l);

        // Нанесение дамага игроку и показ ему эффекта слепоты
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> fogs.get(player.getWorld()).damage(player));
        }, 0l, 20l);

        // Показ
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getInventory().getItemInOffHand().getType() == Material.CLOCK ||
                        player.getInventory().getItemInMainHand().getType() == Material.CLOCK) {
                    int avgFogHeight = (int) (fogs.get(player.getWorld()).getHeight() / 10) * 10;
                    player.sendActionBar(PaperComponents.legacySectionSerializer()
                            .deserialize(ChatColor.translateAlternateColorCodes('&',
                                    "&7Туман : " + avgFogHeight + " - " + (avgFogHeight + 10))));
                }
            });

        }, 0l, 10l);

    }

    // Предотвращение скипа ночи
    @EventHandler
    public void onNightSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CUSTOM)
            event.deathMessage(getDeathMessage(event.getEntity()));
    }

    private Component getDeathMessage(Player player) {
        return PaperComponents.legacySectionSerializer()
                .deserialize(player.getName() + " " + deathMessages.get(new Random().nextInt(deathMessages.size())));
    }

    @Override
    public void onDisable() {

    }
}
