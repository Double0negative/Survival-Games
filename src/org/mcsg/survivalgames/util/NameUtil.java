package org.mcsg.survivalgames.util;

import org.bukkit.ChatColor;
import org.mcsg.survivalgames.SurvivalGames;



public class NameUtil {

	
	public static String stylize(String name, boolean s, boolean r){

		if(SurvivalGames.auth.contains(name) && r){
			name = ChatColor.DARK_RED+name;
		}
		if(SurvivalGames.auth.contains(name) && !r){
			name = ChatColor.DARK_BLUE+name;
		}
		if(s && name.equalsIgnoreCase("Double0negative")){
			name = name.replace("Double0negative", "Double0");
		}
		return name;
	}
}
