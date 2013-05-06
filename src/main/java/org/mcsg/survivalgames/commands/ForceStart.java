package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.MessageManager.PrefixType;



public class ForceStart implements SubCommand {
	
	MessageManager msgmgr = MessageManager.getInstance();
	
    public boolean onCommand(Player player, String[] args) {
        
        if(!player.hasPermission("sg.staff.forcestart") && !player.isOp()){
            player.sendMessage(ChatColor.RED+ "No Permission");
            return true;
        }
        int game = -1;
        int seconds = 10;
        if(args.length == 2){
            seconds = Integer.parseInt(args[1]);
        }
        if(args.length >= 1){
            game = Integer.parseInt(args[0]);
            
        }
        else
            game  = GameManager.getInstance().getPlayerGameId(player);
        if(game == -1){
            player.sendMessage(ChatColor.RED+"Must be in a game!");
            return true;
        }
        if(GameManager.getInstance().getGame(game).getActivePlayers() < 2){
            player.sendMessage(ChatColor.RED+"Needs at least 2 players to start!");
            return true;
        }
        
        
		Game g = GameManager.getInstance().getGame(game);
		if(g.getMode() != Game.GameMode.WAITING && !player.hasPermission("sg.arena.restart")){
		    player.sendMessage(ChatColor.RED+"Game Already Starting!");
		    return true;
		}
		g.countdown(seconds);
		for (Player pl : g.getAllPlayers()) {
        	msgmgr.sendMessage(PrefixType.INFO, "Game starting in " + seconds + " seconds!", pl);
        }
		player.sendMessage(ChatColor.GREEN+"Started arena "+game);
		
		return true;
	}
    
    @Override
    public String help(Player p) {
        return "/sg forcestart - Force starts a game";
    }

	@Override
	public String permission() {
		return "sg.staff.forcestart";
	}
}