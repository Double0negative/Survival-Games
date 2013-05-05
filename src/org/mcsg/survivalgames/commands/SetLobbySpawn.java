package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.SettingsManager;



public class SetLobbySpawn implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        if(!player.hasPermission("sg.admin.setlobby") && !player.isOp()){
            player.sendMessage(ChatColor.RED+"No Permission");
            return true;
        }
        SettingsManager.getInstance().setLobbySpawn(player.getLocation());
        player.sendMessage(ChatColor.GREEN+"Lobby spawnpoint set!");
        return true;
    }
    
    @Override
    public String help(Player p) {
        return "/sg setlobbyspawn - Set the lobby spawn point";
    }

	@Override
	public String permission() {
		return "sg.admin.setlobby";
	}
}
