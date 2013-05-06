package org.mcsg.survivalgames;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class LobbyManagerOld implements Listener {

    //TODO: Possibly clean up
    Sign[][] signs;
    SurvivalGames p;
    private static LobbyManagerOld instance = new LobbyManagerOld();
    public HashSet < Chunk > lobbychunks = new HashSet < Chunk > ();

    private LobbyManagerOld() {

    }

    public static LobbyManagerOld getInstance() {
        return instance;

    }


    public void setup(SurvivalGames p) {
        this.p = p;
        loadSigns();
    }

    public void loadSigns() {

        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        try {
            if (!c.getBoolean("sg-system.lobby.sign.set")) return;
        } catch (Exception e) {
            return;
        }
        boolean usingx = false;
        int hdiff = 0;
        int x1 = c.getInt("sg-system.lobby.sign.x1");
        int y1 = c.getInt("sg-system.lobby.sign.y1");
        int z1 = c.getInt("sg-system.lobby.sign.z1");
        int x2 = c.getInt("sg-system.lobby.sign.x2");
        int y2 = c.getInt("sg-system.lobby.sign.y2");
        int z2 = c.getInt("sg-system.lobby.sign.z2");
        int inc = 0;
        Location l;
        //  System.out.println(x1+"  "+y1+"  "+z1);
        byte temp = ((Sign) new Location(p.getServer().getWorld(c.getString("sg-system.lobby.sign.world")), x1, y1, z1).getBlock().getState()).getData().getData();
        //  System.out.println("facing "+temp);
        if (temp == 3 || temp == 4) {
            l = new Location(Bukkit.getWorld(c.getString("sg-system.lobby.sign.world")), x1, y1, z1);
            inc = -1;
        } else {
            l = new Location(Bukkit.getWorld(c.getString("sg-system.lobby.sign.world")), x2, y1, z2);
            inc = 1;
        }


        usingx = ((x2 - x1) != 0) ? true : false;
        if (usingx) {
            hdiff = (x1 - x2) + 1;
        } else {
            hdiff = (z1 - z2) + 1;
        }
        int vdiff = (y1 - y2) + 1;


        System.out.println(vdiff + "              " + hdiff);
        signs = new Sign[vdiff][hdiff];
        for (int y = vdiff - 1; y >= 0; y--) {
            for (int x = hdiff - 1; x >= 0; x--) {


                BlockState b = p.getServer().getWorld(SettingsManager.getInstance().getSystemConfig().getString("sg-system.lobby.sign.world")).getBlockAt(l).getState();
                lobbychunks.add(b.getChunk());
                if (b instanceof Sign) {
                    signs[y][x] = (Sign) b;
                }
                if (usingx) l = l.add(inc, 0, 0);
                else l = l.add(0, 0, inc);
                //l.getBlock().setTypeId(323);
            }
            l = l.add(0, - 1, 0);
            if (inc == -1) {
                l.setX(x1);
                l.setZ(z1);
            } else {
                l.setX(x2);
                l.setZ(z2);
            }
        }
        showMessage(new String[] {
            "", "Survival Games", "", "Double0negative", "iMalo", "mc-sg.org", ""
        });

        // try{Thread.sleep(4000);}catch(Exception e){}
    }

    public int[] getSignMidPoint() {
        double x = (signs[0].length * 8);
        double y = (signs.length * 2);

        return new int[] {
            (int) x, (int) y
        };
    }



    public void setLobbySignsFromSelection(Player pl) {
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        SettingsManager s = SettingsManager.getInstance();
        if (!c.getBoolean("sg-system.lobby.sign.set", false)) {
            c.set("sg-system.lobby.sign.set", true);
            s.saveSystemConfig();
        }

        WorldEditPlugin we = p.getWorldEdit();
        Selection sel = we.getSelection(pl);
        if (sel == null) {
            pl.sendMessage(ChatColor.RED + "You must make a WorldEdit Selection first");
            return;
        }
        if ((sel.getNativeMaximumPoint().getBlockX() - sel.getNativeMaximumPoint().getBlockX()) != 0 && (sel.getNativeMaximumPoint().getBlockZ() - sel.getNativeMaximumPoint().getBlockZ() != 0)) {
            pl.sendMessage(ChatColor.RED + " Must be in a straight line!");
            return;
        }

        Vector max = sel.getNativeMaximumPoint();
        Vector min = sel.getNativeMinimumPoint();

        c.set("sg-system.lobby.sign.world", pl.getWorld().getName());
        c.set("sg-system.lobby.sign.x1", max.getBlockX());
        c.set("sg-system.lobby.sign.y1", max.getBlockY());
        c.set("sg-system.lobby.sign.z1", max.getBlockZ());
        c.set("sg-system.lobby.sign.x2", min.getBlockX());
        c.set("sg-system.lobby.sign.y2", min.getBlockY());
        c.set("sg-system.lobby.sign.z2", min.getBlockZ());

        pl.sendMessage(ChatColor.GREEN + "Lobby Status wall successfuly created");
        s.saveSystemConfig();
        loadSigns();


    }

    boolean showingMessage = false;
    ArrayList < String[] > messagequeue = new ArrayList < String[] > (3);
    private boolean error;

    public void showMessage(String[] msg9) {
        // new ThreadMessageDisplay(msg9).start();
        signShowMessage(msg9);
    }

    class ThreadMessageDisplay extends Thread {
        String[] message;

        ThreadMessageDisplay(String[] msg) {
            message = msg;
        }

        public void run() {
            signShowMessage(message);
        }
    }

    public void signShowMessage(String[] msg) {
        signShowMessage(msg, 5000);
    }

    int tid = 0;
    public void signShowMessage(String[] msg9, long wait) {

        messagequeue.add(msg9);
        if (showingMessage) return;
        showingMessage = true;
        if (tid != 0) {
            Bukkit.getScheduler().cancelTask(tid);
        }
        /*   for(int y = signs.length-1; y!=-1; y--){
            for(int a = 0; a<4; a++){

                for(int x = 0; x!=signs[0].length; x++){

                    Sign sig = signs[y][x];
                    sig.setLine(a, "==================================================");
                    sig.update();

                }
                try{Thread.sleep(50);}catch(Exception e){}

            }
        }


        for(int y = signs.length-1; y!=-1; y--){
            for(int a = 0; a<4; a++){
                for(int x = 0; x!=signs[0].length; x++){

                    Sign sig = signs[y][x];
                    if((y == signs.length - 1 && a ==0) || y == 0 && a == 3){
                        sig.setLine(a,"===========================================");
                    }
                    else{
                        sig.setLine(a, "");
                        signs[y][0].setLine(a, "|                         ");
                        signs[y][0].update();
                        signs[y][signs[0].length-1].setLine(a, "              |");
                        signs[y][signs[0].length-1].update();
                    }
                    sig.update();

                }
                try{Thread.sleep(50);}catch(Exception e){}

            }
        }*/
        clearSigns();

        for (int c = 0; c < messagequeue.size(); c++) {
            String[] msg = messagequeue.get(c);
            int x = getSignMidPoint()[1] - (msg.length / 2);
            int lineno = x % 3;
            x = x / 4;
            for (int a = msg.length - 1; a > -1; a--) {
                int y = getSignMidPoint()[0] - (msg[a].length() / 2);

                // System.out.println(msg[a]);
                char[] line = msg[a].toCharArray();
                for (int b = 0; b < line.length; b++) {

                    //System.out.println(y/16+"    "+x/4+"     "+(3-x)%4+"     "+x);
                    Sign sig = signs[x][((y) / 16)];
                    sig.setLine(lineno, sig.getLine(lineno) + line[b]);
                    //System.out.println(sig.getLine(x%4));
                    signs[x][((y) / 16)].update();

                    y++;
                }
                if (lineno == 0) {
                    lineno = 3;
                    x++;
                } else lineno--;


            }
        }
        // try{Thread.sleep(wait);}catch(Exception e){}
        tid = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
            public void run() {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(p, new LobbySignUpdater(), 1L, 20L);
                clearSigns();

            }

        }, 100L);


        messagequeue.clear();

        showingMessage = false;
    }


    class LobbySignUpdater implements Runnable {
        public void run() {
            //this.setName("[SurvivalGames] Lobby signs updater");
            /*int trun = runningThread;

            while(SurvivalGames.isActive() && trun == runningThread){*/
            updateGameStatus();

            /* try{
                    try{Thread.sleep(1000);}catch(Exception e){}
                }catch(Exception e){e.printStackTrace(); signs[0][0].setLine(1, ChatColor.RED+"ERROR");signs[0][0].setLine(1, ChatColor.RED+"Check Console");}*/

        }
    }



    public void updateGameStatus() {
        // clearSigns();
        int b = signs.length - 1;
        if (!SurvivalGames.config_todate) {
            signs[b][0].setLine(0, ChatColor.RED + "CONFIG");
            signs[b][0].setLine(1, ChatColor.RED + "OUTDATED!");
            signs[b][1].setLine(0, ChatColor.RED + "Please reset");
            signs[b][1].setLine(1, ChatColor.RED + "your config");
            signs[b][0].update();
            signs[b][1].update();
            return;
        }
        if (!SurvivalGames.dbcon) {
            signs[b][0].setLine(0, ChatColor.RED + "No Database");
            signs[b][0].update();
            return;
        }
        if (GameManager.getInstance().getGameCount() == 0) {
            signs[b][0].setLine(1, ChatColor.RED + "No Arenas");
            signs[b][0].update();
            return;
        }
        try {
            SettingsManager.getInstance().getLobbySpawn();
        } catch (Exception e) {
            signs[b][0].setLine(1, ChatColor.RED + "No Lobby spawn!");
            signs[b][0].update();
            return;
        }
        if (error) {
            signs[b][0].setLine(1, ChatColor.RED + "Error");
            signs[b][0].update();
            return;

        }
        ArrayList < Game > games = GameManager.getInstance().getGames();
        // System.out.println(games.toString());
        for (int a = 0; a < games.size(); a++) {
            try {
                Game game = games.get(a);
                //System.out.println(game.getMode());
                signs[b][0].setLine(0, "[SurvivalGames]");
                signs[b][0].setLine(1, "Click to join");
                signs[b][0].setLine(2, "Arena " + game.getID());
                signs[b][1].setLine(0, "Arena " + game.getID());
                signs[b][1].setLine(1, game.getMode() + "");
                signs[b][1].setLine(2, game.getActivePlayers() + "/" + ChatColor.GRAY + game.getInactivePlayers() + ChatColor.BLACK + "/" + SettingsManager.getInstance().getSpawnCount(game.getID()));
                if (game.getMode() == Game.GameMode.STARTING) signs[b][1].setLine(3, game.getCountdownTime() + "");
                else if (game.getMode() == Game.GameMode.RESETING || game.getGameMode() == Game.GameMode.FINISHING) {
                    signs[b][2].setLine(3, game.getRBStatus());
                    if (game.getRBPercent() > 100) {
                        signs[b][a].setLine(1, "Saving Queue");
                        signs[b][1].setLine(3, (int) game.getRBPercent() + " left");

                    } else signs[b][1].setLine(3, (int) game.getRBPercent() + "%");
                } else signs[b][1].setLine(3, "");
                signs[b][0].update();
                signs[b][1].update();
                signs[b][2].update();

                int signno = 2;
                int line = 0;
                Player[] active = game.getPlayers()[0];
                Player[] inactive = game.getPlayers()[1];
                for (Player p: active) {
                    if (signno < signs[b].length) {

                        signs[b][signno].setLine(line, (SurvivalGames.auth.contains(p.getName()) ? ChatColor.DARK_BLUE : ChatColor.BLACK) + ((p.getName().equalsIgnoreCase("Double0negative")) ? "Double0" : p.getName()));
                        signs[b][signno].update();

                        line++;
                        if (line == 4) {
                            line = 0;
                            signno++;
                        }
                    }
                }
                for (Player p: inactive) {
                    if (signno < signs[b].length) {
                        signs[b][signno].setLine(line, (SurvivalGames.auth.contains(p.getName()) ? ChatColor.DARK_RED : ChatColor.GRAY) + ((p.getName().equalsIgnoreCase("Double0negative")) ? "Double0" : p.getName()));
                        signs[b][signno].update();
                        line++;
                        if (line == 4) {
                            line = 0;
                            signno++;

                        }
                    }

                }

                b--;
            } catch (Exception e) {
                e.printStackTrace();
                signs[0][0].setLine(1, ChatColor.RED + "ERROR");
                signs[0][0].setLine(1, ChatColor.RED + "Check Console");
            }
        }

    }



    public void clearSigns() {
        try {
            for (int y = signs.length - 1; y != -1; y--) {
                for (int a = 0; a < 4; a++) {

                    for (int x = 0; x != signs[0].length; x++) {

                        Sign sig = signs[y][x];
                        sig.setLine(a, "");
                        sig.update();

                    }

                }
            }
        } catch (Exception e) {}
    }

    public void error(boolean e) {
        error = e;
    }

}