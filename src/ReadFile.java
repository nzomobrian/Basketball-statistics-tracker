// Brian Nzomo
// Basketball Statistics Tracker Program
// ReadFile Class
// Reads in an input .BBALL file and creates all necessary components that allow the user to resume tracking.

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;

public class ReadFile extends BasketballMain {

	private static final long serialVersionUID = 1L;
	private static final int UNDO_LENGTH = 8;
	
	private Scanner scanFile;				// Scanner for file
	private Scanner getLines;				// Scanner to get number of lines in the file
	private List<String[]> undoFile;        // List of undo's listed at bottom of game file
	private List<Player> players;		    // List of players from game file 
	private Queue<String> fileData;			// All the lines in the scanned file
	private GameSettings SETTINGS;			// Settings used for coloring/formatting of program
	private String version;                 // Current Version of the Program
	private int numPlayers;					// The number of players being scanned in
	
	// Post: Constructs a ReadFile given the 'fileName' of the input file and the 'settings'
	//       used to format the program.
	@SuppressWarnings("static-access")
	public ReadFile(String fileName, GameSettings settings) {
		try {
			File file = new File(super.FILE_PATH + "\\" + GAMEFILESFOLDER + "\\" + fileName);
			this.scanFile = new Scanner(file);
			this.getLines = new Scanner(file);
		} catch (Exception e) {
			System.out.println("Error, file not found");
			System.out.println(e.getMessage());
		}
		if (!this.scanFile.hasNextLine()) {
			System.out.println(fileName + " is empty.");
			throw new IllegalArgumentException();
		}
		this.fileData = new LinkedList<String>();
		while (this.getLines.hasNextLine()) {
			this.fileData.add(this.getLines.nextLine());
		}
		this.getLines.close();
    	Pattern playerPattern = Pattern.compile("Player\\s.+_.+");
    	// Count the number of players in the input file
    	for (String line : this.fileData) {
    		Matcher playerMatch = playerPattern.matcher(line);
			if (playerMatch.find()) {
				this.numPlayers++;
			}	
    	}
		this.undoFile = new ArrayList<String[]>();
		this.players = new ArrayList<Player>(this.numPlayers);
		this.SETTINGS = settings;
		this.version = this.fileData.remove();
	}
	
	// Post: Returns true if the game being scanned in is over.
	//       Returns false otherwise or if the file is corrupted.
	public boolean isGameOver() {
		try {
			return Integer.valueOf(this.version.split("_")[CURRENT_PERIOD]) == END_OF_GAME;
		} catch (Exception e) {
			return false;
		}
	}
	
	// Post: Returns true if the file is ready to be read in by the system (i.e. is not corrupted).
	//       Returns false otherwise.
	public boolean checkFile() {
		for (int i = 0; i < this.numPlayers; i++) {
			for (int j = 0; j < STATISTIC_ABBREVIATIONS.length + 1; j++) {
				if (j == STATISTIC_ABBREVIATIONS.length) {
					this.fileData.remove();
				} else {
					String stat = STATISTIC_ABBREVIATIONS[j];
					String gameData = this.fileData.remove();
					if (!gameData.contains(stat)) {
						return false;
					}
				}
			}	
		}
		int undo = 0;
		int totalUndo = this.fileData.size();
		while (!this.fileData.isEmpty()) {
			String data = this.fileData.remove();
			String[] checkData = data.split(",");
			if (checkData(checkData) && checkData.length == UNDO_LENGTH) {
				undo++;
			}
		}
		return undo == totalUndo;
	}
	
	// Post: Returns true if all elements in 'data' are correct.
	//       Returns false otherwise.
	private boolean checkData(String[] data) {
		String[] patterns = {"[A-Za-z\\s\\.]+", "[A-Za-z0-9\\s]+", "(OT[0-9])|([0-9][a-z]+)"};
		for (int i = 0; i < data.length; i++) {
			Pattern pattern;
			Matcher match;
			if (i <= 6) {
				pattern = Pattern.compile(patterns[0]);
				match = pattern.matcher(data[i]);
			} else {
				pattern = Pattern.compile(patterns[i - 6]);
				match = pattern.matcher(data[i]);
			}
			if (!match.find()) {
				return false;
			}
		}
		return true;
	}

	// Indices of game data in 'boxscoreData'.
	private static final int NUM_STARTERS = 0;
	private static final int PERSONAL_FOULS = 1;
	private static final int TECHNICAL_FOULS = 2;
	private static final int FLAGRANT_I = 3;
	private static final int FLAGRANT_II = 4;
	private static final int PERIOD_TYPE = 5;
	private static final int GAME_LENGTH = 6;
	private static final int TIME_REMAINING = 7;
	private static final int OVERTIMES = 8;
	private static final int CURRENT_PERIOD = 9;
	private static final int TIMEOUTS = 10;
	private static final String PERIOD_TYPE_ID = "false";
	
	// Post: Returns all the data stored in the game file.
	public Object[] getFileData() {
		return new Object[] {getGameSettingsData(),
				             getPlayers(),
				             getUndoArray()};
		}
	
