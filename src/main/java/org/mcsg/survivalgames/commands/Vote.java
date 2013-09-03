package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class Vote implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg vote - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.vote",
								"Votes to start the game");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return false;
		}
		int game = GameManager.getInstance().getPlayerGameId(player);
		if (game == -1) {
			MessageManager
					.sendMessage(Level.SEVERE, "error.notinarena", player);
			return true;
		}

		GameManager.getInstance()
				.getGame(GameManager.getInstance().getPlayerGameId(player))
				.vote(player);

		return true;
	}

	@Override
	public String permission() {
		return "sg.arena.vote";
	}
}