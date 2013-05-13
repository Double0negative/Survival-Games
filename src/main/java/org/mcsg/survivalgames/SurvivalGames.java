package org.mcsg.survivalgames;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.survivalgames.events.*;
import org.mcsg.survivalgames.hooks.HookManager;
import org.mcsg.survivalgames.logging.LoggingManager;
import org.mcsg.survivalgames.logging.QueueManager;
import org.mcsg.survivalgames.stats.StatsManager;
import org.mcsg.survivalgames.util.ChestRatioStorage;
import org.mcsg.survivalgames.util.DatabaseManager;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.scheduler.BukkitScheduler;

public class SurvivalGames extends JavaPlugin {

    public static Logger logger;
    private static File datafolder;
    private static boolean disabling = false;
    public static boolean dbcon = false;
    public static boolean dbcon2 = false;
    public static boolean config_todate = false;
    public static int config_version = 3;
    private static ResultSet rs;
    public static List< String> auth = Arrays.asList(new String[]{
                "Double0negative", "iMalo", "Medic0987", "alex_markey", "skitscape", "AntVenom", "YoshiGenius", "pimpinpsp", "WinryR", "Jazed2011",
                "KiwiPantz", "blackracoon", "CuppingCakes", "4rr0ws", "Fawdz", "Timothy13", "rich91", "ModernPrestige", "Snowpool", "egoshk",
                "nickm140", "chaseoes", "Oceangrass", "GrailMore", "iAngelic", "Lexonia", "ChaskyT", "Anon232", "DragonCz"
            });
    SurvivalGames p = this;

