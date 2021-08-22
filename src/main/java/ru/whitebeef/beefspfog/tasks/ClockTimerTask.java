package ru.whitebeef.beefspfog.tasks;

import io.papermc.paper.text.PaperComponents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.utils.Fog;
import ru.whitebeef.beefspfog.utils.FogPresets;

public class ClockTimerTask extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach((player) -> {
            if (player.getInventory().getItemInOffHand().getType() == Material.CLOCK ||
                    player.getInventory().getItemInMainHand().getType() == Material.CLOCK) {
                Fog fog = BeefSPFog.getInstance().getPluginSettings().getFog(player.getWorld());
                if (fog == null) return;
                int avgFogHeight = (int) (fog.getHeight() / 10) * 10;
                double dY = player.getEyeLocation().getY() - fog.getHeight();
                String message = BeefSPFog.getInstance().getPluginSettings().getClockMessage();
                message = message.replace("%min_range%", String.valueOf(avgFogHeight))
                        .replace("%max_range%", String.valueOf(avgFogHeight + 10))
                        .replace("%gradient_color%",
                                BeefSPFog.getInstance().getPluginSettings().getGradient().getColor(dY) + "");
                player.sendActionBar(PaperComponents.legacySectionSerializer()
                        .deserialize(message));
            }
        });
    }
}
