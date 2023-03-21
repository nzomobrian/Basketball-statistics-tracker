// Brian Nzomo
// Basketball Statistics Tracker Program
// StartersPanel Class
// A window that opens once the 'Start Game' button is pressed in the GetPlayersPanel
// if the number of players entered is greater than the number of starters specified in the settings.
// Allows the user to select their starters for the game before beginning to track statistics. 

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class StartersPanel extends GUISettings {
	
	private static final long serialVersionUID = 1L;
	
	// Indices of buttons in the 'buttonArray'
	private static final int UNDO_BUTTON = 0;
	private static final int STARTGAME_BUTTON = 1;
	private static final int CLOSE_BUTTON = 2;
	private static final int HOME_BUTTON = 3;
	
	// Key Bindings for each button in 'buttonArray'
	// Start button uses the enter key -- It is set to be the default button for the frame.
	private static final int UNDO_BUTTON_KEY = KeyEvent.VK_BACK_SPACE;
	private static final int CLOSE_BUTTON_KEY = KeyEvent.VK_ESCAPE;
	
	private List<Player> startingOnCourt; // The players that will be starting the game.
	private List<Player> startingOnBench; // The players that will be on the bench to begin the game.
	private List<Player> players; // Total list of players.
	private List<JButton> playerButtons; // List of JButtons for each player.
	private Stack<String> displayNames; // The names of players who are selected for being a starter.
	private int starters; // The number of starters currently selected 
	private JTextArea playerList; // The names of players who are selected for being a starter.
	private JLabel header; // The header of the panel that reads 'Select X Starter(s)'
	private JPanel buttons; // The panel on the left side of the panel that lists all buttons in 'playerButtons'.
	private JPanel pane; // Used to switch viewing windows when certain buttons are pressed.
	private JButton[] buttonArray; // All the non-player buttons used in the StartersPanel.
	private JFrame frame; // The frame the StartersPanel is in.
	private String fileName; // The fileName where the game data will be stored.
	private String startersPlural; // Plural/Singular version of 'Starter(s)'.
	private JSplitPane splitPane; // SplitPane used to split the player buttons and the rest of the panel.
	private GameSettings SETTINGS; // The settings used to format this panel and the number of starters.
	
	final private Color background; // The background color as chosen by the user in the settings.
	final int numberStarters; // The number of starters as chosen by the user in the settings.
	
	// Parameters:
	// 'players': The total list of players
	// 'fileName': The name of the file the game data will be stored in
	// 'settings': The settings being used for the game
	public StartersPanel(List<Player> players, String fileName, GameSettings settings) {
		this.SETTINGS = settings;
		this.startersPlural = "Starter";
		int numberStarters = (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS);
		if (numberStarters > 1) {
			this.startersPlural += "s";
		}
		this.players = players;
		this.startingOnCourt = new ArrayList<Player>(numberStarters);
		this.startingOnBench = new ArrayList<Player>(this.players.size() - numberStarters);
		this.playerButtons = new ArrayList<JButton>(this.players.size());
		this.displayNames = new Stack<String>();
		this.background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
		this.numberStarters = (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS);
        this.header = formatLabel("Select " + numberStarters + " " + startersPlural, FONT_SIZE, SETTINGS);
        this.playerList = formatTextArea(SCREENWIDTH / 2 + 90, 500, SETTINGS);
		this.buttons = new JPanel();
        this.buttons.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR));
    	this.fileName = fileName;
    	this.pane = new JPanel(new GridLayout(1, 1));
    	this.pane.setBackground(background);
    	initializeButtons();
	}
	
	public void createAllButtons() {
		createPlayerButtons();
		createUndoButton();
		createStartButton();
		createCloseButton();
		createHomeButton();
	}
	
    // Post: Creates and formats all buttons used in the StartersPanel.
	private void initializeButtons() {
		String[] buttonNames = {" Undo", " Start Game", " Close", "No"};
        int size = FONT_SIZE / 2;
        int[] sizes = {size, size, size, FONT_SIZE * 3 / 4};
        this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
        this.buttonArray[UNDO_BUTTON].setEnabled(false);
        this.buttonArray[STARTGAME_BUTTON].setEnabled(false);
        this.buttonArray[HOME_BUTTON].setBorder(null);
    	int[] indices = {UNDO_BUTTON, STARTGAME_BUTTON, CLOSE_BUTTON};
    	String[] icons = {UNDO_BUTTON_ICON, START_BUTTON_ICON, CLOSE_BUTTON_ICON};
    	formatIcons(this.buttonArray, indices, icons);
	}
	
	// Post: Adds functionality to the Home Button. 
	public void createHomeButton() {
		this.buttonArray[HOME_BUTTON].addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				pane.removeAll();
				pane.setLayout(new GridLayout(1, 1));
				pane.add(splitPane);
				pane.repaint();
				pane.revalidate();
				frame.setTitle("Starter Selection");
            	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.getRootPane().setDefaultButton(buttonArray[STARTGAME_BUTTON]);
			}
		});
	}
	
	// Post: Adds functionality to all player buttons that appear in a grid on the left side of the StartersPanel.
	public void createPlayerButtons() {
        for (Player player : this.players) {
        	JButton button = new JButton(player.toString());
        	formatButton(button, BUTTON_HEIGHT * 3, BUTTON_HEIGHT, FONT_SIZE / 2, SETTINGS);
        	buttons.add(button);
        	playerButtons.add(button);
        	button.addActionListener(new AbstractAction() {
        		public void actionPerformed(ActionEvent e) {
        			// If the number of starters required has not yet been reached
        			if (starters < numberStarters) {
    					starters++;
            			startingOnCourt.add(player); 
                        displayNames.push(player.getName() + " " + player.getLastName());
                        playerList.setText(displayNames.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                        button.setEnabled(false);
                        buttonArray[UNDO_BUTTON].setEnabled(true);
                        // If the number of starters is reached, all player buttons are disabled
                        if (startingOnCourt.size() == numberStarters) {
                        	buttonArray[STARTGAME_BUTTON].setEnabled(true);
                        	for (JButton btn : playerButtons) {
                        		btn.setEnabled(false);
                        	}
                        } else { // The pressed player button is disabled
                        	buttonArray[STARTGAME_BUTTON].setEnabled(false);
                        }
        			}
        		}
        	});
        }
	}
	
	// Post: Adds the function for the 'Undo' button which removes the most recently added player
	//       to the starters list.
	public void createUndoButton() {
    	this.buttonArray[UNDO_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
				Player remove = startingOnCourt.get(startingOnCourt.size() - 1);
				// Reactivate the button of the most recently removed player
				for (JButton btn : playerButtons) {
					if (btn.getText().equals(remove.toString())) {
						btn.setEnabled(true);
						break;
					}
				}
            	for (JButton btn : playerButtons) {
            		if (!displayNames.contains(btn.getText())) {
            			btn.setEnabled(true);
            		}
            	}
		        displayNames.pop();
		        playerList.setText(displayNames.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
				startingOnCourt.remove(remove); 
				starters--;		  
				buttonArray[UNDO_BUTTON].setEnabled(!startingOnCourt.isEmpty());
				buttonArray[STARTGAME_BUTTON].setEnabled(startingOnCourt.size() >= (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS));
    		}
    	});
    	setButtonKey(this.buttonArray[UNDO_BUTTON], UNDO_BUTTON_KEY, 0);
	}
	
	// Post: Adds the function for the 'Start' button. Asks the user to confirm whether they want to 
	//       start the game. 
	public void createStartButton() {
    	this.buttonArray[STARTGAME_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			AbstractAction button = new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {                 	
                    	// Set starters to be on the court
                    	for (int i = 0; i < startingOnCourt.size(); i++) {
                    		Player starter = startingOnCourt.get(i);
                    		starter.setOnFloor();
                    		players.remove(starter);
                    	}
                    	// Rest of players go to the bench
                    	startingOnBench.addAll(players);
                        startTracking(startingOnCourt, startingOnBench, new ArrayList<Undo>(), fileName, SETTINGS);
                        frame.dispose();
                    }
                };
            	confirmPane(pane, frame, buttonArray[HOME_BUTTON].getActionListeners()[0], button, 
            			    "Confirm " + startersPlural + "?", TEAM_ICON, SETTINGS);	
    		}
    	});
	}
	
	// Post: Adds functionality for the 'Close' button. Closes the StartersPanel window when pressed.
	public void createCloseButton() {
    	this.buttonArray[CLOSE_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			startingOnCourt.clear();
    			startingOnBench.clear();
    			frame.dispose();
    		}
    	});
    	setButtonKey(this.buttonArray[CLOSE_BUTTON], CLOSE_BUTTON_KEY, 0);
	}
	
	// Post: Adds all components to the StartersPanel.
	public void addElements() {  		       
        // The panel that stores all three buttons on the StartersPanel
        JPanel undoStartClose = new JPanel();
        formatPanel(undoStartClose, new Component[] {panelfy(this.buttonArray[UNDO_BUTTON], background, null, null), 
        		                                     panelfy(this.buttonArray[STARTGAME_BUTTON], background, null, null), 
        		                                     panelfy(this.buttonArray[CLOSE_BUTTON], background, null, null)}, null,
        		    new GridLayout(3, 1), background);
        
        // All the elements of the StartersPanel
        JPanel total = new JPanel();
        formatPanel(total, new Component[] {panelfy(this.header, background, null, 
        		                            new MatteBorder(BORDER_SIZE * 4, BORDER_SIZE, BORDER_SIZE * 2, BORDER_SIZE, background)), 
        		                            panelfy(this.playerList, background, null, null), 
        		                            undoStartClose}, 
        		    null, null, background);
        
        // Adds a scroll bar to the list of player buttons on the left/right side of the StartersPanel
        this.buttons.setLayout(new GridLayout(players.size(), 1));
        JScrollPane scrollPane = addScrollPane(this.buttons);
        
        // The panel that stores the scrollable list of players that appears on either side of the StartersPanel
        JPanel panePanel = new JPanel(new GridLayout(1, 1));
        panePanel.add(scrollPane);
        
        // If the user selects the right handed option, the scrollable player list appears on the right
        if (RIGHT_HANDED) {
            this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, total, panePanel);
            this.splitPane.setDividerLocation(SCREENWIDTH - (SCREENWIDTH * 2 / 5));
        } else { // Scrollable player list appears on left for left handed users
            this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panePanel, total);
            this.splitPane.setDividerLocation(SCREENWIDTH * 2 / 7);
        }
        this.splitPane.setEnabled(false);     
	}
    
	// Post: Puts the StartersPanel in a Frame.
    public void frame() {
        this.frame = new JFrame("Starter Selection");
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pane.add(this.splitPane);
        this.frame.add(this.pane);
        this.frame.getRootPane().setDefaultButton(this.buttonArray[STARTGAME_BUTTON]);
    }
    
    // Post: Returns a JPanel with all the components of a StartersPanel.
    public JPanel getStartersPanel() {
    	this.splitPane.setBorder(new MatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, DEFAULT_TEXT_BORDER_COLOR));
    	this.splitPane.setBackground(background);
    	JPanel panel = new JPanel(new GridLayout(1, 1));
    	panel.setPreferredSize(new Dimension(SCREENWIDTH - 250, 900));
    	panel.setBackground(background);
    	panel.add(this.splitPane);
    	return panel;
    }
}