	// Post: Returns the GameSettings used in the game that is being scanned in from the file.
	private GameSettings getGameSettingsData() {
		String[] boxscoreData = this.scanFile.nextLine().split("_");
		GameSettings data = new GameSettings();
		data.setSetting(Integer.valueOf(boxscoreData[NUM_STARTERS]), Setting.NUMBER_OF_STARTERS);
		data.setSetting(Integer.valueOf(boxscoreData[PERSONAL_FOULS]), Setting.PERSONAL_FOULS);
		data.setSetting(Integer.valueOf(boxscoreData[TECHNICAL_FOULS]), Setting.TECHNICAL_FOULS);
		data.setSetting(Integer.valueOf(boxscoreData[FLAGRANT_I]), Setting.FLAGRANT_I);
		data.setSetting(Integer.valueOf(boxscoreData[FLAGRANT_II]), Setting.FLAGRANT_II);
		data.setSetting(!boxscoreData[PERIOD_TYPE].equals(PERIOD_TYPE_ID), Setting.PERIOD_TYPE);
		data.setSetting(Double.valueOf(boxscoreData[GAME_LENGTH]), Setting.GAME_LENGTH);
		data.setSetting((Double.valueOf(boxscoreData[TIME_REMAINING])), Setting.TIME_REMAINING);
		data.setSetting(Double.valueOf(boxscoreData[OVERTIMES]), Setting.OVERTIME_LENGTH);
		data.setSetting(Integer.valueOf(boxscoreData[CURRENT_PERIOD]), Setting.CURRENT_PERIOD);
		data.setSetting(Integer.valueOf(boxscoreData[TIMEOUTS]), Setting.TIMEOUTS);
		data.setSetting(this.SETTINGS.getSetting(Setting.BACKGROUND_COLOR), Setting.BACKGROUND_COLOR);
		data.setSetting(this.SETTINGS.getSetting(Setting.FONT_COLOR), Setting.FONT_COLOR);
		data.setSetting(this.SETTINGS.getSetting(Setting.TEXT_BORDER_COLOR), Setting.TEXT_BORDER_COLOR);
		data.setSetting(this.SETTINGS.getSetting(Setting.FONT_TYPE), Setting.FONT_TYPE);
		return data;
	}
	
	// Post: Returns a list of the players in the input file with all their statistical data.
	private List<Player> getPlayers() {
		// Add all statistics for each player as they appear in the input file
		for (int i = 0; i < this.numPlayers; i++) {
			Player player = new Player("");
			for (int j = 0; j < STATISTIC_ABBREVIATIONS.length + 1; j++) {
				if (this.scanFile.hasNextLine()) {
					String line = this.scanFile.nextLine();
					Scanner lineScan = new Scanner(line);
					String columnName = lineScan.next();
					String stat = lineScan.next();
					player.setStat(columnName, stat);	
					lineScan.close();
				}
			}
			this.players.add(player);
		}		
		// Scan in all undo's into the 'undoFile' list
		while (this.scanFile.hasNextLine()) {
			String[] undoLine = this.scanFile.nextLine().split(", ");
			if (!undoLine[undoLine.length - 1].equals("DONE")) {
				this.undoFile.add(undoLine);
			}
		}		
		this.scanFile.close();	
		return this.players;
	}
	
	// Indices of data in each String[] in 'undoFile'.
	private static final int PLAYER_NAME = 0;
	private static final int PLAYER_LAST_NAME = 1;
	private static final int PLAYER_DISPLAY_NAME = 2;
	private static final int ASSISTPLAYER_NAME = 3;
	private static final int ASSISTPLAYER_LAST_NAME = 4;
	private static final int ASSISTPLAYER_DISPLAY_NAME = 5;
	private static final int STATISTIC = 6;
	private static final int TIME = 7;
	
	// Post: Returns a list of every undo stored in the input file.
	private List<Undo> getUndoArray() {
		List<Undo> undoArray = new ArrayList<Undo>();
		for (String[] line : this.undoFile) {
			Undo undo = null;
			for (Player player : this.players) {
				// Match player with undo
				boolean match = player.getName().equals(line[PLAYER_NAME]) 
						        && player.getLastName().equals(line[PLAYER_LAST_NAME]) 
						        && player.getDisplayName().equals(line[PLAYER_DISPLAY_NAME]);
				if (match) {
					undo = new Undo(player, line[STATISTIC], line[TIME]);
					for (Player astPlayer : this.players) {
						// Match AssistPlayer with undo
						boolean matchAst = astPlayer.getName().equals(line[ASSISTPLAYER_NAME]) 
								           && astPlayer.getLastName().equals(line[ASSISTPLAYER_LAST_NAME]) 
								           && astPlayer.getDisplayName().equals(line[ASSISTPLAYER_DISPLAY_NAME]);
						if (matchAst) {
							undo.setAstPlayer(astPlayer);
							break;
						}
					}
					break;
				}
			}
			undoArray.add(undo);
		}
		return undoArray;
	}
}
