package org.mcsg.survivalgames;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.util.MessageUtil;

public class MessageManager {

	public static String pre = ChatColor.BLUE + "" + ChatColor.BOLD + "["
			+ ChatColor.GOLD + "" + ChatColor.BOLD + "SG" + ChatColor.BLUE + ""
			+ ChatColor.BOLD + "] " + ChatColor.RESET;

	private static HashMap<Level, String> prefix = new HashMap<Level, String>();

	public static void broadcastFMessage(Level type, String input,
			String... args) {
		String msg = SettingsManager.getInstance().getMessageConfig()
				.getString("messages." + input);
		boolean enabled = SettingsManager.getInstance().getMessageConfig()
				.getBoolean("messages." + input + "_enabled", true);
		if (msg == null) {
			Bukkit.broadcastMessage(ChatColor.RED
					+ "Failed to load message for messages." + input);
			return;
		}
		if (!enabled) {
			return;
		}
		if (args != null && args.length != 0) {
			msg = MessageUtil.replaceVars(msg, args);
		}
		msg = MessageUtil.replaceColors(msg);
		Bukkit.broadcastMessage(prefix.get(Level.INFO) + prefix.get(type) + " "
				+ msg);
	}

	public static void broadcastMessage(Level type, String msg, Player player) {
		Bukkit.broadcastMessage(prefix.get(Level.INFO) + " " + prefix.get(type)
				+ " " + msg);
	}

	/**
	 * Logs a message using the Bukkit server logger.
	 * 
	 * @param level
	 *            The {@link Level}.
	 * @param message
	 *            The message.
	 */
	public static void logMessage(Level level, String message) {
		Bukkit.getServer().getLogger().log(level, message);
	}

	/**
	 * SendMessage
	 * 
	 * Loads a Message from messages.yml, converts its colors and replaces vars
	 * in the form of {$var} with its correct values, then sends to the player,
	 * adding the correct prefix
	 * 
	 * @param type
	 * @param input
	 * @param player
	 * @param vars
	 */
	public static void sendFMessage(Level type, String input, Player player,
			String... args) {
		String msg = SettingsManager.getInstance().getMessageConfig()
				.getString("messages." + input);
		boolean enabled = SettingsManager.getInstance().getMessageConfig()
				.getBoolean("messages." + input + "_enabled", true);
		if (msg == null) {
			player.sendMessage(ChatColor.RED
					+ "Failed to load message for messages." + input);
		}
		if (!enabled) {
			return;
		}
		if (args != null && args.length != 0) {
			msg = MessageUtil.replaceVars(msg, args);
		}
		msg = MessageUtil.replaceColors(msg);
		player.sendMessage(prefix.get(Level.INFO) + " " + prefix.get(type)
				+ msg);

	}

	/**
	 * 
	 * Sends a pre-formated message from the plugin to a player, adding correct
	 * prefix first
	 * 
	 * @param level
	 *            The logging {@link Level}.
	 * @param message
	 *            The message.
	 * @param player
	 *            The {@link Player}.
	 */

	public static void sendMessage(Level level, String message, Player player) {
		player.sendMessage(prefix.get(Level.INFO) + " " + prefix.get(level)
				+ message);
	}

	static {
		FileConfiguration f = SettingsManager.getInstance().getMessageConfig();
		prefix.put(Level.INFO,
				MessageUtil.replaceColors(f.getString("prefix.main")));
		prefix.put(Level.INFO,
				MessageUtil.replaceColors(f.getString("prefix.states.info")));
		prefix.put(Level.WARNING,
				MessageUtil.replaceColors(f.getString("prefix.states.warning")));
		prefix.put(Level.SEVERE,
				MessageUtil.replaceColors(f.getString("prefix.states.error")));
	}

}