package org.mcsg.survivalgames.commands;

import org.bukkit.entity.Player;
import org.mcsg.survivalgames.GameManager;



public class LeaveQueue implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {
        GameManager.getInstance().removeFromOtherQueues(player, -1);
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg lq - Leave the queue for any queued games";
    }

	@Override
	public String permission() {
		return null;
	}

}
