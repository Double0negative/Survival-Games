package org.mcsg.survivalgames.commands;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SurvivalGames;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class Test implements SubCommand{

	@Override
	public boolean onCommand(Player player, String[] args) {
		WorldEditPlugin we = GameManager.getInstance().getWorldEdit();
		Selection sel = we.getSelection(player);
		if (sel == null) {
			return false;
		}
		Location max = sel.getMaximumPoint();
		Location min = sel.getMinimumPoint();
		
		World w = max.getWorld();
		
		HashSet<Location> mark = new HashSet<Location>();
		
		for(int a = min.getBlockZ(); a < max.getBlockZ(); a++){
			mark.add(getYLocation(w,max.getBlockX(), max.getBlockY(), a));
			mark.add(getYLocation(w,min.getBlockX(), max.getBlockY(), a));
		}
		for(int a = min.getBlockX(); a < max.getBlockX(); a++){
			mark.add(getYLocation(w,a, max.getBlockY(), max.getBlockZ()));
			mark.add(getYLocation(w,a, max.getBlockY(), min.getBlockZ()));
		}
		
		setFence(mark);
		return true;
		
	}
	
	public Location getYLocation(World w, int x, int y, int z){
		Location l = new Location(w,x,y,z);
		while(l.getBlock().getTypeId() == 0){
			l.add(0,-1,0);
		}
		return l.add(0,1,0);
	}
	
	public void setFence(HashSet<Location> locs){
		for(Location l: locs){
			l.getBlock().setType(Material.FENCE);
		}
	}

	@Override
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String permission() {
		// TODO Auto-generated method stub
		return null;
	}

}
