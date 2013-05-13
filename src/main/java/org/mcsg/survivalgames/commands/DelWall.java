package org.mcsg.survivalgames.commands;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.LobbyManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class DelWall implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (!player.hasPermission(permission()) && !player.isOp()) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.nopermission", player);
            return true;
        }
        FileConfiguration scfg = SettingsManager.getInstance().getSystemConfig();
        if (!scfg.getBoolean("sg-system.lobby.sign.set", false)) {
            return true;
        }
        WorldEditPlugin we = GameManager.getInstance().getWorldEdit();
        Selection sel = we.getSelection(player);
        if (sel == null) {
            player.sendMessage(ChatColor.RED + "You must make a WorldEdit Selection first");
            return true;
        }
        if ((sel.getNativeMaximumPoint().getBlockX() - sel.getNativeMinimumPoint().getBlockX()) != 0 && (sel.getNativeMinimumPoint().getBlockZ() - sel.getNativeMaximumPoint().getBlockZ() != 0)) {
            player.sendMessage(ChatColor.RED + " Must be in a straight line!");
            return true;
        }

        for (int i = 0; i < scfg.getInt("sg-system.lobby.signno", 0) + 1; i++) {
            Vector max = sel.getNativeMaximumPoint();
            Vector min = sel.getNativeMinimumPoint();
            String path = "sg-system.lobby.signs." + i + ".";
            if (scfg.getInt(path + "x1", 0) == max.getBlockX()
                    && scfg.getInt(path + "y1", 0) == max.getBlockY()
                    && scfg.getInt(path + "z1", 0) == max.getBlockZ()
                    && scfg.getInt(path + "x2", 0) == min.getBlockX()
                    && scfg.getInt(path + "y2", 0) == min.getBlockY()
                    && scfg.getInt(path + "z2", 0) == min.getBlockZ()) {
                scfg.set(path + "enabled", false);
                return true;
            }
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg addwall <id> - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.delwall", "Add a lobby stats wall for Arena <id>");
    }

    @Override
    public String permission() {
        return "sg.admin.delwall";
    }
}
