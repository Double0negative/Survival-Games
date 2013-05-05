package org.mcsg.survivalgames.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;



public class SpectatorEvents implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (GameManager.getInstance().isSpectator(player)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (GameManager.getInstance().isSpectator(player)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (GameManager.getInstance().isSpectator(player)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClickEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        try{
            if(GameManager.getInstance().isSpectator(player) && player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR)||
                    GameManager.getInstance().isSpectator(player) && player.isSneaking() && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR)){
                Player[]players = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerSpectateId(player)).getPlayers()[0];
                Game g = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerSpectateId(player));

                int i = g.getNextSpec().get(player);
                if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR)){
                    i++;
                }
                else if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR){
                    i--;
                }
                if(i>players.length-1){
                    i = 0;
                }
                if(i<0){
                    i = players.length-1;
                }
                g.getNextSpec().put(player, i);
                Player tpto = players[i];
                Location l = tpto.getLocation();
                l.setYaw(0);
                l.setPitch(0);
                player.teleport(l);
                player.sendMessage(ChatColor.AQUA+"You are now spectating "+tpto.getName());
            }
            else if (GameManager.getInstance().isSpectator(player)) {
                event.setCancelled(true);
            }
        }
        catch(Exception e){e.printStackTrace();}
    }

    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (GameManager.getInstance().isSpectator(player)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Player player = null;
        if (event.getDamager() instanceof Player) {
            player = (Player)event.getDamager();
        }
        else return;
        if (GameManager.getInstance().isSpectator(player)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Player player = null;
        if (event.getEntity() instanceof Player) {
            player = (Player)event.getEntity();
        }
        else return;
        if (GameManager.getInstance().isSpectator(player)) {
            event.setCancelled(true);
        }
    }
    
   /* @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent event) {
        Player player = null;
        if (event.getTarget() instanceof Player) {
            player = (Player)event.getTarget();
        }
        else return;
        if (GameManager.getInstance().isSpectator(player)) {
            event.setCancelled(true);
        }
    }*/
}

