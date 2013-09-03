package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class SetLobbySpawn implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg setlobbyspawn - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.setlobbyspawn",
								"Set the lobby spawnpoint");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return true;
		}
		SettingsManager.getInstance().setLobbySpawn(player.getLocation());
		MessageManager.sendMessage(Level.INFO, "info.lobbyspawn", player);
		return true;
	}

	@Override
	public String permission() {
		return "sg.admin.setlobby";
	}
}
