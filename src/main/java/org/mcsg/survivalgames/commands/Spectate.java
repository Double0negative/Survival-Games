package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class Spectate implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg spectate <id> - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.spectate",
								"Spectate a running arena");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return true;
		}

		if (args.length == 0) {
			if (GameManager.getInstance().isSpectator(player)) {
				GameManager.getInstance().removeSpectator(player);
				return true;
			} else {
				MessageManager.sendFMessage(Level.SEVERE, "error.notspecified",
						player, "input-Game ID");
				return true;
			}
		}
		if (SettingsManager.getInstance().getSpawnCount(
				Integer.parseInt(args[0])) == 0) {
			MessageManager.sendMessage(Level.SEVERE, "error.nospawns", player);
			return true;
		}
		if (GameManager.getInstance().isPlayerActive(player)) {
			MessageManager
					.sendMessage(Level.SEVERE, "error.specingame", player);
			return true;
		}
		GameManager.getInstance().getGame(Integer.parseInt(args[0]))
				.addSpectator(player);
		return true;
	}

	@Override
	public String permission() {
		return "sg.arena.spectate";
	}

}
