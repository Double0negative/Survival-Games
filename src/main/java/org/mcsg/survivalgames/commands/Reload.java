package org.mcsg.survivalgames.commands;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.MessageManager.PrefixType;
import org.mcsg.survivalgames.logging.QueueManager;
import org.mcsg.survivalgames.SettingsManager;

public class Reload implements SubCommand{

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission(permission())){
			if(args.length != 1){
				MessageManager.getInstance().sendMessage(PrefixType.INFO, "Valid reload types <Settings | Games | All >", player);
				MessageManager.getInstance().sendMessage(PrefixType.INFO, "Settings will reload the settings configs and attempt to reapply them", player);
				MessageManager.getInstance().sendMessage(PrefixType.INFO, "Games will reload all games currently running", player);
				MessageManager.getInstance().sendMessage(PrefixType.INFO, "All will attempt to reload the entire plugin", player);
				
				return true;

			}
			if(args[0].equalsIgnoreCase("settings")){
				SettingsManager.getInstance().setup(GameManager.getInstance().getPlugin());
				MessageManager.getInstance().sendMessage(PrefixType.INFO, "Settings Reloaded", player);
			}
			else if(args[0].equalsIgnoreCase("games")){	
				for(Game g:GameManager.getInstance().getGames()){
					QueueManager.getInstance().rollback(g.getID(), true);
					g.disable();
					g.enable();
				}
				MessageManager.getInstance().sendMessage(PrefixType.INFO, "Games Reloaded", player);
			}
			else if(args[0].equalsIgnoreCase("all")){	
				GameManager.getInstance().getPlugin().onDisable();
				GameManager.getInstance().getPlugin().onEnable();
				MessageManager.getInstance().sendMessage(PrefixType.INFO, "Plugin Reloaded", player);
			}
			
		} else {
			MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.nopermission", player);
		}
		return true;
	}

	@Override
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String permission() {
		return "sg.admin.reload";
	}

}
