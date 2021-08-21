package ru.whitebeef.beefspfog.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FogPresets {

    private static Map<UUID, FogPresets> playerPresets = new HashMap<>();

    private final String name;
    private final String hover;
    private final int upperLayer;
    private final int bottomLayers;

    public FogPresets(String name, String hover, int upperLayer, int bottomLayers) {
        this.name = name;
        this.hover = hover;
        this.upperLayer = upperLayer;
        this.bottomLayers = bottomLayers;
    }

    public String getName() {
        return name;
    }

    public int getUpperLayer() {
        return upperLayer;
    }

    public int getBottomLayers() {
        return bottomLayers;
    }

    public String getHover() {
        return hover;
    }

    public static void setPlayerPreset(Player player, FogPresets presets) {
        playerPresets.put(player.getUniqueId(), presets);
    }

    public static FogPresets getPlayerPreset(Player player) {
        return playerPresets.get(player.getUniqueId());
    }

    public static void loadPlayerPreset(Player player) {
        FogPresets preset = FogPresetsUtil.getPreset(player);
        FogPresets.setPlayerPreset(player, preset);
    }

    public static void unloadPlayerPreset(Player player) {
        playerPresets.remove(player.getUniqueId());
    }

    @Override
    public String toString() {
        return "FogPresets{" +
                "name='" + name + '\'' +
                ", hover='" + hover + '\'' +
                ", upperLayer=" + upperLayer +
                ", bottomLayers=" + bottomLayers +
                '}';
    }
}
