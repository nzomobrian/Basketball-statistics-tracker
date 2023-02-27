// Brian nzomo
// Basketball Statistics Tracker Program
// Constants Class
// All the constants used in the program.

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.JFrame;

public abstract class Constants extends JFrame {

	protected static final long serialVersionUID = 1L;
	
	/*
	 * ************************************************
	 * *		         TEXT ARRAYS                  *
	 * ************************************************
	 * These are the arrays with the statistic names and
	 * abbreviations used in basketball. 
	 * STATISTICS are all the statistics kept track of by this program
	 * STATISTIC_ABBREVIATIONS are the abbreviated statistic names used
	 * STATISTIC_ENTIRE_WORDS are all statistics spelled out entirely with no abbreviations
	 * 		-Necessary for the abbreviation explanations found in the InstructionsPanel class.
	 * STATISTIC_DISPLAY_WORDS is an extension of STATISTIC_ENTIRE_WORDS but has the calculated statistics
	 * (Percentages and total rebounds) abbreviated for display purposes.
	 * GAME_PERIODS is used for the GameClock to cycle through periods. Used for both Halves and Quarters.
	 */
    protected static final String[] STATISTICS = {"Made FG", 
    											  "Missed FG", 
    											  "Made 3pt FG", 
    											  "Missed 3pt FG", 
    											  "Made Free Throw", 
    											  "Missed Free Throw", 
    											  "Assist", 
    											  "Offensive Rebound", 
    											  "Defensive Rebound", 
    											  "Turnover", 
    											  "Steal", 
    											  "Block", 
    											  "Personal Foul", 
    											  "Technical Foul",
    											  "Flagrant I Foul",
    											  "Flagrant II Foul"};
    protected static final String[] STAT_KEYS = {"2", "Shift-2", "3", "Shift-3", "1", "Shift-1", "A", "Shift-R", "R", "T", "S", "B", "P", "F", "X", "Shift-X"};
    protected static final int[] KEY_EVENTS = {KeyEvent.VK_2,
    										   KeyEvent.VK_2,
    										   KeyEvent.VK_3,
    										   KeyEvent.VK_3,
    										   KeyEvent.VK_1,
    										   KeyEvent.VK_1,
    										   KeyEvent.VK_A,
    										   KeyEvent.VK_R,
    										   KeyEvent.VK_R,
    										   KeyEvent.VK_T,
    										   KeyEvent.VK_S,
    										   KeyEvent.VK_B,
    										   KeyEvent.VK_P,
    										   KeyEvent.VK_F,
    										   KeyEvent.VK_X,
    										   KeyEvent.VK_X};
    protected static final String[] STATISTIC_ABBREVIATIONS = {"Player", 
    														   "MIN", 
    														   "PTS", 
    														   "FGM", 
    														   "FGA", 
    														   "FG%", 
    														   "3PM", 
    														   "3PA", 
    														   "3P%", 
    														   "FTM", 
    														   "FTA", 
    														   "FT%", 
    														   "OREB", 
    														   "DREB", 
    														   "REB", 
    														   "AST", 
    														   "TOV", 
    														   "STL", 
    														   "BLK", 
    														   "PF", 
    														   "TF",
    														   "FLGI",
    														   "FLGII"};  
    protected static final String[] STATISTIC_ENTIRE_WORDS = {"Player", 
    														  "Minutes", 
    														  "Points", 
    														  "Made Field Goal", 
    														  "Missed Field Goal", 
    														  "Field Goal Percentage", 
    														  "Made 3 Pointer", 
    														  "Missed 3 Pointer", 
    														  "Three Point Percentage", 
    														  "Made Free Throw", 
    														  "Missed Free Throw", 
    														  "Free Throw Percentage", 
    														  "Offensive Rebound", 
    														  "Defensive Rebound", 
    														  "Rebound", 
    														  "Assist", 
    														  "Turnover", 
    														  "Steal", 
    														  "Block", 
    														  "Personal Foul", 
    														  "Technical Foul",
    														  "Flagrant Level I Foul",
    														  "Flagrant Level II Foul"};
	protected static final String[] STATISTIC_DISPLAY_WORDS = {"Player", 
															   "Minutes", 
															   "Points", 
															   "Made FG", 
															   "Missed FG", 
															   "FG%", 
															   "Made 3pt FG", 
															   "Missed 3pt FG", 
															   "3P%", 
															   "Made Free Throw", 
															   "Missed Free Throw", 
															   "FT%", 
															   "Offensive Rebound", 
															   "Defensive Rebound", 
															   "Rebound", 
															   "Assist", 
															   "Turnover", 
															   "Steal", 
															   "Block", 
															   "Personal Foul", 
															   "Technical Foul",
															   "Flagrant I Foul",
															   "Flagrant II Foul"};
	protected static final String[] GAME_PERIODS = {"1st", "2nd", "3rd", "4th"};
	
