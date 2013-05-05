package org.mcsg.survivalgames.stats;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SettingsManager;



public class PlayerStatsSession {


    public Player player;
    public int kills = 0,death = 0,gameno, arenaid, points = 0;
    public int finish = 0;
    long time = 0;
    int ksbon = 0;
    long lastkill = 0;
    int kslevel  = 0;
    int score = 0;
    int position = 0;
    int pppoints = 0;

    private ArrayList<Player>killed = new ArrayList<Player>();


    private HashMap<Integer, Integer>kslist = new HashMap<Integer, Integer>();







    public PlayerStatsSession(Player p, int arenaid ){
        this.player = p;
        this.arenaid = arenaid;


        kslist.put(1, 0);
        kslist.put(2, 0);
        kslist.put(3, 0);
        kslist.put(4, 0);
        kslist.put(5, 0);


    }

    public void setGameID(int gameid){
        this.gameno = gameid;
    }


    public int addKill(Player p){
        killed.add(p);
        kills++;
        checkKS();
        lastkill = new Date().getTime();
        return kslevel;
    }

    public void win(long time){
        position = 1;
        this.time  = time;
    }

    public void died(int pos, long time){
        this.time = time;
        death=1;
        position = pos;
        pppoints = GameManager.getInstance().getGame(arenaid).getInactivePlayers();
    }

    public void setTime(long time){
        this.time = time;
    }

    public void addkillStreak(int ks){

        ksbon = ksbon + ( SettingsManager.getInstance().getConfig().getInt("stats.points.killstreak.base") * (SettingsManager.getInstance().getConfig().getInt("stats.points.killstreak.multiplier") + ks));
        int level = ks;
        if(level>5)level = 5;
        kslist.put(level, kslist.get(level)+1);
        if(level < 4){
            for(Player p: GameManager.getInstance().getGame(arenaid).getAllPlayers()){
                p.sendMessage(SettingsManager.getInstance().getConfig().getString("stats.killstreak.level"+level).replace("{player}", player.getName()).replaceAll("(&([a-fk-or0-9]))", "\u00A7$2"));
            }
        }
        else{
            Bukkit.getServer().broadcastMessage(SettingsManager.getInstance().getConfig().getString("stats.killstreak.level"+level).replace("{player}", player.getName()).replaceAll("(&([a-fk-or0-9]))", "\u00A7$2"));
        }
        lastkill = new Date().getTime();


    }



    public void calcPoints(){
        FileConfiguration c = SettingsManager.getInstance().getConfig();
        int kpoints = kills * c.getInt("stats.points.kill");
        int ppoints = pppoints * c.getInt("stats.points.position");
        int kspoints = ksbon;

        points = kpoints + ppoints + kspoints + ksbon;
        //System.out.println(player+"  "+kpoints +" "+ppoints+" "+kspoints);

        if(position == 1){
            points = points + c.getInt("stats.points.win");
        }

    }

    public boolean checkKS(){
        if(15000 > new Date().getTime() - lastkill){
            kslevel++;
            addkillStreak(kslevel);

            return true;
        }

        kslevel = 0;
        return false;
    }


    public String createQuery(){
        calcPoints();
        String query= "INSERT INTO "+SettingsManager.getSqlPrefix()+"playerstats VALUES(NULL,";
        query = query + gameno+","+/*SettingsManager.getInstance().getConfig().getString("sql.server-prefix")+*/arenaid+",'"+player.getName()+"',"+points+","+position+","+kills+","+death+",";
        String killeds = "'";
        for(Player p:killed){
            killeds = killeds + ((killeds.length()>2)?":":"")+p.getName();
        }
        // killeds = killeds.replaceFirst(":", "");
        query = query + killeds +"',"+time;
        query = query + ","+kslist.get(1)+ ","+kslist.get(2)+ ","+kslist.get(3)+ ","+kslist.get(4)+ ","+kslist.get(5)+")";


       // System.out.println(query);

        return query;

    }

}
