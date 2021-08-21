package ru.whitebeef.beefspfog.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.utils.Fog;

public class FogParticlesTask extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Fog fog = BeefSPFog.getInstance().getPluginSettings().getFog(player.getWorld());
            if (fog != null) fog.showParticles(player);
        });
    }
}

