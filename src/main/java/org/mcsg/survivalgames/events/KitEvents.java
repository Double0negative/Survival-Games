package org.mcsg.survivalgames.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.mcsg.survivalgames.GameManager;

public class KitEvents implements Listener  {

	@EventHandler
	public void itemClick( 	InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player)e.getWhoClicked();
			if(GameManager.getInstance().isInKitMenu(p)){
				if(e.getRawSlot() == e.getSlot()){
                    ItemStack iS = e.getCurrentItem();
                    if (iS == null){
                        return;
                    }
                    if (iS.getType().equals(Material.AIR)){
                        return;
                    }
                    if (!GameManager.getInstance().kitCheck(p, e.getRawSlot() % 9)){
                        e.setResult(Event.Result.DENY);
                        e.setCancelled(true);
                        return;
                    }
					GameManager.getInstance().selectKit(p, e.getRawSlot() % 9);
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void InvClose(InventoryCloseEvent e){
		GameManager.getInstance().leaveKitMenu((Player)e.getPlayer());
	}


}
