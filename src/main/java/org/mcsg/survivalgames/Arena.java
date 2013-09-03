package org.mcsg.survivalgames;

import org.bukkit.Location;

/**
 * Represents a hunger games arena.
 * 
 * @author Double0negative
 */
public class Arena {

	private final Location min;
	private final Location max;

	/**
	 * Creates a new Arena.
	 * 
	 * @param min
	 *            The minimum point in the Arena.
	 * @param max
	 *            The maximum point in the Arena.
	 */
	public Arena(Location min, Location max) {
		this.max = max;
		this.min = min;
	}

	public boolean containsBlock(Location location) {
		if (location.getWorld() != min.getWorld()) {
			return false;
		}
		final double x = location.getX();
		final double y = location.getY();
		final double z = location.getZ();
		return x >= min.getBlockX() && x < max.getBlockX() + 1
				&& y >= min.getBlockY() && y < max.getBlockY() + 1
				&& z >= min.getBlockZ() && z < max.getBlockZ() + 1;
	}

	/**
	 * Gets the maximum point in the Arena.
	 * 
	 * @return The maximum point.
	 */
	public Location getMax() {
		return max;
	}

	/**
	 * Gets the minimum point in the Arena.
	 * 
	 * @return The minimum point.
	 */
	public Location getMin() {
		return min;
	}

}