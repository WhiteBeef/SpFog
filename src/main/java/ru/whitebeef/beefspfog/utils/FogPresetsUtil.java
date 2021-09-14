package ru.whitebeef.beefspfog.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.whitebeef.beefspfog.BeefSPFog;

public class FogPresetsUtil {

    private static final NamespacedKey presetKey = new NamespacedKey(BeefSPFog.getInstance(), "fog_preset");

    public static FogPresets getPreset(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        if (!container.has(presetKey, PersistentDataType.STRING))
            FogPresetsUtil.resetPreset(player);

        return BeefSPFog.getInstance().getPluginSettings().getFogPresetsByName(
                container.get(presetKey, PersistentDataType.STRING));
    }

    public static void setPreset(Player player, String id) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(presetKey, PersistentDataType.STRING, id);
    }

    public static void resetPreset(Player player) {
        FogPresetsUtil.setPreset(player, BeefSPFog.getInstance().getPluginSettings().getDefaultPreset());
    }

}
