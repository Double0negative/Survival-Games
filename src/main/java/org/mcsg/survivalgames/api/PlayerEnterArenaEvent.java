package org.mcsg.survivalgames.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import org.bukkit.event.Event;
import org.mcsg.survivalgames.Game;

public class PlayerEnterArenaEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Game game;
    private boolean cancelled = false;
    
	public PlayerEnterArenaEvent(Player p, Game game) {
		this.player = p;
		this.game = game;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}

	public Game getGame()
	{
		return this.game;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	   return handlers;
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
