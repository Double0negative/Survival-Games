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
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.notspecified", player, "input-" + SettingsManager.getInstance().getMessageConfig().getString("messages.words.position", "Position"));
            return true;
        }
        int pos = 0;
        try {
            pos = Integer.parseInt(args[0]);
            if (pos <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.notanumber", player, "input-" + args[1]);
            return true;
        }
        Block bl = player.getTargetBlock(null, 0);
        if (bl != null && (bl.getType() == Material.WALL_SIGN || bl.getType() == Material.SIGN_POST)) {
            if (SettingsManager.getInstance().modifyList("sign", bl.getLocation(), pos)) {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "info.success", player, "command-'addsign'");
            } else {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "info.unsuccess", player, "command-'addsign'");
            }
        } else {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.wrongtarget", player, "type-sign");
            return true;
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg addsign <pos> - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.addsign", "The sign you are looking at will show stats of player on <pos> position");
    }

    @Override
    public String permission() {
        return "sg.admin.addsign";
    }
}
