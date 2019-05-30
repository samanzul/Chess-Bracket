import java.util.ArrayList;
/*
 * Author:Saul Manzano
 * File Name: Player.java
 * Purpose: This a class that is used in Bracket.java so each player read in from the file can have all their data
 * stored in a object to simply things later
 */
public class Player {
	String name;
	String[] stats;
	double wP;
	double wins;
	double gamesPlayed;
	public Player(String name, String[] strings) {
		this.name = name;
		this.stats = strings;
		this.wP = winPercent(stats);
	}
	public double winPercent(String[] stats2) {
		
		for(int i=0;i<stats2.length;i++) {
			String[] nums = stats2[i].split(",");
			wins += Double.parseDouble(nums[0]);
			gamesPlayed += Double.parseDouble(nums[1]);
			
		}
		return wins/gamesPlayed;
	}
	public double getPercent() {
		return wP;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name+" "+wP;
	}

}
