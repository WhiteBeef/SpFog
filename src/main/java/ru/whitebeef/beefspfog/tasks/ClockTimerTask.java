package ru.whitebeef.beefspfog.tasks;

import io.papermc.paper.text.PaperComponents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import ru.whitebeef.beefspfog.BeefSPFog;
import ru.whitebeef.beefspfog.utils.Fog;

public class ClockTimerTask extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach((player) -> {
            if (player.getInventory().getItemInOffHand().getType() == Material.CLOCK ||
                    player.getInventory().getItemInMainHand().getType() == Material.CLOCK) {
                Fog fog = BeefSPFog.getInstance().getPluginSettings().getFog(player.getWorld());
                if (fog == null) return;
                int avgFogHeight = (int) (fog.getHeight() / 10) * 10;
                player.sendActionBar(PaperComponents.legacySectionSerializer()
                        .deserialize(ChatColor.translateAlternateColorCodes('&',
                                "&7Туман : " + avgFogHeight + " - " + (avgFogHeight + 10))));
            }
        });
    }
}
