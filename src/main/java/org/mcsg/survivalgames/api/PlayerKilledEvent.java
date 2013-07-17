package org.mcsg.survivalgames.api;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.mcsg.survivalgames.Game;

public class PlayerKilledEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Player killer;
    private DamageCause cause;
    private Game game;

    public PlayerKilledEvent(Player p, Game g, Player killer, DamageCause cause) {
        player = p;
        game = g;
        this.killer = killer;
        this.cause  = cause;
    }

    public Player getKiller(){
    	return killer;
    }
    
    public DamageCause getCause(){
    	return cause;
    }
    
    public Player getPlayer() {
        return player;
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
