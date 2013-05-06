package org.mcsg.survivalgames.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.mcsg.survivalgames.SettingsManager;



public class ChestRatioStorage {



	HashMap<Integer, ArrayList<ItemStack>>lvlstore = new HashMap<Integer, ArrayList<ItemStack>>();
	public static ChestRatioStorage instance = new ChestRatioStorage();
	int ratio = 2;

	private ChestRatioStorage(){

	}

	public void setup(){

		FileConfiguration conf = SettingsManager.getInstance().getChest();

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