	/*
	 * ************************************************
	 * *			   DEFAULT CONSTANTS              *
	 * ************************************************
	 * These are the default game settings used for a game.
	 * They resemble the settings for an NBA game.
	 */
	protected static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	protected static final Color DEFAULT_FONT_COLOR = Color.BLACK;
	protected static final Color DEFAULT_TEXT_BORDER_COLOR = Color.BLACK;
	protected static final String DEFAULT_FONT_TYPE = "Verdana";
	protected static final int DEFAULT_NUMBER_OF_STARTERS = 5;
	protected static final int DEFAULT_PERSONAL_FOULS_ALLOWED = 6;
	protected static final int DEFAULT_TECHNICAL_FOULS_ALLOWED = 2;
	protected static final int DEFAULT_CURRENT_PERIOD = 0;
	protected static final boolean DEFAULT_PERIOD_TYPE = true; // Default Period Type is Quarters
	protected static final double DEFAULT_GAME_LENGTH = 12.0;
	protected static final double DEFAULT_OVERTIME_LENGTH = 5.0;
	protected static final double DEFAULT_TIME_REMAINING = 12.0;
	protected static final int DEFAULT_TIMEOUTS = 7;
	protected static final int DEFAULT_OT_TIMEOUTS = 2;
	protected static final int DEFAULT_FLAGRANT_1 = 2;
	protected static final int DEFAULT_FLAGRANT_2 = 1;
	
	/*
	 * ************************************************
	 * *			   MAXIMUM CONSTANTS              *
	 * ************************************************
	 * The Maximum values for each of the settings shown in the 'DEFAULT SETTINGS'.
	 */
	protected static final int MAX_STARTERS = 5;
	protected static final int MAX_PERSONAL_FOULS = 7;
	protected static final int MAX_TECHNICAL_FOULS = 4;
	protected static final int MAX_TIMEOUTS = 7;
	
