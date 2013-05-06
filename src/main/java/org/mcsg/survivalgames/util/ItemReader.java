package org.mcsg.survivalgames.util;

import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mcsg.survivalgames.SurvivalGames;

public class ItemReader {

	
	private static HashMap<String, Enchantment>encids;
	

	
	private static void loadIds(){
		
		encids =  new HashMap<String, Enchantment>();
		
		for(Enchantment e:Enchantment.values()){
			encids.put(e.toString().toLowerCase().replace("_", ""), e);
		}
		
		
		encids.put("sharpness", Enchantment.DAMAGE_ALL);
		encids.put("dmg", Enchantment.DAMAGE_ALL);
		encids.put("fire", Enchantment.FIRE_ASPECT);

	}
	
	
	
	public static ItemStack read(String str){
		if(encids == null){
			loadIds();
		}
		String split[] = str.split(",");
		SurvivalGames.debug("ItemReader: reading : "+Arrays.toString(split));
		for(int a = 0; a < split.length; a++){
			split[a] = split[a].toLowerCase().trim();
		}
		if(split.length < 1){
			return null;
		}else if(split.length == 1){
			return new ItemStack(Integer.parseInt(split[0]));
		}else if(split.length == 2){
			return new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}else if(split.length == 3){
			return new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Short.parseShort(split[2]));
		}else{
			ItemStack i =  new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Short.parseShort(split[2]));
			String encs[] = split[3].split(" ");
			for(String enc: encs){
				System.out.println(enc);
				String e[] = enc.split(":");
				i.addUnsafeEnchantment(encids.get(e[0]), Integer.parseInt(e[1]));
			}
			if(split.length == 5){
				ItemMeta im = i.getItemMeta();
				im.setDisplayName(MessageUtil.replaceColors(split[4]));
				i.setItemMeta(im);
			}
			return i;
		}
	}
	
	
	
}
