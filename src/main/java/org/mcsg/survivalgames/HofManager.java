package org.mcsg.survivalgames;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.mcsg.survivalgames.util.DatabaseManager;

public class HofManager {

    private static HofManager instance = new HofManager();

    private void HofManager() {
    }

    public static HofManager getInstance() {
        return instance;
    }

    public void setup() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(GameManager.getInstance().getPlugin(), new SignUpdater(), 100, 100);
    }

    public static Map<String, Integer[]> sortByValue(Map<String, Integer[]> map) {
        List<Map.Entry<String, Integer[]>> list = new LinkedList<Map.Entry<String, Integer[]>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer[]>>() {
            public int compare(Map.Entry<String, Integer[]> m1, Map.Entry<String, Integer[]> m2) {
                return (m2.getValue()[2]).compareTo(m1.getValue()[2]);
            }
        });
        Map<String, Integer[]> result = new LinkedHashMap<String, Integer[]>();
        for (Map.Entry<String, Integer[]> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public class SignUpdater implements Runnable {

        public void run() {
            ResultSet rs;
            try {
                PreparedStatement st = DatabaseManager.getInstance().createStatement("SELECT player, kills, death, points FROM " + SettingsManager.getSqlPrefix() + "playerstats;");
                rs = st.executeQuery();
                Map<String, Integer[]> stats = new HashMap<String, Integer[]>();
                while (rs.next()) {
                    if (stats.containsKey(rs.getString("player"))) {
                        stats.put(rs.getString("player"), new Integer[]{stats.get(rs.getString("player"))[0] + rs.getInt("kills"), stats.get(rs.getString("player"))[1] + rs.getInt("death"), stats.get(rs.getString("player"))[2] + rs.getInt("points")});
                    } else {
                        stats.put(rs.getString("player"), new Integer[]{rs.getInt("kills"), rs.getInt("death"), rs.getInt("points")});
                    }
                }
                if (stats.isEmpty()) {
                    return;
                }
                FileConfiguration cfg = SettingsManager.getInstance().getConfig();
                stats = sortByValue(stats);
                for (String str : cfg.getStringList("head")) {
                    World w = Bukkit.getServer().getWorld(str.split(";")[0]);
                    if (w == null) {
                        continue;
                    }
                    Integer pos = Integer.decode(str.split(";")[4]);
                    if (stats.keySet().toArray().length < pos) {
                        continue;
                    }
                    double x = Double.parseDouble(str.split(";")[1]);
                    double y = Double.parseDouble(str.split(";")[2]);
                    double z = Double.parseDouble(str.split(";")[3]);
                    Block bl = w.getBlockAt(new Location(w, x, y, z));
                    if (bl == null) {
                        continue;
                    }
                    Skull s = (Skull) bl.getState();
                    s.setOwner(stats.keySet().toArray()[pos - 1].toString());
                    s.update();
                }
                for (String str : cfg.getStringList("sign")) {
                    World w = Bukkit.getServer().getWorld(str.split(";")[0]);
                    if (w == null) {
                        continue;
                    }
                    Integer pos = Integer.decode(str.split(";")[4]);
                    if (stats.keySet().toArray().length < pos) {
                        continue;
                    }
                    double x = Double.parseDouble(str.split(";")[1]);
                    double y = Double.parseDouble(str.split(";")[2]);
                    double z = Double.parseDouble(str.split(";")[3]);
                    Block bl = w.getBlockAt(new Location(w, x, y, z));
                    if (bl == null) {
                        continue;
                    }
                    Sign s = (Sign) bl.getState();
                    String playerName = stats.keySet().toArray()[pos - 1].toString();
                    s.setLine(0, "Position: " + pos);
                    s.setLine(1, playerName);
                    s.setLine(2, "K/D : " + stats.get(playerName)[0] + "/" + stats.get(playerName)[1]);
                    s.setLine(3, "Points: " + stats.get(playerName)[2]);
                    s.update();
                }
            } catch (Exception ex) {
                Logger.getLogger(HofManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
