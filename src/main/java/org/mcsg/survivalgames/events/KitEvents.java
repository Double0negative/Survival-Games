package org.mcsg.survivalgames.events;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.util.Kit;

public class KitEvents implements Listener {

    @EventHandler
    public void itemClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (GameManager.getInstance().isInKitMenu(p)) {
                if (e.getRawSlot() == e.getSlot()) {
                    String kitMode = SettingsManager.getInstance().getKits().getString("kit-select-mode");
                    boolean showCont = (kitMode.equalsIgnoreCase("SHOW_ALL")) ? true : false;
                    ArrayList<Kit> kits = GameManager.getInstance().getKits(p);
                    
                    if (showCont && kits.size() <= 9) {
                        GameManager.getInstance().selectKit(p, e.getRawSlot() % 9);
                    }else{ 
                        if(e.getRawSlot() < kits.size()){
                            GameManager.getInstance().selectKit(p, e.getRawSlot());
                        }
                    }
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InvClose(InventoryCloseEvent e) {
        GameManager.getInstance().leaveKitMenu((Player) e.getPlayer());
    }
}
