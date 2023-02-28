// Brian Nzomo
// Basketball Statistics Tracker Program
// CreateFile class
// The CreateFile class is used to generate the files where the game data
// is stored. These files can be read back in using the 'Load Old Game' feature.

import java.util.*;

public class CreateFile extends BasketballMain {

	private static final long serialVersionUID = 1L;
	
	private Formatter boxscore; // The Formatter used to create the file
	private String fileName; // Name of file with the game data
    
	// Post: Creates a file with the name 'fileName'
    public CreateFile(String fileName) {
    	this.fileName = fileName;
    }
    
    @SuppressWarnings("static-access")
	public void openFile() {
        try {
            this.boxscore = new Formatter(super.FILE_PATH + "\\" + GAMEFILESFOLDER + "\\" + this.fileName);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
   
    // Post: Prints the current game settings at the top of the file.
    //       Each setting is separated by an underscore.
    public void addGameConfig(GameSettings settings) {
    	this.boxscore.format("%s%n", settings.toString());
    }
    
    // Post: Prints the 'statName' and the 'stat' to a new line on the file.
    public void addInformation(String statName, String stat) {
        this.boxscore.format("%s %s%n", statName, stat);
    }
    
    // Post: Prints all game history (each undo is an event that happened in the game) in 
    //       chronological order to the bottom of the file.
    public void addUndo(List<Undo> undo) {
    	for(Undo u : undo) {
    		this.boxscore.format("%s%n", u.toStringArray().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
    	}
    }
    
    // Post: Closes the 'boxscore'.
    public void closeFile() {
        this.boxscore.close();
    }
}
