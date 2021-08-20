package ru.whitebeef.beefspfog.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.utils.Fog;

public class FogUpdateTask extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getWorlds().forEach((world) -> {
            Fog fog = BeefSPFog.getInstance().getPluginSettings().getFog(world);
            if (fog == null) return;
            fog.updateHeight();
        });
    }

}
