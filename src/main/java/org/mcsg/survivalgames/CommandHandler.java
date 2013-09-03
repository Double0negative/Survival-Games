package org.mcsg.survivalgames;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import org.mcsg.survivalgames.commands.AddWall;
import org.mcsg.survivalgames.commands.CreateArena;
import org.mcsg.survivalgames.commands.DelArena;
import org.mcsg.survivalgames.commands.Disable;
import org.mcsg.survivalgames.commands.Enable;
import org.mcsg.survivalgames.commands.Flag;
import org.mcsg.survivalgames.commands.Start;
import org.mcsg.survivalgames.commands.Join;
import org.mcsg.survivalgames.commands.Leave;
import org.mcsg.survivalgames.commands.LeaveQueue;
import org.mcsg.survivalgames.commands.ListArenas;
import org.mcsg.survivalgames.commands.ListPlayers;
import org.mcsg.survivalgames.commands.Reload;
import org.mcsg.survivalgames.commands.ResetSpawns;
import org.mcsg.survivalgames.commands.SetLobbySpawn;
import org.mcsg.survivalgames.commands.SetLobbyWall;
import org.mcsg.survivalgames.commands.SetSpawn;
import org.mcsg.survivalgames.commands.Spectate;
import org.mcsg.survivalgames.commands.SubCommand;
import org.mcsg.survivalgames.commands.Teleport;
import org.mcsg.survivalgames.commands.Vote;

public class CommandHandler implements CommandExecutor {

	private Plugin plugin;
	private HashMap<String, SubCommand> commands;
	private HashMap<String, Integer> helpinfo;

	public CommandHandler(Plugin plugin) {
		this.plugin = plugin;
		commands = new HashMap<String, SubCommand>();
		helpinfo = new HashMap<String, Integer>();
		loadCommands();
		loadHelpInfo();
	}

	public void help(Player p, int page) {
		if (page == 1) {
			p.sendMessage(ChatColor.BLUE + "------------ " + MessageManager.pre
					+ ChatColor.DARK_AQUA + " Player Commands" + ChatColor.BLUE
					+ " ------------");
		}
		if (page == 2) {
			p.sendMessage(ChatColor.BLUE + "------------ " + MessageManager.pre
					+ ChatColor.DARK_AQUA + " Staff Commands" + ChatColor.BLUE
					+ " ------------");
		}
		if (page == 3) {
			p.sendMessage(ChatColor.BLUE + "------------ " + MessageManager.pre
					+ ChatColor.DARK_AQUA + " Admin Commands" + ChatColor.BLUE
					+ " ------------");
		}

		for (String command : commands.keySet()) {
			try {
				if (helpinfo.get(command) == page) {

					MessageManager.sendMessage(Level.INFO, commands
							.get(command).help(p), p);
				}
			} catch (Exception e) {
			}
		}
		/*
		 * for (SubCommand v : commands.values()) { if (v.permission() != null)
		 * { if (p.hasPermission(v.permission())) {
		 * MessageManager.sendMessage(PrefixType.INFO1, v.help(p), p); } else {
		 * MessageManager.sendMessage(PrefixType.WARNING, v.help(p), p); } }
		 * else { MessageManager.sendMessage(PrefixType.INFO, v.help(p), p); } }
		 */
	}

	private void loadCommands() {
		commands.put("createarena", new CreateArena());
		commands.put("join", new Join());
		commands.put("addwall", new AddWall());
		commands.put("setspawn", new SetSpawn());
		commands.put("getcount", new ListArenas());
		commands.put("disable", new Disable());
		commands.put("start", new Start());
		commands.put("enable", new Enable());
		commands.put("vote", new Vote());
		commands.put("leave", new Leave());
		commands.put("setlobbyspawn", new SetLobbySpawn());
		commands.put("setlobbywall", new SetLobbyWall());
		commands.put("resetspawns", new ResetSpawns());
		commands.put("delarena", new DelArena());
		commands.put("flag", new Flag());
		commands.put("spectate", new Spectate());
		commands.put("lq", new LeaveQueue());
		commands.put("leavequeue", new LeaveQueue());
		commands.put("list", new ListPlayers());
		commands.put("tp", new Teleport());
		commands.put("reload", new Reload());

		// commands.put("sponsor", new Sponsor());
	}

	private void loadHelpInfo() {
		helpinfo.put("createarena", 3);
		helpinfo.put("join", 1);
		helpinfo.put("addwall", 3);
		helpinfo.put("setspawn", 3);
		helpinfo.put("getcount", 3);
		helpinfo.put("disable", 2);
		helpinfo.put("start", 2);
		helpinfo.put("enable", 2);
		helpinfo.put("vote", 1);
		helpinfo.put("leave", 1);
		helpinfo.put("setlobbyspawn", 3);
		helpinfo.put("resetspawns", 3);
		helpinfo.put("delarena", 3);
		helpinfo.put("flag", 3);
		helpinfo.put("spectate", 1);
		helpinfo.put("lq", 1);
		helpinfo.put("leavequeue", 1);
		helpinfo.put("list", 1);

		// helpinfo.put("sponsor", 1);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd1,
			String commandLabel, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		if (!(sender instanceof Player)) {
			MessageManager.logMessage(Level.WARNING,
					"Only in-game players can use SurvivalGames commands!");
			return true;
		}

		Player player = (Player) sender;

		if (SurvivalGames.UPDATED_CONFIG == false) {
			MessageManager
					.sendMessage(
							Level.WARNING,
							"The config file is out of date. Please tell an administrator to reset the config.",
							player);
			return true;
		}

		if (SurvivalGames.dbcon == false) {
			MessageManager.sendMessage(Level.WARNING,
					"Could not connect to server. Plugin disabled.", player);
			return true;
		}

		if (cmd1.getName().equalsIgnoreCase("survivalgames")) {
			if (args == null || args.length < 1) {
				MessageManager.sendMessage(Level.INFO,
						"Version " + pdfFile.getVersion()
								+ " by Double0negative", player);
				MessageManager
						.sendMessage(
								Level.INFO,
								"Type /sg help <player | staff | admin> for command information",
								player);
				return true;
			}
			if (args[0].equalsIgnoreCase("help")) {
				if (args.length == 1) {
					help(player, 1);
				} else {
					if (args[1].toLowerCase().startsWith("player")) {
						help(player, 1);
						return true;
					}
					if (args[1].toLowerCase().startsWith("staff")) {
						help(player, 2);
						return true;
					}
					if (args[1].toLowerCase().startsWith("admin")) {
						help(player, 3);
						return true;
					} else {
						MessageManager
								.sendMessage(
										Level.WARNING,
										args[1]
												+ " is not a valid page! Valid pages are Player, Staff, and Admin.",
										player);
					}
				}
				return true;
			}
			String sub = args[0];
			Vector<String> l = new Vector<String>();
			l.addAll(Arrays.asList(args));
			l.remove(0);
			args = l.toArray(new String[0]);
			if (!commands.containsKey(sub)) {
				MessageManager.sendMessage(Level.WARNING,
						"Command doesn't exist.", player);
				MessageManager.sendMessage(Level.INFO,
						"Type /sg help for command information", player);
				return true;
			}
			try {
				commands.get(sub).onCommand(player, args);
			} catch (Exception e) {
				e.printStackTrace();
				MessageManager.sendFMessage(Level.SEVERE, "error.command",
						player,
						"command-[" + sub + "] " + Arrays.toString(args));
				MessageManager.sendMessage(Level.INFO,
						"Type /sg help for command information", player);
			}
			return true;
		}
		return false;
	}
}