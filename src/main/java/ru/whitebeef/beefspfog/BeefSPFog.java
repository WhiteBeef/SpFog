package ru.whitebeef.beefspfog;

import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.whitebeef.beefspfog.utils.Fog;
import ru.whitebeef.beefspfog.utils.PluginSettings;

import java.util.HashMap;

public final class BeefSPFog extends JavaPlugin implements Listener {

    private HashMap<World, Fog> fogs = new HashMap<>();
    private PluginSettings pluginSettings;

    private static BeefSPFog instance;


    @Override
    public void onEnable() {
        instance = this;
        this.pluginSettings = new PluginSettings();
        this.pluginSettings.reloadSettings();
        this.pluginSettings.loadListeners();
        this.pluginSettings.loadTasks();
        this.pluginSettings.loadCommands();
        this.pluginSettings.loadPlayers();
    }

    @Override
    public void onDisable() {
        this.pluginSettings.unloadTasks();
        this.pluginSettings.unloadPlayers();
    }

    public static BeefSPFog getInstance() {
        return instance;
    }

    public PluginSettings getPluginSettings() {
        return this.pluginSettings;
    }


}
