package org.mcsg.survivalgames.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.SettingsManager;

public class DelHead implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        if (!player.hasPermission(permission()) && !player.isOp()) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.nopermission", player);
            return true;
        }
        if (args.length != 0) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.input", player, "message-" + SettingsManager.getInstance().getMessageConfig().getString("messages.error.argumentsnotneeded", "§cArguments not needed!"));
            return true;
        }
        Block bl = player.getTargetBlock(null, 0);
        if (bl != null && bl.getType() == Material.SKULL) {
            if (SettingsManager.getInstance().modifyList("head", bl.getLocation(), 0)) {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.INFO, "info.success", player, "command-'delhead'");
            } else {
                MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "info.unsuccess", player, "command-'delhead'");
            }
        } else {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.wrongtarget", player, "type-head");
            return true;
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg delsign - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.delhead", "Stops the head from updating the player skin");
    }

    @Override
    public String permission() {
        return "sg.admin.delhead";
    }
}
