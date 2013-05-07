package org.mcsg.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.MessageManager;
import org.mcsg.survivalgames.Game.GameMode;
import org.mcsg.survivalgames.MessageManager.PrefixType;
import org.mcsg.survivalgames.GameManager;

public class Enable implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (!player.hasPermission(permission()) && !player.isOp()) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.nopermission", player);
            return true;
        }
        try {
            if (args.length == 0) {
                for (Game g : GameManager.getInstance().getGames()) {
                    if (g.getMode() == GameMode.DISABLED) {
                        g.enable();
                    }
                }
                player.sendMessage(ChatColor.GREEN + "Enabled all arenas");
            } else {
                GameManager.getInstance().enableGame(Integer.parseInt(args[0]));
                player.sendMessage(ChatColor.GREEN + "Arena " + args[0] + " Enabled");
            }
        } catch (NumberFormatException e) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input", player, "message-Game must be a number!");
        } catch (NullPointerException e) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input", player, "message-No game by this ID exist!");
        }
        return true;

    }

    @Override
    public String help(Player p) {
        return "/sg enable <id> - Enables Arena <id>";
    }

    @Override
    public String permission() {
        return "sg.staff.enablearena";
    }
}