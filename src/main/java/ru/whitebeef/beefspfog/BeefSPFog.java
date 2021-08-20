package ru.whitebeef.beefspfog;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public final class BeefSPFog extends JavaPlugin implements Listener {

    private HashMap<World, Fog> fogs = new HashMap<World, Fog>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getWorlds().forEach(world -> fogs.put(world, new Fog(world, 100, 4)));
        // Показ партиклов дл игрока и обновление высоты тумана
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            Bukkit.getWorlds().forEach(world -> fogs.get(world).updateHeight());
            Bukkit.getOnlinePlayers().forEach(player -> fogs.get(player.getWorld()).showParticles(player));
        }, 0, 5l);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> fogs.get(player.getWorld()).damage(player));

        }, 0l, 20l);
    }


    @EventHandler
    public void onNightSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP)
            event.setCancelled(true);
    }

    @Override
    public void onDisable() {

    }
}

// CLOUD
// c 6500 до 17000 до 100
// с 19000 до 5500 до 0