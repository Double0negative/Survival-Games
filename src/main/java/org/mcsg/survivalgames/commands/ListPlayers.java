package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.MessageManager.PrefixType;



public class ListPlayers implements SubCommand{

	@Override
	public boolean onCommand(Player player, String[] args) {
		int gid = 0;
		try{
			if(args.length == 0){
				gid = GameManager.getInstance().getPlayerGameId(player);
			}
			else{
				gid =  Integer.parseInt(args[0]);
			}

			String[] msg = GameManager.getInstance().getStringList(gid).split("\n");
			player.sendMessage(msg);
			return false;
		}catch(NumberFormatException e){
			MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input",player, "message-Game must be a number!");
		}catch(NullPointerException e){
			MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input",player, "message-No game by this ID exist!");
		}
		return false;
	}

	@Override
	public String help(Player p) {
		return "/list - List all players in the arena you are playing in";
	}

	@Override
	public String permission() {
		return "sg.player.list";
	}

}