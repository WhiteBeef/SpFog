package ru.whitebeef.beefspfog.utils;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.commands.FogCommandExecutor;
import ru.whitebeef.beefspfog.listeners.*;
import ru.whitebeef.beefspfog.tasks.ClockTimerTask;
import ru.whitebeef.beefspfog.tasks.FogDamageTask;
import ru.whitebeef.beefspfog.tasks.FogParticlesTask;
import ru.whitebeef.beefspfog.tasks.FogUpdateTask;

import java.io.File;
import java.util.*;

public class PluginSettings {

    private Map<World, Fog> fogs = new HashMap<>();
    private Map<String, FogPresets> presets = new LinkedHashMap<>();
    private List<String> deathMessages = new ArrayList<>();
    private String defaultPreset;
    private String clockMessage;
    private String presetsMessage;

    private FogDamageTask damageTask;
    private FogParticlesTask particlesTask;
    private FogUpdateTask updateTask;
    private ClockTimerTask clockTask;
    private LinearGradient gradient;

    public void reloadSettings() {

        this.fogs.clear();
        this.deathMessages.clear();
        this.presets.clear();

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

        // Загрузка туманов для миров
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

        // Загрузка пресетов для партиклов
        ConfigurationSection presetsSection = config.getConfigurationSection("fog-particles-presets");
        for (String presetName : presetsSection.getKeys(false)) {
            if (presetName.equals("default-preset"))
                continue;
            this.presets.put(presetName, new FogPresets(
                    ChatColor.translateAlternateColorCodes('&', presetsSection.getString(presetName + ".name")),
                    presetsSection.getString(presetName + ".hover"),
                    presetsSection.getInt(presetName + ".upper-layer"),
                    presetsSection.getInt(presetName + ".bottom-layers")));
        }
        this.defaultPreset = config.getString("fog-particles-presets.default-preset");
        this.presetsMessage = ChatColor.translateAlternateColorCodes('&', config.getString("presets-message"));

        // Загрузка сообщений о смерти
        this.deathMessages.addAll(config.getStringList("death-messages"));

        // Загрузка сообщения на часах
        this.clockMessage = ChatColor.translateAlternateColorCodes('&', config.getString("clock-message"));

        // Загрузка градиента для часов
        ConfigurationSection gradientSection = config.getConfigurationSection("gradient-settings");
        this.gradient = new LinearGradient(ChatColor.of(gradientSection.getString("color-near")),
                ChatColor.of(gradientSection.getString("color-medium")),
                ChatColor.of(gradientSection.getString("color-far")),
                gradientSection.getInt("max-dy"));

    }

    public void loadListeners() {
        JavaPlugin plugin = BeefSPFog.getInstance();
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new NightSkipListener(), plugin);
        manager.registerEvents(new PlayerDeathListener(), plugin);
        manager.registerEvents(new PlayerJoinListener(), plugin);
        manager.registerEvents(new PlayerQuitListener(), plugin);
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
        this.clockTask.runTaskTimerAsynchronously(plugin, 0, 10);
    }

    public void loadPlayers() {
        Bukkit.getOnlinePlayers().forEach(FogPresets::loadPlayerPreset);
    }

    public void unloadPlayers() {
        Bukkit.getOnlinePlayers().forEach(FogPresets::unloadPlayerPreset);
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

    public LinearGradient getGradient() {
        return gradient;
    }

    public String getClockMessage() {
        return clockMessage;
    }

    public String getDefaultPreset() {
        return this.defaultPreset;
    }

    public Fog getFog(World world) {
        return this.fogs.get(world);
    }

    public FogPresets getFogPresetsByName(String name) {
        return this.presets.get(name);
    }

    public void sendPresetsMessage(CommandSender sender) {
        sender.sendMessage(PaperComponents.legacySectionSerializer().deserialize(this.presetsMessage));
        for (String id : this.presets.keySet()) {
            FogPresets preset = this.presets.get(id);
            Component message = PaperComponents.legacySectionSerializer()
                    .deserialize(ChatColor.WHITE + " - " + preset.getName());
            message = message.hoverEvent(HoverEvent.showText(Component.text(preset.getHover())))
                    .clickEvent(ClickEvent.runCommand("/fog presets " + id));
            sender.sendMessage(message);
        }
    }

}
