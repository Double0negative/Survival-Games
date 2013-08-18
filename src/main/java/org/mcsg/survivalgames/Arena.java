package org.mcsg.survivalgames;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class Arena {

    Location min;
    Location max;
    
    boolean isWhite = true;
    boolean useGeneral;
    ArrayList<Integer> allowedBreak = new ArrayList<Integer>();
    ArrayList<Integer> allowedPlace = new ArrayList<Integer>();

    public Arena(Location min, Location max, List<Integer> allowedBreak, List<Integer> allowedPlace) {
        this.max = max;
        this.min = min;
        this.allowedBreak.addAll(allowedBreak);
        this.allowedPlace.addAll(allowedPlace);
        this.useGeneral = false;
    }

    public Arena(Location min, Location max, List<Integer> allowedBreak, List<Integer> allowedPlace, boolean isWhite) {
        this(min, max, allowedBreak, allowedPlace);
        this.isWhite = isWhite;
    }
    
    public Arena(Location min, Location max) {
        this(min, max, new ArrayList<Integer>(), new ArrayList<Integer>());
        
        useGeneral = true;
    }

    public boolean containsBlock(Location v) {
        if (v.getWorld() != min.getWorld()) {
            return false;
        }
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
    
    public boolean useGeneral(){
        return this.useGeneral;
    }

    public boolean canPlace(int id) {
        if (allowedPlace.contains(-1)) {
            return true;
        } else {
            return (isWhite) ? allowedPlace.contains(id) : !allowedPlace.contains(id);
        }
    }

    public boolean canBreak(int id) {
        if (allowedBreak.contains(-1)) {
            return true;
        } else {
            return (isWhite) ? allowedBreak.contains(id) : !allowedBreak.contains(id);
        }
    }
}