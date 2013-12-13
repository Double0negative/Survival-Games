package org.mcsg.survivalgames.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.mcsg.survivalgames.SettingsManager;



public class ChestRatioStorage {

	HashMap<Integer,  ArrayList<ItemStack>>lvlstore = new HashMap<Integer, ArrayList<ItemStack>>();
	public static ChestRatioStorage instance = new ChestRatioStorage();
	private int ratio = 2;
	private int maxlevel = 0;

	private ChestRatioStorage(){ }

	public static ChestRatioStorage getInstance(){
		return instance;
	}
	
	public void setup(){

		FileConfiguration conf = SettingsManager.getInstance().getChest();

		for(int clevel = 1; clevel <= 16; clevel++){
			ArrayList<ItemStack> lvl = new ArrayList<ItemStack>();
			List<String>list = conf.getStringList("chest.lvl"+clevel);

			if(!list.isEmpty()){
				for(int b = 0; b<list.size();b++){
					ItemStack i = ItemReader.read(list.get(b));
					lvl.add(i);
				}
				lvlstore.put(clevel, lvl);
			} else {
				maxlevel = clevel-1;
				break;
			}
		}
		
		ratio = conf.getInt("chest.ratio", ratio);
		
	}
	
	public int getLevel(int base){
		Random rand = new Random();
		int max = Math.min(base + 5, maxlevel);
		while(rand.nextInt(ratio) == 0 && base < max){
			base++;
		}
		return base;
	}
	
	public ArrayList<ItemStack> getItems(int level){
		Random r = new Random();
		ArrayList<ItemStack>items = new ArrayList<ItemStack>();

		for(int a = 0; a< r.nextInt(7)+10; a++){
			if(r.nextBoolean() == true){
				while(level<level+5 && level < maxlevel && r.nextInt(ratio) == 1){
					level++;
				}

				ArrayList<ItemStack>lvl = lvlstore.get(level);
				ItemStack item = lvl.get(r.nextInt(lvl.size()));

				items.add(item);

			}

		}
		return items;
	}
}
