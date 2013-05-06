package org.mcsg.survivalgames.util;


import java.lang.management.ManagementFactory;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SettingsManager;



public class ArenaDuplicator {

    background background;

/*
 * NOT USED
 * 
 * Experimental Arena duplicator using multiple threads to copy the arena faster
 * 
 * Unfortunately it just crashes once its finished due to bukkit not being thread safe.
 */
    
    
    public void startDupe(Vector v1, Vector v2){

        int factor =  ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        factor = 4;
        int xspan = v2.getBlockX() - v1.getBlockX();
        int maxx = GameManager.getInstance().getGame(1).getArena().getMax().getBlockX();
        for(Game g: GameManager.getInstance().getGames()){
            Location a1 = g.getArena().getMin();
            Location a2 = g.getArena().getMax();

            if(a1.getBlockX()>maxx){
                maxx = a1.getBlockX();
            }
            if(a2.getBlockX()>maxx){
                maxx = a2.getBlockX();
            }
        }

        int divf = xspan / factor;
        background = new background(Math.abs(v2.getBlockX()-v1.getBlockX()) * Math.abs(v2.getBlockY()-v1.getBlockY()) * Math.abs(v1.getBlockZ()-v2.getBlockZ()));
        background.start();
        for(int a = 1; a<=factor; a++){
            System.out.println(xspan);
            int sp1 = divf * a + v1.getBlockX();
            int sp2 = divf * (a+1) + v1.getBlockX();
            int y1  =  v1.getBlockY();
            int y2  =  v2.getBlockY();
            int z1  =  v1.getBlockZ();
            int z2  =  v2.getBlockZ();


            Vector s1 = new Vector((sp1<sp2)?sp1:sp2 , (y1<y2)?y1:y2 , (z1<z2)?z1:z2);
            Vector s2 = new Vector((sp1>sp2)?sp1:sp2 , (y1>y2)?y1:y2 , (z1>z2)?z1:z2);
            System.out.println(s1);
            System.out.println(s2);
            new DupeThread(s1,s2, maxx - v1.getBlockX(), 0, a).start();
        }


    }


    class DupeThread extends Thread{

        World w = SettingsManager.getGameWorld(1);
        Vector min;
        Vector max;
        int xoff;
        int zoff;
        int id;
        public DupeThread(Vector min, Vector max, int xoff, int yoff, int id){
            this.min = min;
            this.max = max;

            this.xoff = xoff;
            this.zoff = yoff;
            this.id = id;

        }

        public void run(){
            Location l1;
            Location l2;
            Bukkit.getServer().broadcastMessage("Starting "+id);
            for(int x = min.getBlockX(); x<max.getBlockX();x++){
                for(int y = min.getBlockY(); y<max.getBlockY();y++){
                    for(int z = min.getBlockZ(); z<max.getBlockZ();z++){
                        l1 = new Location(w,x,y,z);
                        l2 = new Location(w,x+xoff, y, z+zoff);

                        //  System.out.println(l1);
                        //  System.out.println(l2);
                        //if(w.getBlockTypeIdAt(l1) != w.getBlockTypeIdAt(l2)){
                        try{
                            if(l1.getBlock().getTypeId() != l2.getBlock().getTypeId()){

            //                    Chunk c = l2.getChunk();
            //                    net.minecraft.server.Chunk chunk = ((CraftChunk) c).getHandle();

            //                    chunk.a(l2.getBlockX() & 15, l2.getBlockY() , l2.getBlockZ() & 15, l1.getBlock().getTypeId(), l1.getBlock().getData());
                                // w.getBlockAt(l2).setTypeId(w.getBlockTypeIdAt(l1));
                                // w.getBlockAt(l2).setData(w.getBlockAt(l1).getData());

                                background.inc();
                            }
                        }catch(Exception e){e.printStackTrace();}
                        //    w.getBlockAt(l2).getState().update();
                        //    System.out.println(l2);
                        // }
                    }
                }
            }
            Bukkit.getServer().broadcastMessage("Ending "+id);

        }
    }

    class background extends Thread{
        int x = 0;
        int fin = 0;
        int prev = 0;
        public background(int x){
            this.x = x;
        }

        public synchronized void inc(){
            fin++; 
        }

        public void run(){
            while(true){
                System.out.println(fin+"/"+x+" "+((fin - prev) / 2 )+" "+((fin + 0.0)/(x + 0.0))*100);
                prev = fin;
                try{sleep(2000);}catch(Exception e){}
            }
        }
    }
    
}
