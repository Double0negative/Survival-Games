package org.mcsg.survivalgames.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.SurvivalGames;
import org.mcsg.survivalgames.util.UpdateChecker;

import java.io.File;
import java.io.IOException;


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


        if (SurvivalGames.playStats.get("Players." + p.getName()) != null){
            SurvivalGames.playerKills.put(p.getName(), SurvivalGames.playStats.getInt("Players." + p.getName() + ".Kills"));
            SurvivalGames.playerWins.put(p.getName(), SurvivalGames.playStats.getInt("Players." + p.getName() + ".Wins"));
        }  else {
            SurvivalGames.playerKills.put(p.getName(), 0);
            SurvivalGames.playerWins.put(p.getName(), 0);
        }
        playerBoardSetter(p);
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
    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        SurvivalGames.playerBoards.remove(p.getName());
        SurvivalGames.playStats.set("Players." + p.getName() + ".Kills", SurvivalGames.playerKills.get(p.getName()));
        SurvivalGames.playStats.set("Players." + p.getName() + ".Wins", SurvivalGames.playerWins.get(p.getName()));
        try {
            SurvivalGames.playStats.save(new File("plugins/SurvivalGames/PlayerStats/Players.yml"));
        } catch (IOException i){
            i.printStackTrace();
        }
        SurvivalGames.playerWins.remove(p.getName());
        SurvivalGames.playerKills.remove(p.getName());
    }
    @EventHandler
    public void playerKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        SurvivalGames.playerBoards.remove(p.getName());
        SurvivalGames.playStats.set("Players." + p.getName() + ".Kills", SurvivalGames.playerKills.get(p.getName()));
        SurvivalGames.playStats.set("Players." + p.getName() + ".Wins", SurvivalGames.playerWins.get(p.getName()));
        try {
            SurvivalGames.playStats.save(new File("plugins/SurvivalGames/PlayerStats/Players.yml"));
        } catch (IOException i){
            i.printStackTrace();
        }
        SurvivalGames.playerWins.remove(p.getName());
        SurvivalGames.playerKills.remove(p.getName());
    }
    public static void playerBoardSetter(Player p){
        if (SurvivalGames.playerBoards.containsKey(p.getName())){
            SurvivalGames.playerBoards.remove(p.getName());
        }
        Scoreboard board = SurvivalGames.manager.getNewScoreboard();
        Team team = board.registerNewTeam(p.getName());
        team.addPlayer(p);
        Objective obj = board.registerNewObjective("SG Stats", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.DARK_GREEN + "S.G. Stats");
        Score kills = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + "Kills:"));
        kills.setScore(SurvivalGames.playerKills.get(p.getName()));
        Score wins = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + "Wins:"));
        wins.setScore(SurvivalGames.playerWins.get(p.getName()));
        if (SurvivalGames.econOn){
            Score coins = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + "Coins:"));
            coins.setScore((int)SurvivalGames.econ.getBalance(p.getName()));
        }
        p.setScoreboard(board);
        SurvivalGames.playerBoards.put(p.getName(), board);
    }
}
