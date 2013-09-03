package org.mcsg.survivalgames.commands;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.LobbyManager;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;



public class AddWall implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(!player.hasPermission(permission()) && !player.isOp()){
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.nopermission", player);
            return true;
        } else if (args.length < 1){
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.notspecified", player, "input-Arena");
        	return true;
        }
        int arena;
        try {
        	arena = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
        	MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.notanumber", player, "input-Arena");
        	return true;
        }
        LobbyManager.getInstance().setLobbySignsFromSelection(player, arena);
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg addwall <id> - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.addwall", "Add a lobby stats wall for Arena <id>");
    }

    @Override
    public String permission() {
        return "sg.admin.addwall";
    }

    //TODO: TAKE A W.E SELECTION AND SET THE LOBBY. ALSO SET LOBBY WALL
}
