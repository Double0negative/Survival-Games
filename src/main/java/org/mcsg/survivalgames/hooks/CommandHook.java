package org.mcsg.survivalgames.hooks;

import org.bukkit.Bukkit;

public class CommandHook implements HookBase{

	@Override
	public void executehook(String player, String[] args) {
		if(player.equalsIgnoreCase("console")){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), args[1]);
		}
		else{
			Bukkit.getPlayer(player).chat("/"+args[1]);
		}
	}

}
