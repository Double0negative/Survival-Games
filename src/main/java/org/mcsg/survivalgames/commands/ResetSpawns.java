package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.MessageManager.PrefixType;



public class ResetSpawns implements SubCommand{

    public boolean onCommand(Player player, String[] args) {
        
        if(!player.hasPermission("sg.admin.resetspawns") && !player.isOp()){
            player.sendMessage(ChatColor.RED+ "No Permission");
            return true;
        }
        try{
        SettingsManager.getInstance().getSpawns().set("spawns."+Integer.parseInt(args[0]), null);
        return true;
		}catch(NumberFormatException e){
			MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input",player, "message-Game must be a number!");
		}catch(NullPointerException e){
			MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input",player, "message-No game by this ID exist!");
		}
        return true;
    }   

    @Override
    public String help(Player p) {
        return "/sg resetspawns <id> - Resets spawns for Arena <id>";
    }

	@Override
	public String permission() {
		return "sg.admin.resetspawns";
	}
}