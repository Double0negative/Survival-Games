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

public class SurvivalGames extends JavaPlugin {
	public static Logger logger;
	private static File datafolder;
	private static boolean disabling = false;
	public static boolean dbcon = false;
	public static boolean config_todate = false;
	public static int config_version = 3;

	public static List < String > auth = Arrays.asList(new String[] {
			"Double0negative", "iMalo", "Medic0987", "alex_markey", "skitscape", "AntVenom", "YoshiGenius", "pimpinpsp", "WinryR", "Jazed2011",
			"KiwiPantz", "blackracoon", "CuppingCakes", "4rr0ws", "Fawdz", "Timothy13", "rich91", "ModernPrestige", "Snowpool", "egoshk", 
			"nickm140",  "chaseoes", "Oceangrass", "GrailMore", "iAngelic", "Lexonia", "ChaskyT", "Anon232"
	});

	SurvivalGames p = this;
	public void onDisable() {
		disabling = false;
		PluginDescriptionFile pdfFile = p.getDescription();
		SettingsManager.getInstance().saveSpawns();
		SettingsManager.getInstance().saveSystemConfig();
		for (Game g: GameManager.getInstance().getGames()) {
			try{
				g.disable();
			}catch(Exception e){
				//will throw useless "tried to register task blah blah error." Use the method below to reset the arena without a task.
			}
			QueueManager.getInstance().rollback(g.getID(), true);
		}

		logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " has now been disabled and reset");
	}

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
				if (c.getBoolean("stats.enabled")) DatabaseManager.getInstance().setup(p);
				QueueManager.getInstance().setup();
				StatsManager.getInstance().setup(p, c.getBoolean("stats.enabled"));
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

			for (Player p: Bukkit.getOnlinePlayers()) {
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

	public static void $(String msg){
		logger.log(Level.INFO, msg);
	}

	public static void $(Level l, String msg){
		logger.log(l, msg);
	}

	public static void debug(String msg){
		if(SettingsManager.getInstance().getConfig().getBoolean("debug", false))
			$(msg);
	}

	public static void debug(int a) {
		if(SettingsManager.getInstance().getConfig().getBoolean("debug", false))
			debug(a+"");
	}
}