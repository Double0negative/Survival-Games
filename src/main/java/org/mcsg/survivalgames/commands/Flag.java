package org.mcsg.survivalgames.commands;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;



public class Flag implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        
        if (!player.hasPermission(permission())) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.nopermission", player);
            return true;
        }
        
        if(args.length < 2){
            player.sendMessage(help(player));
            return true;
        }
        
        Game g = GameManager.getInstance().getGame(Integer.parseInt(args[0]));
        
        if(g == null){
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.gamedoesntexist", player, "arena-" + args[0]);
            return true;
        }
        
        HashMap<String, Object>z = SettingsManager.getInstance().getGameFlags(g.getID());
        z.put(args[1].toUpperCase(), g.getID());
        SettingsManager.getInstance().saveGameFlags(z, g.getID());
        		

        
        return false;
    }

    @Override
    public String help(Player p) {
        return "/sg flag <id> <flag> <value> - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.flag", "Modifies an arena-specific setting");
    }

	@Override
	public String permission() {
		return "sg.admin.flag";
	}
}
