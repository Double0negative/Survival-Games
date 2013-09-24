package org.mcsg.survivalgames.events;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.Game.GameMode;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class MoveEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void outOfBoundsHandler(PlayerMoveEvent e) {
        Player p = e.getPlayer();
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
        if (GameManager.getInstance().getBlockGameId(e.getPlayer().getLocation()) == playerGameId) {
            return;
        } else {

            Location l = e.getPlayer().getLocation();
            Location max = GameManager.getInstance().getGame(playerGameId).getArena().getMax();
            Location min = GameManager.getInstance().getGame(playerGameId).getArena().getMin();
            if (max.getBlockX() - 1 <= l.getBlockX()) {
                l.add(-5, 0, 0);
            } else if (min.getBlockX() + 1 >= l.getBlockX()) {
                l.add(5, 0, 0);
            }

            if (max.getBlockZ() - 1 <= l.getBlockZ()) {
                l.add(0, 0, -5);
            } else if (min.getBlockX() + 1 >= l.getBlockZ()) {
                l.add(0, 0, 5);
            }

            l.setY(l.getBlockY()); //WHY THIS?
            e.getPlayer().teleport(l);
        }
    }
    HashMap<Player, Vector> playerpos = new HashMap<Player, Vector>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void frozenSpawnHandler(PlayerMoveEvent e) {

        if (GameManager.getInstance().getPlayerGameId(e.getPlayer()) == -1) {
            playerpos.remove(e.getPlayer());
            return;
        }
        if (GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(e.getPlayer())).getMode() == Game.GameMode.INGAME) {
            return;
        }
        GameMode mo3 = GameManager.getInstance().getGameMode(GameManager.getInstance().getPlayerGameId(e.getPlayer()));
        if (GameManager.getInstance().isPlayerActive(e.getPlayer()) && mo3 != Game.GameMode.INGAME) {
            if (playerpos.get(e.getPlayer()) == null) {
                playerpos.put(e.getPlayer(), e.getPlayer().getLocation().toVector());
                return;
            }
            Location l = e.getPlayer().getLocation();
            Vector v = playerpos.get(e.getPlayer());
            if (l.getBlockX() != v.getBlockX() || l.getBlockZ() != v.getBlockZ()) {
                l.setX(v.getBlockX() + .5);
                l.setZ(v.getBlockZ() + .5);
                l.setYaw(e.getPlayer().getLocation().getYaw());
                l.setPitch(e.getPlayer().getLocation().getPitch());
                e.getPlayer().teleport(l);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if (!SettingsManager.getInstance().getConfig().getBoolean("compass-track")) {
            return;
        }

        Player p = e.getPlayer();
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
            e.getPlayer().setCompassTarget(compassTarget.getLocation());
        }

        for (Player gPlayer : GameManager.getInstance().getGame(playerGameId).getPlayers()[0]) {

            Player currentCompassTarget = GameManager.getInstance().getGame(playerGameId).getCompassTarget(gPlayer);

            if (currentCompassTarget == p) {
                if (gPlayer.getLocation().distance(p.getLocation()) <= 30.0D) {
                    MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "game.targetclose", gPlayer);
                    GameManager.getInstance().getGame(playerGameId).removeCompassTarget(gPlayer);
                }
                gPlayer.setCompassTarget(p.getLocation());
            }
        }
    }
}
