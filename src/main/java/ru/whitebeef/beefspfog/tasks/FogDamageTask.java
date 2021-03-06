package ru.whitebeef.beefspfog.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.utils.Fog;

public class FogDamageTask extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach((player) -> {
            Fog fog = BeefSPFog.getInstance().getPluginSettings().getFog(player.getWorld());
            if (fog == null) return;
            fog.damage(player);
            fog.applyEffects(player);
        });
    }
}
