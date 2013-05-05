package org.mcsg.survivalgames.stats;

public class StatsDataManager {

	private static StatsDataManager instance = new StatsDataManager();
	
	private StatsDataManager(){
		
	}
	
	
	
	
	
	public static StatsDataManager getInstance(){
		return instance;
	}
	
	public void setup(){
		
		
	}
	
	
	
	
	
	class PlayerData{
		
		public String getName() {
			return name;
		}

		public int getPos() {
			return pos;
		}

		public int getScore() {
			return score;
		}

		private String name;
		private int pos;
		private int score;
		
		public PlayerData(String name, int pos, int score){
			this.name = name;
			this.pos = pos;
			this.score = score;
		}
		
		
	}
	
	
	
	
}
