package org.mcsg.survivalgames.events;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.Game.GameMode;
import org.mcsg.survivalgames.util.ChestRatioStorage;



public class ChestReplaceEvent implements Listener{


    @EventHandler(priority = EventPriority.HIGHEST)
    public void ChestListener(PlayerInteractEvent e){
        try{
            HashSet<Block>openedChest3 = new HashSet<Block>();

            if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK)) return;

            Block clickedBlock = e.getClickedBlock(); 
            int gameid = GameManager.getInstance().getPlayerGameId(e.getPlayer());
            if(gameid == -1) return;
            GameManager gm = GameManager.getInstance();
            
            if(!gm.isPlayerActive(e.getPlayer())){
                return;
            }
            
            if(gm.getGame(gameid).getMode() != GameMode.INGAME){
            	e.setCancelled(true);
                return;
            }
            
            if(GameManager.openedChest.get(gameid) !=null){
                openedChest3.addAll(GameManager.openedChest.get(gameid));
            }
            
            if(openedChest3.contains(clickedBlock)){
                return;
            }
            
            Inventory inv;
            int size = 0;
            
            if (clickedBlock.getState() instanceof Chest) {
                size = 1;
                inv  = ((Chest) clickedBlock.getState()).getInventory();

            }
            else if(clickedBlock.getState() instanceof DoubleChest){
                size = 2;
                inv = ((DoubleChest) clickedBlock.getState()).getInventory();

            }
            else return;

            inv.clear();
            Random r = new Random();

            for(ItemStack i: ChestRatioStorage.getInstance().getItems()){
                int l = r.nextInt(26 * size);

                while(inv.getItem(l) != null)
                    l = r.nextInt(26 * size);
                inv.setItem(l, i);


            }
            openedChest3.add(clickedBlock);
            GameManager.openedChest.put(gameid, openedChest3);
        }
        catch(Exception e5){}
    }



}
