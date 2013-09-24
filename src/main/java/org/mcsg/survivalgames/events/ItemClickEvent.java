package org.mcsg.survivalgames.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class ItemClickEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCompassClick(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        Block clickedBlock = e.getClickedBlock();

        if (clickedBlock.getType() == Material.SIGN || clickedBlock.getType() == Material.SIGN_POST || clickedBlock.getType() == Material.WALL_SIGN) {
            Sign thisSign = (Sign) clickedBlock.getState();
            String[] lines = thisSign.getLines();
            if (lines.length < 3) {
                return;
            }
            if (lines[0].equalsIgnoreCase("[SurvivalGames]")) {
                e.setCancelled(true);
                try {
                    if (lines[2].equalsIgnoreCase("Auto Assign")) {
                        GameManager.getInstance().autoAddPlayer(e.getPlayer());
                    } else {
                        String game = lines[2].replace("Arena ", "");
                        int gameno = Integer.parseInt(game);
                        GameManager.getInstance().addPlayer(e.getPlayer(), gameno);
                    }

                } catch (Exception ek) {
                }
            }
            
            return;
        }

        if (!SettingsManager.getInstance().getConfig().getBoolean("compass-track")) {
            return;
        }
        
        Player p = e.getPlayer();
        Player targ = null;

        if (p.getItemInHand().getType() == Material.COMPASS && e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            int playerGameId = GameManager.getInstance().getPlayerGameId(p);

            if (playerGameId == -1) {
                return;
            }

            if (!GameManager.getInstance().isPlayerActive(e.getPlayer())) {
                return;
            }

            if (GameManager.getInstance().getGameMode(playerGameId) == Game.GameMode.WAITING) {
                return;
            }

            for (Player gPlayer : GameManager.getInstance().getGame(playerGameId).getPlayers()[0]) {

                if (p != gPlayer) {
                    if ((targ == null) || (p.getLocation().distance(gPlayer.getLocation()) < targ.getLocation().distance(gPlayer.getLocation()))) {
                        if ((gPlayer.getLocation().distance(p.getLocation()) > 30.0D)) {

                            targ = gPlayer;
                        }
                    }
                }

            }

            if (targ == null) {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "game.notarget", p);
                return;
            }

            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "game.targetfinded", p, "target-" + targ.getName());
            GameManager.getInstance().getGame(playerGameId).setCompassTarget(p, targ);
            
            return;
        }

        if (p.getItemInHand().getType() == Material.COMPASS && e.getAction() == Action.LEFT_CLICK_BLOCK) {

            int playerGameId = GameManager.getInstance().getPlayerGameId(p);

            if (playerGameId == -1) {
                return;
            }

            if (!GameManager.getInstance().isPlayerActive(e.getPlayer())) {
                return;
            }

            if (GameManager.getInstance().getGameMode(playerGameId) == Game.GameMode.WAITING) {
                return;
            }

            Player compassTarget = GameManager.getInstance().getGame(playerGameId).getCompassTarget(p);

            if (compassTarget != null) {
                int distance = (int) p.getLocation().distance(compassTarget.getLocation());

                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "game.targetdistance", p, "distance-" + distance);
            } else {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "game.notarget", p);
            }
            
            return;
        }
    }
}
