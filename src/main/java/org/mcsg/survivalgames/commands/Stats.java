package org.mcsg.survivalgames.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.util.DatabaseManager;

public class Stats implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        try {
            ResultSet rs;
            PreparedStatement st = DatabaseManager.getInstance().createStatement("SELECT player, kills, death, points, ks1, ks2, ks3, ks4, ks5 FROM " + SettingsManager.getSqlPrefix() + "playerstats WHERE player='" + player.getName() + "' ;");
            rs = st.executeQuery();
            int kills = 0, death = 0, points = 0, ks1 = 0, ks2 = 0, ks3 = 0, ks4 = 0, ks5 = 0;
            while (rs.next()) {
                kills += rs.getInt("kills");
                death += rs.getInt("death");
                points += rs.getInt("points");
                ks1 += rs.getInt("ks1");
                ks2 += rs.getInt("ks2");
                ks3 += rs.getInt("ks3");
                ks4 += rs.getInt("ks4");
                ks5 += rs.getInt("ks5");
            }
            String prefix = SettingsManager.getInstance().getMessageConfig().getString("prefix.main").replace("&", "§");
            player.sendMessage(new String[]{
                        prefix + " §9Your SurvivalGames stats so far",
                        prefix + " §9" + SettingsManager.getInstance().getMessageConfig().getString("messages.words.points") + "§7: §e" + points,
                        prefix + " §a" + SettingsManager.getInstance().getMessageConfig().getString("messages.words.kills") + "§7/§c" + SettingsManager.getInstance().getMessageConfig().getString("messages.words.deaths") + " §7: §a" + kills + "§7/§c" + death + "§7§l(§e" + (death == 0 ? 0 : kills / death) + "§7§l)",
                        prefix + " §9" + SettingsManager.getInstance().getMessageConfig().getString("messages.words.killstreaks") + "§7: §1" + ks1 + "§7/§2" + ks2 + "§7/§a" + ks3 + "§7/§b" + ks4 + "§7/§c" + ks5
                    });
            return true;
        } catch (SQLException ex) {
            MessageManager.getInstance().logMessage(MessageManager.PrefixType.WARNING, ex.getMessage());
            return false;
        }
    }

    public String help(Player p) {
        return "/sg stats - Shows you your own stats";
    }

    public String permission() {
        return null;
    }
}
