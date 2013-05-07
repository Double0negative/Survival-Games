package org.mcsg.survivalgames.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class AddSign implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        if (!player.hasPermission(permission()) && !player.isOp()) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.nopermission", player);
            return true;
        }
        if (args.length != 1) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.input", player, "message-Please specify a position");
            return true;
        }
        int pos = 0;
        try {
            pos = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.input", player, "message-Position is not a number");
            return true;
        }
        Block bl = player.getTargetBlock(null, 0);
        if (bl != null && (bl.getType() == Material.SIGN || bl.getType() == Material.SIGN_POST)) {
            if (SettingsManager.getInstance().addToList("sign", bl.getLocation(), pos)) {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "info.success", player, "command-'add sign'");
            } else {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "info.unsuccess", player, "command-'add sign'");
            }
        } else {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.wrongtarget", player, "type-sign");
            return true;
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg addsign <pos> - The sign you are lookin at will show stats of player on <pos> position";
    }

    @Override
    public String permission() {
        return "sg.admin.addsign";
    }
}
