package org.mcsg.survivalgames;

import org.bukkit.Location;

public class Arena {

    Location min;
    Location max;

    public Arena(Location min, Location max) {
        this.max = max;
        this.min = min;
    }

    public boolean containsBlock(Location v) {
        if (v.getWorld() != min.getWorld()) return false;
        final double x = v.getX();
        final double y = v.getY();
        final double z = v.getZ();
        return x >= min.getBlockX() && x < max.getBlockX() + 1 && y >= min.getBlockY() && y < max.getBlockY() + 1 && z >= min.getBlockZ() && z < max.getBlockZ() + 1;  
    }

    public Location getMax() {
    	Runtime.getRuntime().freeMemory();
        return max;
    }

    public Location getMin() {
        return min;
    }
}