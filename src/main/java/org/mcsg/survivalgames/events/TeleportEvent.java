package org.mcsg.survivalgames.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.mcsg.survivalgames.GameManager;


public class TeleportEvent implements Listener{

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event){
        Player p = event.getPlayer();
        int id = GameManager.getInstance().getPlayerGameId(p);
        if(id == -1) return;
        if(GameManager.getInstance().getGame(id).isPlayerActive(p) && event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND){
            p.sendMessage(ChatColor.RED +" Cannot teleport while ingame!");
            event.setCancelled(true);
        }
    }

}