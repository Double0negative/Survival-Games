package org.mcsg.survivalgames.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.mcsg.survivalgames.Game;


public class PlayerGameDeathEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    private Player dead;
	private Player killer;
    private Game game;

    public PlayerGameDeathEvent(Player dead, Player killer, Game g) {
        this.dead = dead;
		this.killer = killer;
        game = g;
    }

    public Player getPlayer() {
        return dead;
    }

	public Player getKiller() {
		return killer;
	}

	public Game getGame() {
    	return game;
    }
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}