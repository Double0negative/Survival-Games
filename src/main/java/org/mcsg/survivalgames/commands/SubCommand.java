package org.mcsg.survivalgames.commands;

import org.bukkit.entity.Player;

public interface SubCommand {

	public String help(Player p);

	public boolean onCommand(Player player, String[] args);

	public String permission();

}