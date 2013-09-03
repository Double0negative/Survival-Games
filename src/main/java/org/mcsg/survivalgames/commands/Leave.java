package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class Leave implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg leave - "
				+ SettingsManager.getInstance().getMessageConfig()
						.getString("messages.help.leave", "Leaves the game");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (GameManager.getInstance().getPlayerGameId(player) == -1) {
			MessageManager.sendFMessage(Level.SEVERE, "error.notinarena",
					player);
		} else {
			GameManager.getInstance().removePlayer(player, false);
		}
		return true;
	}

	@Override
	public String permission() {
		return null;
	}
}
