package org.mcsg.survivalgames.events;

import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;

public class DeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDieEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        int gameid = GameManager.getInstance().getPlayerGameId(player);
        if (gameid == -1) {
            return;
        }
        if (!GameManager.getInstance().isPlayerActive(player)) {
            return;
        }
        Game game = GameManager.getInstance().getGame(gameid);
        if (game.getMode() != Game.GameMode.INGAME) {
            event.setCancelled(true);
            return;
        }
        if (game.isProtectionOn()) {
            event.setCancelled(true);
            return;
        }
        if (player.getHealth() <= event.getDamage()) {
            event.setCancelled(true);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setFireTicks(0);

            Collection<PotionEffect> activePotionEffects = player.getActivePotionEffects();
            for (PotionEffect i : activePotionEffects) {
                player.removePotionEffect(i.getType());
            }

            PlayerInventory inv = player.getInventory();
            Location l = player.getLocation();

            for (ItemStack i : inv.getContents()) {
                if (i != null) {
                    l.getWorld().dropItemNaturally(l, i);
                }
            }
            for (ItemStack i : inv.getArmorContents()) {
                if (i != null && i.getTypeId() != 0) {
                    l.getWorld().dropItemNaturally(l, i);
                }
            }

            GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).killPlayer(player, false);



        }
    }
}