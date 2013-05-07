package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.LobbyManager;
import org.mcsg.survivalgames.MessageManager;

public class SetLobbyWall implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        player.sendMessage(ChatColor.RED + "This command has been replaced by /sg addwall <arenaid>");
        if (!player.hasPermission(permission()) && !player.isOp()) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.nopermission", player);
            return true;
        } else if (args.length < 1) {
            player.sendMessage("Please Specify a gameid");
            return true;
        }
        LobbyManager.getInstance().setLobbySignsFromSelection(player, Integer.parseInt(args[0]));
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg addwall <id> - Add a lobby stats wall for Arena <id>";
    }

    @Override
    public String permission() {
        return "sg.admin.setlobby";
    }
    //TODO: TAKE A W.E SELECTIONA AND SET THE LOBBY. ALSO SET LOBBY WALL
}