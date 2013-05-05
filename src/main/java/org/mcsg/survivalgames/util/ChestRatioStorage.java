package org.mcsg.survivalgames.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.mcsg.survivalgames.SurvivalGames;



public class ChestRatioStorage {



	HashMap<Integer, ArrayList<ItemStack>>lvlstore = new HashMap<Integer, ArrayList<ItemStack>>();
	public static ChestRatioStorage instance = new ChestRatioStorage();
	int ratio = 2;

	private ChestRatioStorage(){

	}

	public void setup(){


		File f = new File(SurvivalGames.getPluginDataFolder()+"/chest.yml");
		if(!f.exists()){
			try {f.createNewFile();
			FileWriter out = new FileWriter(f);
			InputStream is = getClass().getResourceAsStream("chest.yml");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				out.write(line+"\n");
				//  System.out.println(line+"\n");
			}


			is.close();
			isr.close();
			br.close();
			out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

		for(int a = 1; a<5;a++){
			ArrayList<ItemStack> lvl = new ArrayList<ItemStack>();
			List<String>list = conf.getStringList("chest.lvl"+a);

			for(int b = 0; b<list.size();b++){
				ItemStack i = ItemReader.read(list.get(b));
				

				lvl.add(i);

			}

			lvlstore.put(a, lvl);

		}

		ratio = conf.getInt("chest.ratio") + 1;

	}

	public static ChestRatioStorage getInstance(){
		return instance;
	}

	

	public ArrayList<ItemStack> getItems(){
		Random r = new Random();
		ArrayList<ItemStack>items = new ArrayList<ItemStack>();
		for(int a = 0; a< r.nextInt(7)+5; a++){
			if(r.nextBoolean() == true){
				int i = 1;
				while(i<6 &&  r.nextInt(ratio) == 1){
					i++;
				}

				ArrayList<ItemStack>lvl = lvlstore.get(i);
				ItemStack item = lvl.get(r.nextInt(lvl.size()));
				
				items.add(item);
			}

		}

		//Bukkit.broadcastMessage(items+"");
		return items;



	}




}
