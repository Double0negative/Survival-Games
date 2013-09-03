package org.mcsg.survivalgames.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.mcsg.survivalgames.Game;

public class PlayerJoinArenaEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private Game game;

	private boolean cancelled = false;

	/**
	 * Creates a new player join arena event.
	 * 
	 * @param p
	 *            The {@link Player}.
	 * @param g
	 *            The {@link Game}.
	 */
	public PlayerJoinArenaEvent(Player p, Game g) {
		player = p;
		game = g;
	}

	public Game getGame() {
		return game;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

}