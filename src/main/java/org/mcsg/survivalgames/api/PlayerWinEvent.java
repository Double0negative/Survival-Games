package org.mcsg.survivalgames.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.mcsg.survivalgames.Game;


public class PlayerWinEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    private Player winner;
	private Player killer;
    private Game game;

    public PlayerWinEvent(Player winner, Player killer, Game g) {
        this.winner = winner;
		this.killer = killer;
        game = g;
    }

    public Player getPlayer() {
        return winner;
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