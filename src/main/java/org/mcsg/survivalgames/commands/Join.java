package org.mcsg.survivalgames.commands;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.MessageManager.PrefixType;


public class Join implements SubCommand{

	public boolean onCommand(Player player, String[] args) {
		if(args.length == 1){
			if(player.hasPermission(permission())){
				try {
					int a = Integer.parseInt(args[0]);
					GameManager.getInstance().addPlayer(player, a);
				} catch (NumberFormatException e) {
					MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.notanumber", player, "input-" + args[0]);
				}
			}
			else{
				MessageManager.getInstance().sendFMessage(PrefixType.WARNING, "error.nopermission", player);
			}
		}
		else{
			if(player.hasPermission("sg.lobby.join")){
				if(GameManager.getInstance().getPlayerGameId(player)!=-1){
					MessageManager.getInstance().sendMessage(PrefixType.ERROR, "error.alreadyingame", player);
					return true;
				}
				player.teleport(SettingsManager.getInstance().getLobbySpawn());
				return true;
			}
			else{
				MessageManager.getInstance().sendFMessage(PrefixType.WARNING, "error.nopermission", player);
			}
		}
		return true;
	}

	@Override
	public String help(Player p) {
		return "/sg join - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.join", "Join the lobby");
	}

	@Override
	public String permission() {
		return "sg.arena.join";
	}
}

