package org.mcsg.survivalgames.stats;

public class StatsDataManager {

	class PlayerData {

		private String name;

		private int pos;

		private int score;

		public PlayerData(String name, int pos, int score) {
			this.name = name;
			this.pos = pos;
			this.score = score;
		}

		public String getName() {
			return name;
		}

		public int getPos() {
			return pos;
		}

		public int getScore() {
			return score;
		}

	}

	private static StatsDataManager instance = new StatsDataManager();

	public static StatsDataManager getInstance() {
		return instance;
	}

	private StatsDataManager() {

	}

	public void setup() {

	}

}
