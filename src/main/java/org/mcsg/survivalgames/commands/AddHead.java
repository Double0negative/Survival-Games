package org.mcsg.survivalgames.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class AddHead implements SubCommand {

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
        if (bl != null && bl.getType() == Material.SKULL) {
            if (SettingsManager.getInstance().modifyList("head", bl.getLocation(), pos)) {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "info.success", player, "command-'addhead'");
            } else {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "info.unsuccess", player, "command-'addhead'");
            }
        } else {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.wrongtarget", player, "type-head");
            return true;
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg addhead <pos> - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.addhead", "The head you are looking at will show skin of player on position <pos>");
    }

    @Override
    public String permission() {
        return "sg.admin.addhead";
    }
}
