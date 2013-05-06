package org.mcsg.survivalgames.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mcsg.survivalgames.GameManager;



public class SignClickEvent implements Listener{


    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(PlayerInteractEvent e){

        if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK || e.getAction()==Action.LEFT_CLICK_BLOCK)) return;
        

        Block clickedBlock = e.getClickedBlock(); 
        if(!(clickedBlock.getType()==Material.SIGN || clickedBlock.getType()==Material.SIGN_POST || clickedBlock.getType()==Material.WALL_SIGN)) return;
        Sign thisSign = (Sign) clickedBlock.getState();
        //System.out.println("Clicked sign");
        String[] lines = thisSign.getLines();
        if(lines.length<3) return;
        if(lines[0].equalsIgnoreCase("[SurvivalGames]")) {
            e.setCancelled(true);
            try{
                if(lines[2].equalsIgnoreCase("Auto Assign")){
                    GameManager.getInstance().autoAddPlayer(e.getPlayer());
                }
                else{
                    String game = lines[2].replace("Arena ", "");
                    int gameno  = Integer.parseInt(game);
                    GameManager.getInstance().addPlayer(e.getPlayer(), gameno);
                }

            }catch(Exception ek){}
        }

    }

}


