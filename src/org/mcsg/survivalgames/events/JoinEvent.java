package org.mcsg.survivalgames.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.util.UpdateChecker;



public class JoinEvent implements Listener {
    
    Plugin plugin;
    
    public JoinEvent(Plugin plugin){
        this.plugin = plugin;
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        
/*        if (p.getName().equalsIgnoreCase("Double0negative") || p.getName().equalsIgnoreCase("PogoStick29")) {
        	if (SettingsManager.getInstance().getConfig().getBoolean("broadcastdevmessage")) {
        		Bukkit.getServer().broadcastMessage(MessageManager.getInstance().pre + ChatColor.GREEN + ChatColor.BOLD + p.getName() + ChatColor.GREEN + " created SurvivalGames!");
        	}
        }
*/
        
        if(GameManager.getInstance().getBlockGameId(p.getLocation()) != -1){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                public void run(){
                    p.teleport(SettingsManager.getInstance().getLobbySpawn());

                }
            }, 5L);
        }
        if((p.isOp() || p.hasPermission("sg.system.updatenotify")) && SettingsManager.getInstance().getConfig().getBoolean("check-for-update", true)){
            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

                public void run() {
                    System.out.println("[SG]Checking for updates");
                    new UpdateChecker().check(p, plugin);
                }
             }, 60L);
        }
    }
    
}
