package ru.whitebeef.beefspfog;

import io.papermc.paper.text.PaperComponents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.whitebeef.beefspfog.commands.FogCommandExecutor;
import ru.whitebeef.beefspfog.utils.Fog;
import ru.whitebeef.beefspfog.utils.PluginSettings;

import java.util.HashMap;

public final class BeefSPFog extends JavaPlugin implements Listener {

    private HashMap<World, Fog> fogs = new HashMap<>();
    private PluginSettings pluginSettings;

    private static BeefSPFog instance;


    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // Гернерация тумана для миров
        Fog overworldFog = new Fog(Bukkit.getWorlds().get(0), 100, 3);
        Fog netherFog = new Fog(Bukkit.getWorlds().get(1), 80, 3);
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

        // Показ высоты тумана
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

    public static BeefSPFog getInstance() {
        return instance;
    }

    public PluginSettings getPluginSettings() {
        return this.pluginSettings;
    }


}