    @Override
    public void onDisable() {
        disabling = false;
        PluginDescriptionFile pdfFile = p.getDescription();
        SettingsManager.getInstance().saveSpawns();
        SettingsManager.getInstance().saveSystemConfig();
        for (Game g : GameManager.getInstance().getGames()) {
            try {
                g.disable();
            } catch (Exception e) {
                //will throw useless "tried to register task blah blah error." Use the method below to reset the arena without a task.
            }
            QueueManager.getInstance().rollback(g.getID(), true);
        }

        logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " has now been disabled and reset");
    }

    @Override
    public void onEnable() {
        logger = p.getLogger();

        //ensure that all worlds are loaded. Fixes some issues with Multiverse loading after this plugin had started
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Startup(), 10);
        try {
            new Metrics(this).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    class Startup implements Runnable {

        public void run() {
            datafolder = p.getDataFolder();

            PluginManager pm = getServer().getPluginManager();
            setCommands();

            SettingsManager.getInstance().setup(p);
            MessageManager.getInstance().setup();
            GameManager.getInstance().setup(p);

            try { // try loading everything that uses SQL. 
                FileConfiguration c = SettingsManager.getInstance().getConfig();
                if (c.getBoolean("stats.enabled")) {
                    DatabaseManager.getInstance().setup(p);
                    dbcon2 = true;
                }
                QueueManager.getInstance().setup();
                StatsManager.getInstance().setup(p, c.getBoolean("stats.enabled"));
                if (dbcon2) {
                    getServer().getScheduler().runTaskTimerAsynchronously(p, new HofUpdater(), 10, 100);
                }
                dbcon = true;
            } catch (Exception e) {
                dbcon = false;
                e.printStackTrace();
                logger.severe("!!!Failed to connect to the database. Please check the settings and try again!!!");
                return;
            } finally {
                LobbyManager.getInstance().setup(p);
            }

            ChestRatioStorage.getInstance().setup();
            HookManager.getInstance().setup();
            pm.registerEvents(new PlaceEvent(), p);
            pm.registerEvents(new BreakEvent(), p);
            pm.registerEvents(new DeathEvent(), p);
            pm.registerEvents(new MoveEvent(), p);
            pm.registerEvents(new CommandCatch(), p);
            pm.registerEvents(new SignClickEvent(), p);
            pm.registerEvents(new ChestReplaceEvent(), p);
            pm.registerEvents(new LogoutEvent(), p);
            pm.registerEvents(new JoinEvent(p), p);
            pm.registerEvents(new TeleportEvent(), p);
            pm.registerEvents(LoggingManager.getInstance(), p);
            pm.registerEvents(new SpectatorEvents(), p);
            pm.registerEvents(new BandageUse(), p);
            pm.registerEvents(new KitEvents(), p);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (GameManager.getInstance().getBlockGameId(p.getLocation()) != -1) {
                    p.teleport(SettingsManager.getInstance().getLobbySpawn());
                }
            }

            //   new Webserver().start();
        }
    }

    public void setCommands() {
        getCommand("survivalgames").setExecutor(new CommandHandler(p));
    }

    public static File getPluginDataFolder() {
        return datafolder;
    }

    public static boolean isDisabling() {
        return disabling;
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit instanceof WorldEditPlugin) {
            return (WorldEditPlugin) worldEdit;
        } else {
            return null;
        }
    }

    public static void $(String msg) {
        $(Level.INFO, msg);
    }

    public static void $(Level l, String msg) {
        logger.log(l, msg);
    }

    public static void debug(String msg) {
        if (SettingsManager.getInstance().getConfig().getBoolean("debug", false)) {
            $(msg);
        }
    }

    public static void debug(int a) {
        if (SettingsManager.getInstance().getConfig().getBoolean("debug", false)) {
            debug(a + "");
        }
    }

    public static Map<String, Integer[]> sortByValue(Map<String, Integer[]> map) {
        List<Map.Entry<String, Integer[]>> list = new LinkedList<Map.Entry<String, Integer[]>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer[]>>() {

            public int compare(Map.Entry<String, Integer[]> m1, Map.Entry<String, Integer[]> m2) {
                return (m2.getValue()[2]).compareTo(m1.getValue()[2]);
            }
        });
        Map<String, Integer[]> result = new LinkedHashMap<String, Integer[]>();
        for (Map.Entry<String, Integer[]> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public void updateHof(ResultSet rs) {
        Map<String, Integer[]> stats = new HashMap<String, Integer[]>();
        try {
            while (rs.next()) {
                if (stats.containsKey(rs.getString("player"))) {
                    stats.put(rs.getString("player"), new Integer[]{
                                stats.get(rs.getString("player"))[0] + rs.getInt("kills"),
                                stats.get(rs.getString("player"))[1] + rs.getInt("death"),
                                stats.get(rs.getString("player"))[2] + rs.getInt("points")
                            });
                } else {
                    stats.put(rs.getString("player"), new Integer[]{rs.getInt("kills"), rs.getInt("death"), rs.getInt("points")});
                }
            }
        } catch (SQLException ex) {
            MessageManager.getInstance().logMessage(MessageManager.PrefixType.WARNING, ex.getMessage());
        }
        FileConfiguration cfg = SettingsManager.getInstance().getSystemConfig();
        stats = sortByValue(stats);
        if (cfg.contains("hof.head")) {
            for (String str : cfg.getStringList("hof.head")) {
                World w = Bukkit.getServer().getWorld(str.split(";")[0]);
                if (w == null) {
                    continue;
                }
                Integer pos = Integer.decode(str.split(";")[4]);
                if (stats.keySet().toArray().length < pos) {
                    continue;
                }
                double x = Double.parseDouble(str.split(";")[1]);
                double y = Double.parseDouble(str.split(";")[2]);
                double z = Double.parseDouble(str.split(";")[3]);
                Block bl = w.getBlockAt(new Location(w, x, y, z));
                if (bl == null) {
                    continue;
                } else if (!(bl.getState() instanceof Skull)) {
                    continue;
                }
                Skull s = (Skull) bl.getState();
                s.setOwner(stats.keySet().toArray()[pos - 1].toString());
                s.update();
            }
        }
        if (cfg.contains("hof.sign")) {
            for (String str : cfg.getStringList("hof.sign")) {
                World w = Bukkit.getServer().getWorld(str.split(";")[0]);
                if (w == null) {
                    continue;
                }
                Integer pos = Integer.decode(str.split(";")[4]);
                if (stats.keySet().toArray().length < pos) {
                    continue;
                }
                double x = Double.parseDouble(str.split(";")[1]);
                double y = Double.parseDouble(str.split(";")[2]);
                double z = Double.parseDouble(str.split(";")[3]);
                Block bl = w.getBlockAt(new Location(w, x, y, z));
                if (bl == null) {
                    continue;
                } else if (!(bl.getState() instanceof Sign)) {
                    continue;
                }
                Sign s = (Sign) bl.getState();
                String playerName = stats.keySet().toArray()[pos - 1].toString();
                s.setLine(0, "Position: " + pos);
                s.setLine(1, playerName);
                s.setLine(2, "K/D : " + stats.get(playerName)[0] + "/" + stats.get(playerName)[1]);
                s.setLine(3, "Points: " + stats.get(playerName)[2]);
                s.update();
            }
        }
    }

    public class HofUpdater implements Runnable {

        public void run() {
            try {
                PreparedStatement st = DatabaseManager.getInstance().createStatement("SELECT player, kills, death, points FROM " + SettingsManager.getSqlPrefix() + "playerstats;");
                ResultSet rs2 = st.executeQuery();
                if (rs2 != rs) {
                    updateHof(rs2);
                    rs = rs2;
                }
            } catch (SQLException ex) {
                MessageManager.getInstance().logMessage(MessageManager.PrefixType.WARNING, ex.getMessage());
            }
        }
    }
}