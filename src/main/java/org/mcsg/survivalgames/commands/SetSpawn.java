package org.mcsg.survivalgames.commands;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class SetSpawn implements SubCommand {

	HashMap<Integer, Integer> next = new HashMap<Integer, Integer>();

	public SetSpawn() {

	}

	@Override
	public String help(Player p) {
		return "/sg setspawn next - "
				+ SettingsManager
						.getInstance()
						.getMessageConfig()
						.getString("messages.help.setspawn",
								"Sets a spawn for the arena you are located in");
	}

	public void loadNextSpawn() {
		for (Game g : GameManager.getInstance().getGames().toArray(new Game[0])) { // Avoid
																					// Concurrency
																					// problems
			next.put(g.getID(),
					SettingsManager.getInstance().getSpawnCount(g.getID()) + 1);
		}
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.sendFMessage(Level.SEVERE, "error.nopermission",
					player);
			return true;
		}

		loadNextSpawn();
		System.out.println("settings spawn");
		Location l = player.getLocation();
		int game = GameManager.getInstance().getBlockGameId(l);
		System.out.println(game + " " + next.size());
		if (game == -1) {
			MessageManager
					.sendMessage(Level.SEVERE, "error.notinarena", player);
			return true;
		}
		int i = 0;
		if (args[0].equalsIgnoreCase("next")) {
			i = next.get(game);
			next.put(game, next.get(game) + 1);
		} else {
			try {
				i = Integer.parseInt(args[0]);
				if (i > next.get(game) + 1 || i < 1) {
					MessageManager.sendFMessage(Level.SEVERE, "error.between",
							player, "num-" + next.get(game));
					return true;
				}
				if (i == next.get(game)) {
					next.put(game, next.get(game) + 1);
				}
			} catch (Exception e) {
				MessageManager.sendMessage(Level.SEVERE, "error.badinput",
						player);
				return false;
			}
		}
		if (i == -1) {
			MessageManager.sendMessage(Level.SEVERE, "error.notinside", player);
			return true;
		}
		SettingsManager.getInstance().setSpawn(game, i, l.toVector());
		MessageManager.sendFMessage(Level.INFO, "info.spawnset", player, "num-"
				+ i, "arena-" + game);
		return true;
	}

	@Override
	public String permission() {
		return "sg.admin.setarenaspawns";
	}
}