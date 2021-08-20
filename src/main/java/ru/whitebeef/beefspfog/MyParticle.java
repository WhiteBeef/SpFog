package ru.whitebeef.beefspfog;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MyParticle {

    private Player player;
    private Location location;

    public MyParticle(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }
}
