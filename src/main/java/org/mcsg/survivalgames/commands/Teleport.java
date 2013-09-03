package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class Teleport implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg tp <arenaid> - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.teleport",
								"Teleport to an arena");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (player.hasPermission(permission())) {
			if (args.length == 1) {
				try {
					int a = Integer.parseInt(args[0]);
					try {
						player.teleport(SettingsManager.getInstance()
								.getSpawnPoint(a, 1));
					} catch (Exception e) {
						MessageManager.sendMessage(Level.SEVERE,
								"error.nospawns", player);
					}
				} catch (NumberFormatException e) {
					MessageManager.sendFMessage(Level.SEVERE,
							"error.notanumber", player, "input-" + args[0]);
				}
			} else {
				MessageManager.sendFMessage(Level.SEVERE, "error.notspecified",
						player, "input-Game ID");
			}
		} else {
			MessageManager.sendFMessage(Level.WARNING, "error.nopermission",
					player);
		}
		return true;
	}

	@Override
	public String permission() {
		return "sg.arena.teleport";
	}

}
