package org.mcsg.survivalgames.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.mcsg.survivalgames.Game;

/**
 * An event fired when a player dies while in an arena.
 * 
 * @author Toby
 */
public class PlayerDeathEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private Player victim;
	private Player killer;
	private DamageCause cause;

	private Game game;

	public PlayerDeathEvent(Player victim, Game g, Player killer,
			DamageCause cause) {
		this.victim = victim;
		game = g;
		this.killer = killer;
		this.cause = cause;
	}

	public DamageCause getCause() {
		return cause;
	}

	public Game getGame() {
		return game;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Player getKiller() {
		return killer;
	}

	public Player getVictim() {
		return victim;
	}

}