package org.mcsg.survivalgames.commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.logging.QueueManager;

public class Reload implements SubCommand {

	@Override
	public String help(Player p) {
		return null;
	}

	@Override
	public boolean onCommand(final Player player, String[] args) {
		if (player.hasPermission(permission())) {
			if (args.length != 1) {
				MessageManager.sendMessage(Level.INFO,
						"Valid reload types <Settings | Games |All>", player);
				MessageManager
						.sendMessage(
								Level.INFO,
								"Settings will reload the settings configs and attempt to reapply them",
								player);
				MessageManager
						.sendMessage(
								Level.INFO,
								"Games will reload all games currently running",
								player);
				MessageManager.sendMessage(Level.INFO,
						"All will attempt to reload the entire plugin", player);

				return true;

			}
			if (args[0].equalsIgnoreCase("settings")) {
				SettingsManager.getInstance().reloadChest();
				SettingsManager.getInstance().reloadKits();
				SettingsManager.getInstance().reloadMessages();
				SettingsManager.getInstance().reloadSpawns();
				SettingsManager.getInstance().reloadSystem();
				SettingsManager.getInstance().reloadConfig();
				for (Game g : GameManager.getInstance().getGames()) {
					g.reloadConfig();
				}
				MessageManager.sendMessage(Level.INFO, "Settings Reloaded",
						player);
			} else if (args[0].equalsIgnoreCase("games")) {
				for (Game g : GameManager.getInstance().getGames()) {
					QueueManager.getInstance().rollback(g.getID(), true);
					g.disable();
					g.enable();
				}
				MessageManager
						.sendMessage(Level.INFO, "Games Reloaded", player);
			} else if (args[0].equalsIgnoreCase("all")) {
				final Plugin pinstance = GameManager.getInstance().getPlugin();
				Bukkit.getPluginManager().disablePlugin(pinstance);
				Bukkit.getPluginManager().enablePlugin(pinstance);
				MessageManager.sendMessage(Level.INFO, "Plugin reloaded",
						player);
			}

		} else {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
		}
		return true;
	}

	@Override
	public String permission() {
		return "sg.admin.reload";
	}

}
