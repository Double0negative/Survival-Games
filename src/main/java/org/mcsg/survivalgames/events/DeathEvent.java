package org.mcsg.survivalgames.events;

import net.minecraft.server.v1_6_R2.Packet205ClientCommand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;

public class DeathEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDieEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		int gameid = GameManager.getInstance().getPlayerGameId(player);
		if (gameid <= 0) {
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
        //Start AEM
		/*if (player.getHealth() <= event.getDamage()) {
			event.setCancelled(true);
			GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).killPlayer(player, false);

		}*/
        //End AEM
	}


    //Start AEM
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        int gameid = GameManager.getInstance().getPlayerGameId(player);
        if (gameid <= 0) {
            return;
        }
        respawn(e.getEntity());
        GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).killPlayer(player, false);
    }

    //NMS
    public void respawn(Player pl){
        Packet205ClientCommand packet = new Packet205ClientCommand();
        packet.a = 1;
        ((CraftPlayer)pl).getHandle().playerConnection.a(packet);
    }
    //End AEM
}
