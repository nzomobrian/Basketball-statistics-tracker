// Brian Nzomo
// Basketball Statistics Tracker Program
// Player Class
// The Player class keeps track of all the statistic data for a player.

import java.util.*;

public class Player extends Constants implements Comparable<Player> {

	private static final long serialVersionUID = 1L;
	private String name, lastName, displayName;			// Players first and last names
    private boolean isOnFloor;
	private Map<String, Integer> data;					// Map to store players statistical data
    private Map<String, String> statMap;				// Map to map extended statistic names to abbreviations
      
    // Parameters: 
    //		String 'name': The first name of the player
    //      String 'lastName': The last name of the player
    //	    String 'display': The display name of the player. Represented as their first name
    //      followed by their last initial followed by a period. If more than two players have
    //      the same first name and last initial, then the second player with the same first name
    //      and last initial's display name becomes their first name followed by their last name followed
    //      by a period.
    // Post: Initializes the 'data' map to map all statistical abbreviations found in the STATISTIC_ABBREVIATIONS array
    //       to a number.
    //       Initializes the 'statMap' map to map all full length statistic names to their
    //       abbreviations.
    public Player(String name, String lastName, String display) {
    	this.name = name;
    	this.lastName = lastName;
    	this.displayName = display;
    	this.data = new TreeMap<String, Integer>();
    	this.statMap = new TreeMap<String, String>();
    	for (int i = 0; i < STATISTIC_ABBREVIATIONS.length; i++) {
    		this.data.put(STATISTIC_ABBREVIATIONS[i], 0);
    	}
    	this.data.put("OnCourt", -1);
    	for (int i = 0; i < STATISTIC_ABBREVIATIONS.length; i++) {
    		this.statMap.put(STATISTIC_DISPLAY_WORDS[i], STATISTIC_ABBREVIATIONS[i]);
    	}
    	this.statMap.put("OnCourt", "OnCourt");
    }
    
    // Post: Alternate Player Constructor with only the first name given
    public Player(String name) {
    	this(name, "", "");
    }
    
    // Post: Returns true if the two players being compared have the exact same first and last name (case sensitive)
    //       Otherwise returns false
    public boolean equals(Object obj){
    	if (obj == null) {
    		return false;
    	} else if (obj == this) {
    		return true;
    	} else {
    		Player player = (Player) obj;
        	return (player.getName().equals(this.name) && player.getLastName().equals(this.lastName) && player.getDisplayName().equals(this.displayName));
    	}     
    }			
    
    public int compareTo(Player other) {
    	int firstNames = this.name.compareTo(other.getName());
    	if (firstNames == 0) {
    		return this.lastName.compareTo(other.getLastName());
    	} else {
    		return firstNames;
    	}
    }
    
    // Pre:  'statistic' is a key in either the 'data' or 'statMap' map, otherwise an 
    //       IllegalArgumentException is thrown.
    // Post: Increments/Decrements the value associated with the String 'statistic' by the right amount.
    //       (i.e, A 3 point shot will increment the players points by 3).
    //       If 'add' is true then the value is incremented, otherwise it is decremented.
    public void add(boolean add, String statistic) {
    	String stat = mapStat(statistic);
    	int addMultiplier;
    	if (add) {
    		addMultiplier = 1;
    	} else {
    		addMultiplier = -1;
    	}
    	int addOne = 1 * addMultiplier;
    	/*
    	 * FGM, 3PM, 3PA, FTM all involve multiple statistics being incremented
    	 * such as PTS, FGM/FGA.
    	 */
    	switch (stat) {
    	case "Made FG":
    	case "FGM":
    		this.data.put("FGA", this.data.get("FGA") + addOne);
    		this.data.put("PTS", this.data.get("PTS") + (2 * addOne));
    		break;
    	case "Made 3pt FG":
    	case "3PM":
    		this.data.put("FGM", this.data.get("FGM") + addOne);
    		this.data.put("FGA", this.data.get("FGA") + addOne);
    		this.data.put("3PA", this.data.get("3PA") + addOne);
    		this.data.put("PTS", this.data.get("PTS") + (3 * addOne));
    		break;
    	case "Missed 3pt FG":
    	case "3PA":
    		this.data.put("FGA", this.data.get("FGA") + addOne);
    		break;
    	case "Made Free Throw":
    	case "FTM":
    		this.data.put("FTA", this.data.get("FTA") + addOne);
    		this.data.put("PTS", this.data.get("PTS") + addOne);
    		break;
       	}
    	int value = this.data.get(stat) + addOne;
		this.data.put(stat, value);
    }
    
    // Pre:  'statistic' is a key in either the 'data' or 'statMap' map, otherwise an 
    //       IllegalArgumentException is thrown.
    // Post: Sets the value associated with the String 'statistic' to be the Integer value of 'num'.
    //       If 'num' is a Player name, then the players name is set to 'num'. 
    public void setStat(String statistic, String num) {
    	String stat = mapStat(statistic);
    	switch (stat) {
    	case "Player":
    		if (!num.equals("Total")) {
        		num = num.replace("_", " ");
        		Scanner scanner = new Scanner(num);
         		this.name = scanner.next();
         		this.lastName = scanner.next();
         		this.displayName = this.name + " " + this.lastName.charAt(0) + ".";
         		scanner.close();
        	} else {
        		this.name = this.displayName = num;
        		this.lastName = "";
        	}
    		break;
    	case "MIN":
        	this.data.put(stat, Integer.valueOf(num));
        	break;
    	case "FG%":
    	case "3P%":
    	case "FT%":
    		break;
    	case "OnCourt":
    		this.isOnFloor = num.equals("true");
    		break;
    	default:
        	this.data.put(stat, Integer.valueOf(num));
    	}
    }
    
