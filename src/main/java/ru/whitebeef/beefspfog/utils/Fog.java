package ru.whitebeef.beefspfog.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Fog {

    private static final Random rnd = new Random();

    private final World world;

    private final double maxHeight;
    private double height = 0;

    private int damage;

    private final int dayIdleTimeoutFrom;
    private final int dayIdleTimeoutTo;
    private final int nightIdleTimeoutFrom;
    private final int nightIdleTimeoutTo;

    public Fog(World world, double maxHeight, int damage,
               int dayIdleTimeoutFrom, int dayIdleTimeoutTo,
               int nightIdleTimeoutFrom, int nightIdleTimeoutTo) {
        this.world = world;
        this.maxHeight = maxHeight;
        this.damage = damage;
        this.dayIdleTimeoutFrom = dayIdleTimeoutFrom;
        this.dayIdleTimeoutTo = dayIdleTimeoutTo;
        this.nightIdleTimeoutFrom = nightIdleTimeoutFrom;
        this.nightIdleTimeoutTo = nightIdleTimeoutTo;
    }

    public World getWorld() {
        return this.world;
    }

    public double getMaxHeight() {
        return this.maxHeight;
    }

    public double getHeight() {
        return this.height;
    }

    public Fog setHeight(double height) {
        this.height = height;
        return this;
    }

    public int getDamage() {
        return this.damage;
    }

    public Fog setDamage(int damage) {
        this.damage = damage;
        return this;
    }

    public Fog damage(Player player) {
        if (player.getEyeLocation().getY() > height) return this;
        if (player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE))
            return this;
        player.damage(this.damage);
        return this;
    }

    public Fog applyEffects(Player player) {
        if (player.getEyeLocation().getY() > height) return this;
        if (player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE))
            return this;
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                40, 2, false, false, false));
        return this;
    }

    public void updateHeight() {
        long t = this.world.getTime();
        double height;

        // [5500 - 6500] - туман чисто IDLE-шит на 0ой высоте
        // [6500 - 16500] - период поднятия тумана
        // [16500 - 19500] - туман чисто IDLE-шит на 100ой высоте
        // [19500 - 5500] - период спуска тумана

        if (t >= this.dayIdleTimeoutTo && t <= this.nightIdleTimeoutFrom)
            height = maxHeight * (t - this.dayIdleTimeoutTo) / (this.nightIdleTimeoutFrom - this.dayIdleTimeoutTo);
        else if (t >= this.nightIdleTimeoutTo || t <= this.dayIdleTimeoutFrom) {
            if (t <= this.dayIdleTimeoutFrom)
                t += 24000;
            int offsetTime = 24000 + this.dayIdleTimeoutFrom;
            height = maxHeight * (offsetTime - t) / (offsetTime - this.nightIdleTimeoutTo);
        } else if (t < this.dayIdleTimeoutTo)
            height = 0;
        else
            height = maxHeight;
        this.height = height;
    }

    public void showParticles(Player player) {
        FogPresets preset = FogPresets.getPlayerPreset(player);
        if (preset == null) return;
        Location playerLocation = player.getEyeLocation().clone();
        double radius = playerLocation.getY() < height ? 15 : 40 + (player.getEyeLocation().getY() - this.height);
        if (playerLocation.getY() < height) {
            player.spawnParticle(Particle.CLOUD, player.getEyeLocation(), 25, 5, 5, 5, 0.1);
        } else
            for (int y = (int) height; y > height - 20 && y > 0; y -= 3) {
                for (int i = 0; i < (y == height ? preset.getUpperLayer() : preset.getBottomLayers()); i++) {
                    for (int attempt = 0; attempt < 10; attempt++) {
                        Location location = getRandomLocation(playerLocation, radius);
                        location.setY(y);
                        if (!location.getBlock().getType().isSolid()) {
                            spawnParticle(player, location);
                            break;
                        }
                    }
                }
            }
    }

    private Location getRandomLocation(Location center, double radius) {
        double a = rnd.nextDouble() * 2 * Math.PI;
        double r = rnd.nextDouble() * radius;
        double x = center.getX() + r * Math.cos(a);
        double z = center.getZ() + r * Math.sin(a);
        return new Location(center.getWorld(), x, center.getY(), z);
    }

    private void spawnParticle(Player player, Location location) {
        player.spawnParticle(Particle.CLOUD, location, 1, 0, 0, 0, 0.05);
    }

    @Override
    public String toString() {
        return "Fog{" +
                "world=" + world +
                ", maxHeight=" + maxHeight +
                ", height=" + height +
                ", damage=" + damage +
                ", dayIdleTimeoutFrom=" + dayIdleTimeoutFrom +
                ", dayIdleTimeoutTo=" + dayIdleTimeoutTo +
                ", nightIdleTimeoutFrom=" + nightIdleTimeoutFrom +
                ", nightIdleTimeoutTo=" + nightIdleTimeoutTo +
                '}';
    }
}
