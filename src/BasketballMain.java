// Brian Nzomo
// Basketball Statistics Tracker Program
// Basketball Main
// Starts the Basketball Statistics Tracking Program.

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@SuppressWarnings("serial")
public abstract class BasketballMain extends Constants {
	
	protected static final String GAMEFILESFOLDER = "GameFiles";
	protected static final String ICON_FOLDER = "Image";
	protected static final String SOURCE_FOLDER = "src";
	protected static final String INSTRUCTIONS_FOLDER = "Instructions";
	protected static String FILE_PATH = "";

	public static void main(String[] args) throws InterruptedException {
		// Make Folder to store all Game Files created by the program
		Path pathToDirectory = Paths.get("");
		String path = pathToDirectory.toAbsolutePath().toString();
		FILE_PATH = path;
		File f = new File(path + "\\" + GAMEFILESFOLDER);
		boolean folderCreated = false;
		try {
		    folderCreated = f.mkdir();
		} catch (Exception e){
		    System.out.println("Error in creating folder");
		    System.out.println(e.getMessage());
		} 
		// Checks if this run is the first time the program is being run
		// If so, gives the user an introduction to get all files in order
		// Otherwise proceeds right to starting the program.
		if (folderCreated) {
			intro(path);
		} else {
			// Default settings of an NBA Game used for initial tracking
	    	GameSettings settings = new GameSettings(); 
	    	// The main menu panel used when the program launches
	        GetPlayersPanel mainMenu = new GetPlayersPanel(settings);
	        mainMenu.createTextField();
	        mainMenu.createAllButtons();
	        mainMenu.addElements();
	        mainMenu.frame();
		}
    }
	

	// Post: Gives the user the necessary information to correctly run the program
	private static void intro(String path) {
		System.out.println("Basketball Statisitcs Tracking Program");
		System.out.println();
		System.out.println("A folder to store Game Files has been created under: ");
		System.out.println("\t" + path);
		System.out.println();
	}
    
	// Parameters:
	// 'list': List of players that are part of the game
	// 'undo': List of Undos (only used for scanned in games), otherwise this is empty for new games
	// 'fileName': The file name of the file where the game data is stored
	// 'scan': Whether a file is being scanned in or not
	// 'settings': The settings used for this game
    protected static void run(List<Player> list, List<Undo> undo, String fileName, 
    					   boolean scan, GameSettings settings) {  
    	int numStarters = (int) settings.getSetting(Setting.NUMBER_OF_STARTERS);
    	if (!scan) { // If a brand new game is being started
    		if (list.size() > numStarters) { // If the number of players exceeds the number of starters
    			// Open up the Starters Selection Panel to select starters
            	StartersPanel getStarters = new StartersPanel(list, fileName, settings);
            	getStarters.createAllButtons();
            	getStarters.addElements();
            	getStarters.frame();
        	} else { // If number of players equals number of starters
        		startTracking(list, new ArrayList<Player>(0), undo, fileName, settings);
        	}
    	} else { // If a game is being scanned in from a file
    		if (list.size() == numStarters) { // If number of players equals number of starters
        		startTracking(list, new ArrayList<Player>(0), undo, fileName, settings);
    		} else { // If the number of players exceeds the number of starters
        		List<Player> bench = new ArrayList<Player>(list.size() - numStarters);
        		List<Player> starters = new ArrayList<Player>(numStarters);       		
        		for (Player player : list) {
        			if (player.isStarter()) {
        				starters.add(player);
        			} else {
        				bench.add(player);
        			}
        		}
        		startTracking(starters, bench, undo, fileName, settings);
    		}
    	}  	
    }
    
    // Parameters:
	// 'starters': List of players that are starting the game
    // 'bench': List of players that are starting the game on the bench
	// 'undo': List of Undos (only used for scanned in games), otherwise this is empty for new games
	// 'fileName': The file name of the file where the game data is stored
    // 'settings': The settings used for this game
    protected static void startTracking(List<Player> starters, List<Player> bench, 
    								    List<Undo> undo, String fileName, GameSettings settings) {
        ManagementPanel manageGame = new ManagementPanel(starters, bench, undo, fileName, settings);
        manageGame.createTableButton();
        if (bench.isEmpty()) {
            manageGame.createTeamButtons();
        } else {
            manageGame.createPlayerButtons();
        }
        manageGame.createUndoButton();
        manageGame.createDoneButton();
        manageGame.createStartStopButton();
        manageGame.createTimeoutButton();
        manageGame.createHomeButton();
        //manageGame.createSettingsButton();
        manageGame.addElements();
        manageGame.frame();
    }
}
