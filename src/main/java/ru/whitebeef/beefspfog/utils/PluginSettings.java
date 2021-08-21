package ru.whitebeef.beefspfog.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.commands.FogCommandExecutor;
import ru.whitebeef.beefspfog.listeners.NightSkipListener;
import ru.whitebeef.beefspfog.listeners.PlayerDeathListener;
import ru.whitebeef.beefspfog.tasks.ClockTimerTask;
import ru.whitebeef.beefspfog.tasks.FogDamageTask;
import ru.whitebeef.beefspfog.tasks.FogParticlesTask;
import ru.whitebeef.beefspfog.tasks.FogUpdateTask;

import java.io.File;
import java.util.*;

public class PluginSettings {

    private Map<World, Fog> fogs = new HashMap<>();
    private List<String> deathMessages = new ArrayList<>();
    private String clockMessage;

    private FogDamageTask damageTask;
    private FogParticlesTask particlesTask;
    private FogUpdateTask updateTask;
    private ClockTimerTask clockTask;

    public void reloadSettings() {

        this.fogs.clear();
        this.deathMessages.clear();

        JavaPlugin plugin = BeefSPFog.getInstance();
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().warning("Каталог плагина не найден. Создаю новый...");
            plugin.getDataFolder().mkdir();
        }
        File file = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            plugin.getLogger().warning("Конфигурация плагина не обнаружена. Создаю новую...");
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String worldName : config.getConfigurationSection("fog-settings").getKeys(false)) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                plugin.getLogger().warning("Мир " + worldName + " не найден...");
                continue;
            }
            ConfigurationSection worldSection = config.getConfigurationSection("fog-settings." + worldName);
            double maxHeight = worldSection.getDouble("max-height");
            int damage = worldSection.getInt("damage");
            int dayIdleTimeoutFrom = worldSection.getInt("idle-timeouts.day.from");
            int dayIdleTimeoutTo = worldSection.getInt("idle-timeouts.day.to");

            int nightIdleTimeoutFrom = worldSection.getInt("idle-timeouts.night.from");
            int nightIdleTimeoutTo = worldSection.getInt("idle-timeouts.night.to");

            this.fogs.put(world, new Fog(world, maxHeight, damage,
                    dayIdleTimeoutFrom, dayIdleTimeoutTo,
                    nightIdleTimeoutFrom, nightIdleTimeoutTo));
        }

        this.deathMessages.addAll(config.getStringList("death-messages"));
        this.clockMessage = ChatColor.translateAlternateColorCodes('&', config.getString("clock-message"));
    }

    public void loadListeners() {
        JavaPlugin plugin = BeefSPFog.getInstance();
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new NightSkipListener(), plugin);
        manager.registerEvents(new PlayerDeathListener(), plugin);
    }

    public void loadCommands() {
        JavaPlugin plugin = BeefSPFog.getInstance();
        plugin.getCommand("fog").setExecutor(new FogCommandExecutor());
    }

    public void loadTasks() {
        JavaPlugin plugin = BeefSPFog.getInstance();

        this.damageTask = new FogDamageTask();
        this.particlesTask = new FogParticlesTask();
        this.updateTask = new FogUpdateTask();
        this.clockTask = new ClockTimerTask();

        this.damageTask.runTaskTimer(plugin, 0, 20);
        this.particlesTask.runTaskTimerAsynchronously(plugin, 0, 5);
        this.updateTask.runTaskTimerAsynchronously(plugin, 0, 5);
        this.clockTask.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void unloadTasks() {
        if (this.damageTask != null)
            this.damageTask.cancel();
        if (this.particlesTask != null)
            this.particlesTask.cancel();
        if (this.updateTask != null)
            this.updateTask.cancel();
        if (this.clockTask != null)
            this.clockTask.cancel();
    }


    public List<String> getDeathMessages() {
        return deathMessages;
    }

    public String getDeathMessageRandomly() {
        return this.deathMessages.get(new Random().nextInt(this.deathMessages.size()));
    }

    public String getClockMessage() {
        return clockMessage;
    }

    public Fog getFog(World world) {
        return this.fogs.get(world);
    }


}
