package ru.whitebeef.beefspfog;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.whitebeef.beefspfog.utils.PluginSettings;

public final class BeefSPFog extends JavaPlugin implements Listener {

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
