package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.MessageManager.PrefixType;
import org.mcsg.survivalgames.SettingsManager;



public class ListArenas implements SubCommand{
	
    public boolean onCommand(Player player, String[] args) {
    	StringBuilder arenas = new StringBuilder();
    	try{
    	if(args.length == 0 || Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > GameManager.getInstance().getGameCount()){
    		MessageManager.getInstance().sendMessage(PrefixType.ERROR, "error.gamenoexist", player);
    	}
    	if (GameManager.getInstance().getGames().isEmpty()) {
    		arenas.append(SettingsManager.getInstance().getMessageConfig().getString("messages.words.noarenas", "No arenas")).append(": ");
    		player.sendMessage(ChatColor.RED + arenas.toString());
        	return true;
    	}
    	arenas.append(SettingsManager.getInstance().getMessageConfig().getString("messages.words.noarenas", "Arenas")).append(": ");
        for (Game g : GameManager.getInstance().getGames()) {
        	arenas.append(g.getID()).append(", ");
        }
        player.sendMessage(ChatColor.GREEN + arenas.toString());
    	}catch(Exception e){
    		MessageManager.getInstance().sendMessage(PrefixType.ERROR, "error.gamenoexist", player);
    	}
        return false;
    }
    
    @Override
    public String help(Player p) {
        return "/sg listarenas - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.listarenas", "List all available arenas");
    }

	@Override
	public String permission() {
		return "sg.player.listarenas";
	}
}