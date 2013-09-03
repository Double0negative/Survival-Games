package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.Game.GameMode;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class Enable implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg enable <id> - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.enable", "Enables arena <id>");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return true;
		}
		try {
			if (args.length == 0) {
				for (Game g : GameManager.getInstance().getGames()) {
					if (g.getMode() == GameMode.DISABLED) {
						g.enable();
					}
				}
				MessageManager.sendFMessage(Level.INFO, "game.all", player,
						"input-enabled");
			} else {
				GameManager.getInstance().enableGame(Integer.parseInt(args[0]));
				MessageManager.sendFMessage(Level.INFO, "game.state", player,
						"arena-" + args[0], "input-enabled");
			}
		} catch (NumberFormatException e) {
			MessageManager.sendFMessage(Level.SEVERE, "error.notanumber",
					player, "input-Arena");
		} catch (NullPointerException e) {
			MessageManager.sendFMessage(Level.SEVERE, "error.gamedoesntexist",
					player, "arena-" + args[0]);
		}
		return true;

	}

	@Override
	public String permission() {
		return "sg.arena.enable";
	}
}