    // Pre:  'statistic' is a key in either the 'data' or 'statMap' map, otherwise an 
    //       IllegalArgumentException is thrown.
    // Post: Returns the String representation of the value associated with the given 'statistic'.
    //       The 'total' parameter only applies if the 'statistic' in question is "Player". In which case,
    //       'total' is used to distinguish a normal player from the abstract Total player used in the
    //       Table class to show the totals of each statistic throughout the team.
    public String getStat(String statistic) {
    	boolean isForTable = statistic.contains("*");
    	statistic = statistic.replace("*", "");
    	switch (statistic) {
    	case "Missed FG":
    		return Integer.toString(this.data.get("FGA") - this.data.get("FGM"));
    	case "Missed 3pt FG":
    		return Integer.toString(this.data.get("3PA") - this.data.get("3PM"));
    	case "Missed Free Throw":
    		return Integer.toString(this.data.get("FTA") - this.data.get("FTM"));
    	}
    	String stat = mapStat(statistic);
    	switch (stat) {
    	case "Player":
    		if (this.displayName.isEmpty()) {
    			return this.name;
    		} else {
        		return this.name + "_" + this.lastName;
    		}
    	case "MIN":
    		if (isForTable) {
                return Integer.toString((int) Math.round(this.data.get(stat) / 600.0));
    		} else {
                return Integer.toString((int) Math.round(this.data.get(stat)));
    		}
    	case "FG%":
        	return getPercentage(this.data.get("FGM"), this.data.get("FGA"));
    	case "3P%":
        	return getPercentage(this.data.get("3PM"), this.data.get("3PA"));
    	case "FT%":
        	return getPercentage(this.data.get("FTM"), this.data.get("FTA"));
    	case "REB":
    		return Integer.toString(this.data.get("OREB") + this.data.get("DREB"));
    	default:
        	return this.data.get(stat) + "";
    	}
    }
    
    // Pre:  'made' and 'attempted' are greater than 0, otherwise throws an IllegalArgumentException.
    // Post: Returns a String representation of the percentage of 'made' / 'attempted'.
    //       If percentage is 100%, then "100" is returned.
    //       If no shots were made, then "0.0" is returned.
    //       Otherwise, a number with one decimal spot is returned as a String.
    private String getPercentage(int made, int attempted) {
    	if (made < 0 || attempted < 0) {
    		throw new IllegalArgumentException("Number of made/attemped shots cannot be negative");
    	}
        if (made == 0) {
            return "0.0";
        } else {
        	if (attempted == made) {
        		return "100";
        	} else {
                double fg = (double) made / attempted * 100;
                return Math.round(fg * 10.0) / 10.0 + "";
        	}
        }
    }
    
    // Pre:  'stat' is a key in either the data or statMap maps.
    //       Otherwise throws an IllegalArgumentException.
    // Post: Returns the abbreviated version of the 'stat' if 'stat' is the full statistic
    //       name. Otherwise 'stat' is simply returned.
    private String mapStat(String stat) {
    	if (!this.data.containsKey(stat) && !this.statMap.containsKey(stat)) {
    		throw new IllegalArgumentException(stat + " is not a valid statistic");
    	}
    	if (!this.data.containsKey(stat)) {
    		return this.statMap.get(stat);
    	} else {
    		return stat;
    	}
    }
    
    // Post: Returns true if the player's personal fouls are greater than the minimum given by the user
    //       or if the player's technical fouls are greater than the minimum given by the user.
    public boolean hasFouledOut(int PF, int TF, int FI, int FII) {
    	return Integer.valueOf(getStat("PF")) >= PF 
    		   || Integer.valueOf(getStat("TF")) >= TF 
    		   || Integer.valueOf(getStat("FLGI")) >= FI 
    		   || Integer.valueOf(getStat("FLGII")) >= FII;
    }
    
    public boolean isEqual(Player other) {
    	return this.name.equals(other.getName()) && 
    		   this.lastName.equals(other.getLastName()) && 
    		   this.displayName.equals(other.getDisplayName());
    }
    
    // Post: Returns the calculated statistics for the player
    public String getStatLine() {
    	return toString() + ": " + getStat("PTS") + " Points | " + getStat("REB") + " Rebounds | FG%: "
               + getStat("FG%") + " | 3P% " + getStat("3P%") + " | FT%: " + getStat("FT%");
    }
    
    // Post: Returns the players first and last name separated by a space
    public String toString() {
    	return this.name + " " + this.lastName;
    }
    
    // Post: Returns the First Name of the Player.
    public String getName() {
        return this.name;
    }
    
    // Post: Returns the Last Name of the Player.
    public String getLastName() {
    	return this.lastName;
    }
    
    // Post: Returns the Display Name of the Player.
    public String getDisplayName() {
    	return this.displayName;
    }
    
    // Post: Returns true if the player is currently on the court.
    //       Returns false if not.
    public boolean isStarter() {
    	return this.isOnFloor;
    }
    
    // Post: Sets the players first name to be 'name'.
    public void setName(String name) {
        this.name = name;
    }
    
    // Post: Sets the players display name to be 'name'.
    public void setDisplayName(String name) {
        this.displayName = name;
    }
    
    // Post: Player is on court.
    public void setOnFloor() {
    	this.isOnFloor = true;
    }
    
    // Post: Player if off court.
    public void setOffFloor() {
    	this.isOnFloor = false;
    }
}
