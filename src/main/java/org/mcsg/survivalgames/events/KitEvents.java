package org.mcsg.survivalgames.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.mcsg.survivalgames.GameManager;

public class KitEvents implements Listener  {

	@EventHandler
	public void itemClick( 	InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player)e.getWhoClicked();
			if(GameManager.getInstance().isInKitMenu(p)){
				if(e.getRawSlot() == e.getSlot()){
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
