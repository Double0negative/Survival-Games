package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class CreateArena implements SubCommand {

	@Override
	public String help(Player p) {
		return "/sg createarena - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.createarena",
								"Create a new arena with the current WorldEdit selection");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return true;
		}
		GameManager.getInstance().createArenaFromSelection(player);
		return true;
	}

	@Override
	public String permission() {
		return "sg.admin.createarena";
	}
}