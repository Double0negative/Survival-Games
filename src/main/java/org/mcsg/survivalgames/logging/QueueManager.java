package org.mcsg.survivalgames.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.SurvivalGames;




public class QueueManager {

	private static QueueManager instance = new QueueManager();
	private ConcurrentHashMap<Integer, ArrayList<BlockData>> queue = new ConcurrentHashMap<Integer, ArrayList<BlockData>>();
	File baseDir;

	private QueueManager(){

	}

	public static QueueManager getInstance(){
		return instance;
	}

	public void setup(){
		baseDir = new File(SurvivalGames.getPluginDataFolder()+"/ArenaData/");
		try{
			if(!baseDir.exists()){
				baseDir.mkdirs();
			}
			for(Game g : GameManager.getInstance().getGames()){
				ensureFile(g.getID());
			}

		}catch(Exception e){}

		Bukkit.getScheduler().runTaskTimerAsynchronously(GameManager.getInstance().getPlugin(), new DataDumper(), 100, 100);
	}



	public void rollback(final int id, final boolean shutdown){
		loadSave(id);
		if(!shutdown){
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(),
				new Rollback(id, shutdown));
		}
		else{
			new Rollback(id, shutdown).run();
		}
		ArrayList<Entity>removelist = new ArrayList<Entity>();

		for(Entity e:SettingsManager.getGameWorld(id).getEntities()){
			if((!(e instanceof Player)) && (!(e instanceof HumanEntity))){
				if(GameManager.getInstance().getBlockGameId(e.getLocation()) == id){
					removelist.add(e);
				}
			}
		}
		for(int a = 0; a < removelist.size(); a = 0){
			try{removelist.remove(0).remove();}catch(Exception e){}
		}

	}



	public void add(BlockData data){
		ArrayList<BlockData>dat = queue.get(data.getGameId());
		if(dat == null){
			dat = new ArrayList<BlockData>();
			ensureFile(data.getGameId());
		}
		dat.add(data);
		queue.put(data.getGameId(), dat);

	}

	public void ensureFile(int id){
		try{
			File f2 = new File(baseDir, "Arena"+id+".dat");
			if(!f2.exists()){
				f2.createNewFile();
			}
		}catch(Exception e){}
	}

	class DataDumper implements Runnable{
		public void run(){
			for(int id: queue.keySet()){
				try{

					ArrayList<BlockData>data = queue.get(id);
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(baseDir, "Arena"+id+".dat")));

					out.writeObject(data);
					out.flush();
					out.close();

				}catch(Exception e){}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void loadSave(int id){
		ensureFile(id);
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(baseDir, "Arena"+id+".dat")));

			ArrayList<BlockData>input = (ArrayList<BlockData>) in.readObject();

			ArrayList<BlockData>data = queue.get(id); 
			if(data == null){
				data = new ArrayList<BlockData>();
			}

			for(BlockData d:input){
				if(!data.contains(d)){
					data.add(d);
				}
			}

			queue.put(id, data);
			in.close();
		}catch(Exception e){}
	}






	class Rollback implements Runnable{

		int id;
		Game game;
		boolean shutdown;

		public Rollback(int id, boolean shutdown){
			this.id = id;
			game = GameManager.getInstance().getGame(id);
			this.shutdown = shutdown;
		}



		public void run(){

			ArrayList<BlockData>data = queue.get(id);
			if(data != null){
				int a = data.size()-1;
				int rb = 0;
				while(a>=0 && (rb < 100 || shutdown)){
					SurvivalGames.debug("Reseting "+a);
					BlockData result = data.get(a);
					if(result.getGameId() == game.getID()){

						data.remove(a);
						Location l = new Location(Bukkit.getWorld(result.getWorld()), result.getX(), result.getY(), result.getZ());
						Block b = l.getBlock();
						b.setTypeIdAndData(result.getPrevid(), result.getPrevdata(), false);
						b.getState().update();

						/*	if(result.getItems() != null){
							Chest c = (Chest)b;
							c.getBlockInventory().setContents(result.getItems());
						}
						 */

						rb++;

					}
					a--;
				}

				if(a != -1){
					Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(),
							new Rollback(id, shutdown), 1);
				}
				else{
					SurvivalGames.$ ("Arena "+id+" reset. ");
					game.resetCallback();
				}
			}else{
				SurvivalGames.$ (" Arena "+id+" reset. ");
				game.resetCallback();
			}
		}


	}
}


