package org.mcsg.survivalgames.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mcsg.survivalgames.SurvivalGames;

public class ItemReader {

	
	private static HashMap<String, Enchantment>encids;
	

	
	private static void loadIds(){
		
		encids =  new HashMap<String, Enchantment>();

		for(Enchantment e:Enchantment.values()){
			encids.put(e.getName().toLowerCase().replace("_", ""), e);
                        encids.put(e.getName().toLowerCase(), e);
                        encids.put(e.getName(), e);
		}
				
		encids.put("sharpness", Enchantment.DAMAGE_ALL);
		encids.put("dmg", Enchantment.DAMAGE_ALL);
		encids.put("fire", Enchantment.FIRE_ASPECT);
                /* eventually friendly names for all
                encids.put("", Enchantment.PROTECTION_ENVIRONMENTAL);
                encids.put("", Enchantment.PROTECTION_FIRE);
                encids.put("", Enchantment.PROTECTION_FALL);
                encids.put("", Enchantment.PROTECTION_EXPLOSIONS);
                encids.put("", Enchantment.PROTECTION_PROJECTILE);
                encids.put("", Enchantment.OXYGEN);
                encids.put("", Enchantment.WATER_WORKER);
                encids.put("", Enchantment.THORNS);
                encids.put("", Enchantment.DAMAGE_ALL);
                encids.put("", Enchantment.DAMAGE_UNDEAD);
                encids.put("", Enchantment.DAMAGE_ARTHROPODS);
                encids.put("", Enchantment.KNOCKBACK);
                encids.put("", Enchantment.FIRE_ASPECT);
                encids.put("", Enchantment.LOOT_BONUS_MOBS);
                encids.put("", Enchantment.DIG_SPEED);
                encids.put("", Enchantment.SILK_TOUCH);
                encids.put("", Enchantment.DURABILITY);
                encids.put("", Enchantment.LOOT_BONUS_BLOCKS);
                encids.put("", Enchantment.ARROW_DAMAGE);
                encids.put("", Enchantment.ARROW_KNOCKBACK);
                encids.put("", Enchantment.ARROW_FIRE);
                encids.put("", Enchantment.ARROW_INFINITE);
                encids.put("", Enchantment.LUCK);
                encids.put("", Enchantment.LURE);
                */
	}
	
	public static ItemStack read(String str){
		if(encids == null){
			loadIds();
		}
                //- 95:3, 3, enchants [enchants ... []], name, lore, lore, lore...
		String split[] = str.split(",",4);
		SurvivalGames.debug("ItemReader: reading : "+Arrays.toString(split));
                
                if(split.length < 1) return null;
                
                int id, qty = 1;
                short dmg;
                
                try{
                    if (split[0].contains(":"))
                    {
                        id = Integer.parseInt(split[0].split(":",2)[0].trim());
                        dmg = Short.parseShort(split[0].split(":",2)[1].trim());
                    }
                    else
                    {
                        id = Integer.parseInt(split[0].trim());
                        dmg = 0;
                    }
                }
                catch(NumberFormatException ex)
                {
                    System.out.println("could not read id and/or damage value from:");
                    System.out.println("    "+str);
                    return null;
                }
                
                if(split.length > 1)
                {
                    try
                    {
                        qty = Integer.parseInt(split[1].trim());
                    }
                    catch(NumberFormatException ex)
                    { /* not really a big deal */ }
                }
                
                ItemStack ret = new ItemStack(id, qty, dmg);
                if(ret == null){
                    System.out.println("invalid item id:"+id+" data:"+dmg);
                    return null;
                }
		if(split.length > 2)
                {
                    String encs[] = split[2].toLowerCase().trim().split(" ");
                    for(String enc: encs){
                        //System.out.println(enc);
                        if(enc.contains(":"))
                        {
                            try{
                                String e[] = enc.split(":",2);
                                if(encids.containsKey(e[0].trim()))
                                {
                                    ret.addUnsafeEnchantment(encids.get(e[0].trim()), Integer.parseInt(e[1].trim()));
                                }else{
                                    System.out.println("unknown enchantment: \""+ e[0].trim() +"\"");
                                }
                            } catch (NumberFormatException ex)
                            {System.out.println("\"" + enc.trim() + "\" is not a properly formatted enchantment.");}
                        }
                        else //assume lvl 0
                        {
                            if(encids.containsKey(enc.trim()))
                            {
                                ret.addUnsafeEnchantment(encids.get(enc.trim()), 0);
                            }else{
                                if(!enc.trim().equals("")) //likely omitted
                                    System.out.println("unknown enchantment: \""+ enc.trim() +"\"");
                            }
                        }
                    }
                }
                if(split.length > 3)
                {
                    ArrayList<String> lore = new ArrayList();
                    String[] ss = split[3].split(",");
                    ItemMeta im = ret.getItemMeta();
                    if(!ss[0].trim().equals(""))
                        im.setDisplayName(MessageUtil.replaceColors(ss[0].trim()));
                    if(ss.length > 1)
                    {
                        for(String line:ss)
                        {
                            lore.add(MessageUtil.replaceColors(line.trim()));
                        }
                        lore.remove(0); // the display name
                        im.setLore(lore);
                    }
                    ret.setItemMeta(im);
		}
                System.out.println(ret.toString());
                return ret;
	}
	
	public static String getFriendlyItemName(Material m){
		String str = m.toString();
		str = str.replace('_',' ');
		str = str.substring(0, 1).toUpperCase() +
				str.substring(1).toLowerCase();
		return str;
	}
	
	
}
