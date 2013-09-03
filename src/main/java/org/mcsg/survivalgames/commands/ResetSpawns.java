package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class ResetSpawns implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg resetspawns <id> - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.resetspawns",
								"Resets spawns for Arena <id>");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {

		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return true;
		}
		try {
			SettingsManager.getInstance().getSpawns()
					.set("spawns." + Integer.parseInt(args[0]), null);
			return true;
		} catch (NumberFormatException e) {
			MessageManager.sendFMessage(Level.SEVERE, "error.notanumber",
					player, "input-Arena");
		} catch (NullPointerException e) {
			MessageManager.sendMessage(Level.SEVERE, "error.gamenoexist",
					player);
		}
		return true;
	}

	@Override
	public String permission() {
		return "sg.admin.resetspawns";
	}
}