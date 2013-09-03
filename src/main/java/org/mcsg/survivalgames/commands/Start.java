package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class Start implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg forcestart - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.forcestart",
								"Forces the game to start");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {

		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return true;
		}
		int game = -1;
		int seconds = 10;
		if (args.length == 2) {
			seconds = Integer.parseInt(args[1]);
		}
		if (args.length >= 1) {
			game = Integer.parseInt(args[0]);

		} else {
			game = GameManager.getInstance().getPlayerGameId(player);
		}
		if (game == -1) {
			MessageManager
					.sendFMessage(Level.SEVERE, "error.notingame", player);
			return true;
		}
		if (GameManager.getInstance().getGame(game).getActivePlayers() < 2) {
			MessageManager.sendFMessage(Level.SEVERE,
					"error.notenoughtplayers", player);
			return true;
		}

		Game g = GameManager.getInstance().getGame(game);
		if (g.getMode() != Game.GameMode.WAITING
				&& !player.hasPermission("sg.arena.restart")) {
			MessageManager.sendFMessage(Level.SEVERE, "error.alreadyingame",
					player);
			return true;
		}
		g.countdown(seconds);

		MessageManager.sendFMessage(Level.INFO, "game.started", player,
				"arena-" + game);

		return true;
	}

	@Override
	public String permission() {
		return "sg.arena.start";
	}
}