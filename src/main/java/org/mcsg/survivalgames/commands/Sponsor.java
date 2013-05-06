package org.mcsg.survivalgames.commands;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;



public class Sponsor implements SubCommand {

	MessageManager msgmgr = MessageManager.getInstance();
	
	@Override
	public boolean onCommand(Player player, String[] args) {
	/*	if (!player.hasPermission("sg.player.sponsor") && !player.isOp()) {
            player.sendMessage(ChatColor.RED + "No Permission");
            return true;
        }
		if (args.length != 3) {
			player.sendMessage(ChatColor.GREEN + "/sg sponsor <player> <itemid> <amount> - Gives <player> <amount> of <item>. If they win, you get some money back!");
			return true;
		}
		if (Bukkit.getPlayer(args[0]) == null) {
			player.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
			return true;
		}
		boolean playerActive = false;
		for (Game g : GameManager.getInstance().getGames()) {
			if (g.getAllPlayers().contains(player)) {
				playerActive = true;
				break;
			}
		}
		if (playerActive == false) {
			player.sendMessage(ChatColor.RED + "Player " + args[0] + " is not active in a game!");
			return true;
		}
		if (SettingsManager.getInstance().getSponsor().getConfigurationSection("items") == null) {
			player.sendMessage(ChatColor.RED + "Error!");
			return true;
		}
		if (!(SettingsManager.getInstance().getSponsor().getConfigurationSection("items").contains(args[1]))) {
			player.sendMessage(ChatColor.RED + "The item ID " + args[1] + " is not available for sponsorship.");
			return true;
		}
		int item;
		try {
			item = Integer.parseInt(args[1]);
		}
		catch (Exception e) {
			player.sendMessage(ChatColor.RED + args[1] + " is not a valid number!");
			return true;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[2]);
		}
		catch (Exception e) {
			player.sendMessage(ChatColor.RED + args[2] + " is not a valid number!");
			return true;
		}
		//Charge player!
		Bukkit.getPlayer(args[0]).getInventory().addItem(new ItemStack(item, amount));
		msgmgr.sendMessage(PrefixType.INFO, "You sponsored player " + args[0] + " with " + amount + " of item ID " + item + "!", player);
		msgmgr.sendMessage(PrefixType.INFO, player.getName() + " sponsored you with " + amount + " of item ID " + item + "!", Bukkit.getServer().getPlayer(args[0]));
		return true;*/
		
		
		/* NOT USED */
		return true;
	}

	@Override
	public String help(Player p) {
		return "/sg sponsor <player> <itemid> <amount> - Sponsor a player!";
	}

	@Override
	public String permission() {
		return "sg.player.sponsor";
	}
}