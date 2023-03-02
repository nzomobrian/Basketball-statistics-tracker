// Brian Nzomo
// Basketball Statistics Tracker Program
// GameSettings Class
// GameSettings keeps track of all the settings used for the actual game/game play and the
// settings used to format the program (background color, etc.).

import java.awt.*;
import java.awt.event.*;

public class GameSettings extends BasketballMain implements ActionListener {
	
	private Color background_color; // Background color used throughout the program
	private int number_of_starters; // Number of starters for the game
	private int personal_fouls; // Number of personal fouls allowed
	private int technical_fouls; // Number of technical fouls allowed
	private int flagrant_i; // Number of Flagrant I fouls allowed
	private int flagrant_ii; // Number of Flagrant II fouls allowed
	private int current_period; // The current period of the game (1st quarter/half, 2nd quarter/half ...)
	private boolean period_type; // Period Type ---> Quarters: true / Halves: false
	private double game_length; // The length of each period
	private double overtime_length; // The length of an overtime
	private double time_remaining; // The time remaining in the current period
	private int timeouts; // The number of timeouts for the team
	private int OTTimeouts; // The number of timeouts per overtime period
	
    private static final long serialVersionUID = 1L;
    
    // Post: Set the settings of the game to resemble those of a typical NBA
    //       basketball game with four 12-minute quarters, 6 personal and 2 technical
    //       fouls allowed per player.
    //       The background colors are set to white and all borders and text colors are
    //       set to black.
    public GameSettings() {
    	this.background_color = DEFAULT_BACKGROUND_COLOR;
    	this.number_of_starters = DEFAULT_NUMBER_OF_STARTERS;
    	this.personal_fouls = DEFAULT_PERSONAL_FOULS_ALLOWED;
    	this.technical_fouls = DEFAULT_TECHNICAL_FOULS_ALLOWED;
    	this.flagrant_i = DEFAULT_FLAGRANT_1;
    	this.flagrant_ii = DEFAULT_FLAGRANT_2;
    	this.period_type = DEFAULT_PERIOD_TYPE;
    	this.game_length = DEFAULT_GAME_LENGTH;
    	this.overtime_length = DEFAULT_OVERTIME_LENGTH;
    	this.time_remaining = DEFAULT_TIME_REMAINING;
    	this.current_period = DEFAULT_CURRENT_PERIOD;
    	this.timeouts = DEFAULT_TIMEOUTS;
    	this.OTTimeouts = DEFAULT_OT_TIMEOUTS;
    	
    }
    
    // Post: Returns an object representing the setting linked with a certain integer
    //       defined by the "Settings" constants in the Constants superclass.
    //       If the parameter 'choose' is not linked with a setting, the method returns null.
    protected Object getSetting(Setting choose) {
    	switch(choose) {
    	case BACKGROUND_COLOR:
    		return this.background_color;
    	case NUMBER_OF_STARTERS:
    		return this.number_of_starters;
    	case PERSONAL_FOULS:
    		return this.personal_fouls;
    	case TECHNICAL_FOULS:
    		return this.technical_fouls;
    	case FLAGRANT_I:
    		return this.flagrant_i;
    	case FLAGRANT_II:
    		return this.flagrant_ii;
    	case CURRENT_PERIOD:
    		return this.current_period;
    	case PERIOD_TYPE:
    		return this.period_type;
    	case GAME_LENGTH:
    		return this.game_length;
    	case OVERTIME_LENGTH:
    		return this.overtime_length;
    	case TIME_REMAINING:
    		return this.time_remaining;
    	case TIMEOUTS:
    		return this.timeouts;
    	case OT_TIMEOUTS:
    		return this.OTTimeouts;
    	default:
    		return null;	
    	}
    }
    
    // Post: Sets the setting linked with the parameter 'choose' with the Object 'data'.
    //       THe Object is cast to the correct type depending on which setting is being set.
    protected void setSetting(Object data, Setting choose) {
    	switch (choose) {
    	case BACKGROUND_COLOR:
    		this.background_color = (Color) data;
    		break;
    	case NUMBER_OF_STARTERS:
    		this.number_of_starters = (int) data;
    		break;
    	case PERSONAL_FOULS:
    		this.personal_fouls = (int) data;
    		break;
    	case TECHNICAL_FOULS:
    		this.technical_fouls = (int) data;
    		break;
    	case FLAGRANT_I:
    		this.flagrant_i = (int) data;
    		break;
    	case FLAGRANT_II:
    		this.flagrant_ii = (int) data;
    		break;
    	case CURRENT_PERIOD:
    		this.current_period = (int) data;
    		break;
    	case PERIOD_TYPE:
    		this.period_type = (boolean) data;
    		break;
    	case GAME_LENGTH:
    		this.game_length = (double) data;
    		break;
    	case OVERTIME_LENGTH:
    		this.overtime_length = (double) data;
    		break;
    	case TIME_REMAINING:
    		this.time_remaining = (double) data;
    		break;
    	case TIMEOUTS:
    		this.timeouts = (int) data;
    		break;
    	case OT_TIMEOUTS:
    		this.OTTimeouts = (int) data;
    		break;
    	default:
    		break;
    	}
    }
    
    protected static final String DIVIDER = "_";
    
    // Post: Returns a String representation of the game settings used when writing
    //       to the game file.
    public String toString() {
    	String[] data = {this.number_of_starters + "", + this.personal_fouls + "",
            this.technical_fouls + "", this.flagrant_i + "",
            this.flagrant_ii + "", this.period_type + "",
            this.game_length + "", this.time_remaining + "",
            this.overtime_length + "", this.current_period + "", 
            this.timeouts + ""};
    	String result = data[0];
    	for (int i = 1; i < data.length; i++) {
    		result += DIVIDER + data[i];
    	}
    	return result;
    }
    
    public void actionPerformed(ActionEvent arg0) {       
    }
}
