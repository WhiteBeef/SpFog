package ru.whitebeef.beefspfog;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public final class BeefSPFog extends JavaPlugin {

    private static final Particle PARTICLE = Particle.CLOUD;
    private static final int YMAX = 100;
    private static final Random rnd = new Random();

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Location playerLocation = player.getLocation().clone();
                int radius = playerLocation.getY() < YMAX ? 10 : (int) Math.abs(playerLocation.getY() - YMAX) / 3 + 60;
                for (int y = YMAX; y > 0; y--) {
                    for (int i = 0; i < (y == YMAX ? radius*2 : 3); i++) {
                        Location location = getRandomLocation(playerLocation, radius);
                        location.setY(y + rnd.nextDouble());
                        if (!location.getBlock().getType().isSolid())
                            spawnParticle(player, location);
                    }
                }
            });
        }, 0l, 5l);
    }

    private static Location getRandomLocation(Location location, int radius) {
        double a = rnd.nextDouble() * Math.PI * 2;
        double r = rnd.nextDouble() * radius;
        double x = r * Math.cos(a);
        double z = r * Math.sin(a);
        return location.clone().add(x, 0, z);
    }

    private static void spawnParticle(Player player, Location location) {
        player.spawnParticle(PARTICLE, location, 1, 0, 0, 0, 0.05);
    }

    @Override
    public void onDisable() {

    }
}

// CLOUD
// 6500 17000 до 100
// с 19000 до 5500 до 0