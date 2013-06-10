package org.mcsg.survivalgames;


import java.util.ArrayList;
import org.bukkit.entity.Player;


public class ECCEndgame
{

	public static void killPlayer(Game game, Player p)
	{
		int count = 0;
		ArrayList<Player> players = game.getAllPlayers();
		for (Player player : players)
		{
			count++;
			if (game.isPlayerActive(player))
			{
				player.setFoodLevel(game.getConfig().getInt("endgame.deathmatch.foodlevel"));
				player.setSaturation(0);
				player.teleport(SettingsManager.getInstance().getSpawnPoint(game.getID(), count));
			}
		}
	}

}
