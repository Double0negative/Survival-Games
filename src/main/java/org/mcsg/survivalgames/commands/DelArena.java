package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.LobbyManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;
import org.mcsg.survivalgames.MessageManager.PrefixType;

public class DelArena implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (!player.hasPermission(permission()) && !player.isOp()) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.nopermission", player);
            return true;
        }

        if (args.length != 1) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input", player, "message-Please specify an arena");
            return true;
        }

        FileConfiguration s = SettingsManager.getInstance().getSystemConfig();
        //FileConfiguration spawn = SettingsManager.getInstance().getSpawns();
        int arena = Integer.parseInt(args[0]);
        Game g = GameManager.getInstance().getGame(arena);

        if (g == null) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input", player, "message-Invalid arena!");
            return true;
        }

        g.disable();
        s.set("sg-system.arenas." + arena + ".enabled", false);
        s.set("sg-system.arenano", s.getInt("sg-system.arenano") - 1);
        //spawn.set("spawns."+arena, null);
        player.sendMessage(ChatColor.GREEN + "Arena deleted");
        SettingsManager.getInstance().saveSystemConfig();
        GameManager.getInstance().hotRemoveArena(arena);
        LobbyManager.getInstance().clearAllSigns();
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg delarena <id> - Delete an arena";
    }

    @Override
    public String permission() {
        return "sg.admin.deletearena";
    }
}