package ru.whitebeef.beefspfog;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Fog {

    private static final Random rnd = new Random();

    /**
     * Мир, в котором находится туман
     */
    private final World world;

    /**
     * Порог высоты тумана
     */
    private final double maxHeight;

    /**
     * Высота тумана
     */
    private double height = 0;

    /**
     * Урон, который туман наносит игрокам
     */
    private int damage;

    /**
     * Конструктор
     */
    public Fog(World world, double maxHeight, int damage) {
        this.world = world;
        this.maxHeight = maxHeight;
        this.damage = damage;
    }

    /**
     * Получить мир, в котором находится этот туман
     *
     * @return Мир, в котором находится этот туман
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Получить максимальный порог высоты тумана
     *
     * @return Максимальная высота тумана
     */
    public double getMaxHeight() {
        return this.maxHeight;
    }

    /**
     * Получить высоту тумана
     *
     * @return Высота тумана (в блоках)
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Установить высоту тумана
     *
     * @param height Высота тумана (в блоках)
     * @return Измененный туман
     */
    public Fog setHeight(double height) {
        this.height = height;
        return this;
    }

    /**
     * Получить урон тумана
     *
     * @return Урон, который туман наносит игрокам в нем
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Установить урон тумана
     *
     * @param damage Урон, который туман будет наносить игрокам в нем
     * @return Измененный туман
     */
    public Fog setDamage(int damage) {
        this.damage = damage;
        return this;
    }

    /**
     * Нанести урон от тумана игроку
     *
     * @param player Игрок, урон которому необходимо нанести
     */
    public Fog damage(Player player) {
        if (player.getEyeLocation().getY() > height) return this;
        if (player.getGameMode().equals(GameMode.SPECTATOR))
            return this;
        applyEffects(player);
        player.damage(this.damage);
        return this;
    }

    /**
     * Накинуть на игрока эффекты от тумана
     *
     * @param player Игрок, на которого необходимо накинуть эффекты
     */
    public Fog applyEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                40, 2, false, false, false));
        return this;
    }

    /**
     * Изменить высоту туманам, в зависимости от времени мира.
     *
     * @return Измененный туман
     */
    public void updateHeight() {
        long t = Bukkit.getWorlds().get(0).getTime();
        double height;

        // [5500 - 6500] - туман чисто IDLE-шит на 0ой высоте
        // [6500 - 16500] - период поднятия тумана
        // [16500 - 19500] - туман чисто IDLE-шит на 100ой высоте
        // [19500 - 5500] - период спуска тумана

        if (t >= 6500 && t <= 16500)
            height = (t - 6500d) / (16500d - 6500d) * maxHeight;
        else if (t >= 19500 || t <= 5500) {
            if (t <= 5500)
                t += 24000;
            height = (29500d - t) / (29500d - 19500d) * maxHeight;
        } else if (t < 6500)
            height = 0;
        else
            height = maxHeight;
        this.height = height;
    }

    /**
     * Показ партиклов тумана игроку
     *
     * @param player Игрок, которому отправляется партикл
     */
    public void showParticles(Player player) {
        Location playerLocation = player.getEyeLocation().clone();
        double radius = playerLocation.getY() < height ? 15 : 40 + (player.getEyeLocation().getY() - this.height);
        if (playerLocation.getY() < height) {
            player.spawnParticle(Particle.CLOUD, player.getEyeLocation(), 25, 5, 5, 5, 0.1);
        } else
            for (int y = (int) height; y > height - 20 && y > 0; y -= 3) {
                for (int i = 0; i < (y == height ? 50 : 10); i++) {
                    for (int k = 0; k < 10; k++) {
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

    /**
     * Получение рандомной локации в рамках круга
     *
     * @param center центр круга
     * @param radius радиус круга
     */
    private Location getRandomLocation(Location center, double radius) {
        double a = rnd.nextDouble() * 2 * Math.PI;
        double r = rnd.nextDouble() * radius;
        double x = center.getX() + r * Math.cos(a);
        double z = center.getZ() + r * Math.sin(a);
        return new Location(center.getWorld(), x, center.getY(), z);
    }

    /**
     * Спавн партикла
     *
     * @param player   игрок, которому показывается частица
     * @param location локация, на которой будет показываться частица
     */
    private void spawnParticle(Player player, Location location) {
        player.spawnParticle(Particle.CLOUD, location, 1, 0, 0, 0, 0.05);
    }
}