	/*
	 * ************************************************
	 * *		         MISC DATA                    *
	 * ************************************************
	 * Constants used for formatting purposes.
	 * YEAR, MONTH, and DAYNUM are used to write the date on new game files.
	 * COLOR_NAMES and COLORS have the Color name represented as a String (in COLOR_NAMES) that corresponds
	 * to the same Color of the same index in COLORS.
	 * ROUNDED_BORDERS is used mainly for button borders, default is no rounded borders.
	 * FILETYPE is the type of file the game data is written to.
	 */
    protected static final boolean RIGHT_HANDED = false;
	protected static final int SCREENWIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    protected static final int SCREENHEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    protected static final int BUTTON_HEIGHT = SCREENHEIGHT / 10;
    protected static final int FONT_SIZE = SCREENHEIGHT / 18;
    protected static final int BORDER_SIZE = SCREENHEIGHT / 108;
    protected static final int QUARTERS = 4;
    protected static final int HALVES = 2;
    protected static final int END_OF_GAME = 1909;
    protected static final int TIMER_SETTING = 100;
    protected static final Color FOULED_OUT_BUTTON_COLOR = Color.RED;
    protected static final Color BENCH_BUTTON_COLOR = Color.LIGHT_GRAY;
    protected static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    protected static final int MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1;
    protected static final int DAYNUM = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    protected static final String[] COLOR_NAMES = {"Red", "Orange", "Yellow", "Green", "Cyan", "Blue", "Magenta", "Pink", "Light Gray", "Gray", "Dark Gray"};
    protected static final Color[] COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.PINK, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY};
    protected static final boolean ROUNDED_BORDERS = false;
    protected static final String FILETYPE = "bball";
    
	// InstructionPanel Files:
	
	// Name of the text file containing the written explanations for each statistic being
	// tracked by the program
    protected static final String STAT_EXPLANATIONS_FILE = "Stat_Explanations.txt";
	// Name of the text file containing the explanations found in the Instructions window
    protected static final String TEXT_FILE_NAME = "Instructions.txt";	
	// The sequence of characters in the TEXT_FILE that signals a new paragraph
    protected static final String GAMEDATA_FILE_NAME = "InstructionExample." + FILETYPE;
	// Used when scanning the text file to enter the correct number of starters
    
	/*
	 * ************************************************
	 * *		         ICON NAMES                   *
	 * ************************************************
	 * File names of all icons used for buttons
	 */
    protected static final String SLASH = "/";
    protected static final String SUBMIT_BUTTON_ICON = SLASH + "submit.png";
    protected static final String UNDO_BUTTON_ICON = SLASH + "undo.png";
    protected static final String START_BUTTON_ICON = SLASH + "start.png";
    protected static final String OLDGAMES_BUTTON_ICON = SLASH + "oldGames.png";
    protected static final String INSTRUCTIONS_BUTTON_ICON = SLASH + "instructions.png";
    protected static final String CLOSE_BUTTON_ICON = SLASH + "close.png";
    protected static final String SETTINGS_BUTTON_ICON = SLASH + "settings.png";
    protected static final String PLAYER_ICON = SLASH + "player.png";
    protected static final String SCORE_BUTTON_ICON = SLASH + "score.png";
    protected static final String PLAY_BUTTON_ICON = SLASH + "play.png";
    protected static final String PAUSE_BUTTON_ICON = SLASH + "pause.png";
    protected static final String DONE_BUTTON_ICON = SLASH + "done.png";
    protected static final String BOXSCORE_BUTTON_ICON = SLASH + "boxscore.png";
    protected static final String MAINMENU_BUTTON_ICON = SLASH + "mainMenu.png";
    protected static final String ABBREVIATIONS_BUTTON_ICON = SLASH + "abbreviations.png";
    protected static final String ROSTER_MANAGEMENT_ICON = SLASH + "rosterManagement.png";
    protected static final String TIMEOUT_BUTTON_ICON = SLASH + "timeout.png";
    protected static final String TEAMFOULS_ICON = SLASH + "fouls.png";
    protected static final String FILE_WARNING_ICON = SLASH + "fileWarning.png";
    protected static final String OVERTIME_ICON = SLASH + "overtime.png";
    protected static final String SUB_PLAYER_ICON = SLASH + "sub.png";
    protected static final String TEAM_ICON = SLASH + "team.png";
    protected static final String GAME_OVER_ICON = SLASH + "gameOver.png";
    protected static final String GAMEOVER_WARNING_ICON = SLASH + "combo.png";
    protected static final int ICON_SIZE = SCREENHEIGHT * 2 / 27;
    
	/*
	 * ************************************************
	 * *		         Settings                     *
	 * ************************************************
	 * Enumerated values used to identify certain settings
	 */
	enum Setting {
		 BACKGROUND_COLOR,
		 FONT_COLOR,
		 TEXT_BORDER_COLOR,
		 FONT_TYPE,
		 NUMBER_OF_STARTERS,
		 PERSONAL_FOULS,
		 TECHNICAL_FOULS,
		 FLAGRANT_I,
		 FLAGRANT_II,
		 CURRENT_PERIOD,
		 PERIOD_TYPE,
		 GAME_LENGTH,
		 OVERTIME_LENGTH,
		 TIME_REMAINING,
		 TIMEOUTS,
		 OT_TIMEOUTS;
	}
}
