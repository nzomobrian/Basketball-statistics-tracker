// Brian Nzomo
// Basketball Statistics Tracker Progam
// GetPlayersPanel Class
// The window that opens when the program is started. Allows the user to create their team by entering
// player names or reloading old games.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.border.*;

/*
* The GetPlayersPanel" class opens a window for the user to either create a new team or to load an old game
* that they want to continue.
* The user can either "Submit" new players by entering their first name into a text field and press "Done"
* when they enough players (Determined by the class constant "ROSTER_SIZE"), or they can "Load an Old Game".
*/

public class GetPlayersPanel extends GUISettings {
	
    private static final long serialVersionUID = 1L;
    
	// Indices of buttons in the 'buttonArray'
	private static final int SUBMIT_BUTTON = 0;
	private static final int UNDO_BUTTON = 1;
	private static final int START_BUTTON = 2;
	private static final int OLDGAMES_BUTTON = 3;
	private static final int INSTRUCTIONS_BUTTON = 4;
	private static final int CLOSE_BUTTON = 5;
	private static final int SETTINGS_BUTTON = 6;
	private static final int HOME_BUTTON = 7;
	
	// Key Bindings for each button in 'buttonArray'
	// Submit button uses the enter key -- It is set to be the default button for the frame.
	private static final int UNDO_BUTTON_KEY = KeyEvent.VK_BACK_SPACE;
	private static final int START_BUTTON_KEY = KeyEvent.VK_F4;
	private static final int OLDGAMES_BUTTON_KEY = KeyEvent.VK_F2;
	private static final int INSTRUCTIONS_BUTTON_KEY = KeyEvent.VK_F1;
	private static final int SETTINGS_BUTTON_KEY = KeyEvent.VK_F9;
	private static final int HOME_BUTTON_KEY = KeyEvent.VK_ESCAPE;

	
	// Whether a file is being scanned in or not
	private static final boolean IS_SCAN = true;
	
    private List<Player> players; // The list of players on the team inputted by the user
    private String fileName; // The name of the file the game's data will be stored in
    private JButton[] buttonArray; // All the buttons on the GetPlayersPanel
    private JTextField name; // The text field used to enter player names
    private JFrame frame; // The frame that opens when the program is started
    private JFrame settingsFrame; // The frame that opens when the 'Settings' button is pressed
    private JPanel pane; // Frames all elements in GetPlayersPanel into a single JPanel used for display in the InstructionsPanel
    private JPanel mainPanel; // The panel that stores the Home Screen
    private GameSettings SETTINGS; // The settings used to configure the current game
    private List<JPanel> panels; // All the panels in the GetPlayersPanel, used to quickly change background color in Settings
    private Stack<String> displayNames; // The player names as they appear in the 'labelList'
    private JPanel playerNames; // The dynamic list of player names updated whenever a player is entered or removed
    private InstructionPanel instructionsPanel; // The instructions window that opens when the 'Instructions' button is pressed
    
    private final Color background; // The background color as determined by the user in the settings.
    
    // Parameters:
    // 'settings': The settings used for the game. Can be changed in the settings window.
    public GetPlayersPanel(GameSettings settings) {
    	this.SETTINGS = settings;
    	this.background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
        this.players = new ArrayList<Player>();
        this.panels = new ArrayList<JPanel>();
        this.displayNames = new Stack<String>();
        this.name = new JTextField(FONT_SIZE / 3);
        initializeButtons();
        this.settingsFrame = new JFrame("Settings");
	    this.settingsFrame.setMinimumSize(new Dimension(SCREENWIDTH * 4 / 5, SCREENHEIGHT * 4 / 5));
        this.playerNames = new JPanel();
        this.pane = new JPanel();
        this.mainPanel = new JPanel();
        this.pane.setBackground(this.background);
        this.pane.setBorder(new MatteBorder(BORDER_SIZE, 0, 0, 0, this.background));
        this.panels.add(this.pane);
		this.instructionsPanel = new InstructionPanel(SETTINGS);
        this.frame = new JFrame("Basketball Statistics Tracking");
    	updatePlayerList();
    }
    
    // Post: Creates and formats all buttons used in the GetPlayersPanel.
    private void initializeButtons() {
        // Create all buttons used in the GetPlayersPanel
        String[] buttonNames = {" Add Player", " Undo", " Start Game", " Old Games", " Instructions", " Close", " Settings", " Main Menu"};
        int size = FONT_SIZE / 2;
        int[] sizes = {size, size, FONT_SIZE * 2 / 3, size, 
        			     size, size, size, size, size};
        this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
        this.buttonArray[UNDO_BUTTON].setEnabled(false);
        this.buttonArray[START_BUTTON].setEnabled(false);
        // Add icons to all buttons
        int[] indices = {INSTRUCTIONS_BUTTON, SETTINGS_BUTTON, OLDGAMES_BUTTON, 
        		         CLOSE_BUTTON, UNDO_BUTTON, SUBMIT_BUTTON, START_BUTTON, HOME_BUTTON};
    	String[] icons = {INSTRUCTIONS_BUTTON_ICON, SETTINGS_BUTTON_ICON, OLDGAMES_BUTTON_ICON, 
    			          CLOSE_BUTTON_ICON, UNDO_BUTTON_ICON, SUBMIT_BUTTON_ICON, START_BUTTON_ICON, MAINMENU_BUTTON_ICON};
    	formatIcons(this.buttonArray, indices, icons);
    }
    
    // Post: Formats the text field used to enter player names.
    public void createTextField() {
        this.name.setToolTipText("Enter Player name here:");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                name.requestFocusInWindow();
            }
        });       
        this.name.setHorizontalAlignment(JTextField.CENTER);
        this.name.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, FONT_SIZE * 2 / 3));       
        this.name.addFocusListener(new FocusListener() {
        	// When text field is clicked
        	public void focusGained(FocusEvent e) {
        		name.setText(null);
        		name.setForeground(Color.BLACK);
				buttonArray[SUBMIT_BUTTON].setEnabled(true);
        	}
        	// Default setting for text field
        	public void focusLost(FocusEvent e) {
				if (name.getText().length() == 0) {
					name.setText("Enter a Player Name");
					name.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, FONT_SIZE * 2 / 3));
					name.setForeground(new Color(100, 100, 100));
					buttonArray[SUBMIT_BUTTON].setEnabled(false);
				}
			}
        });
    }
    
    // Post: Adds actionListeners to each button in the GetPlayersPanel frame
    public void createAllButtons() {
    	createSubmitButton();
    	createUndoButton();
    	createStartButton();
    	createLoadGameButton();
    	createInstructionButton();
        createSettingsButton();
        createHomeButton();
    	createCloseButton();
    }

    // Post: Adds a player with a first name given by the user to the ArrayList of Players.
    //       Players with the same name cannot be included. The minimum number of players
    //       required is the number of starters set by the user or by default (5).
    public void createSubmitButton() {
        this.buttonArray[SUBMIT_BUTTON].addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	// Search for a valid player name (First [Space] Last) using regular expressions
            	Pattern pattern = Pattern.compile(".+(\\s){1}.+");
                Matcher match = pattern.matcher(name.getText().trim());
                if (match.find()) {
                	String[] playerNameData = name.getText().trim().split(" ");
                	String firstName = playerNameData[0];
                	String lastName = playerNameData[1];
                	// Auto capitalize names
                	firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
                	lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
                	// Create the new Player object
                	String display = firstName + " " + lastName.charAt(0) + ".";
                	Player newPlayer = new Player(firstName, lastName, display);
                    if (!players.contains(newPlayer)) {
                        players.add(newPlayer);
                        name.setText(null);
                        int size = players.size() - 1;
                        // Check to see if the players display name is already used.
                        // If yes, then the last initial is replaced by the full last name.
                        Player check = players.get(size);
                        if (displayNames.contains(check.getDisplayName())) {
                        	Player player = check;
                        	String newName = player.getName() + ". " + player.getLastName();
                        	check.setDisplayName(newName);
                        }
                        displayNames.push(check.getDisplayName()); 
                        updatePlayerList();
                        buttonArray[UNDO_BUTTON].setEnabled(true);
                    } 
                }            
                if (players.size() >= (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) {
                	buttonArray[START_BUTTON].setEnabled(true);
                }
            }
        });
    }
    
    // Post: Removes the most recently added player from the 'labelList' and the 'players' list.
    public void createUndoButton() {
    	this.buttonArray[UNDO_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			int index = players.size() - 1;
		        displayNames.pop();
				players.remove(players.get(index));
				buttonArray[UNDO_BUTTON].setEnabled(!players.isEmpty());
				if (players.size() < (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) {
					buttonArray[START_BUTTON].setEnabled(false);
				}
				updatePlayerList();
    		}
    	});
    	setButtonKey(this.buttonArray[UNDO_BUTTON], UNDO_BUTTON_KEY, 0);
    }
    
    // Post: Allows the user to start tracking statistics for their players once
    //       enough players are on the team. If not enough players are in the players ArrayList,
    //       the user will be prompted to add more players.
    public void createStartButton() {
    	this.buttonArray[START_BUTTON].addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                AbstractAction button = new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {                 	
                        String result = "Boxscore_" + DAYNUM + "." + MONTH + "." + YEAR;
                        fileName = getFileName(result) + "." + FILETYPE;
                        List<Player> list = new ArrayList<Player>(players.size());
                        list.addAll(players);
                        Collections.sort(list);
                        run(list, new ArrayList<Undo>(), fileName, !IS_SCAN, SETTINGS);
                        pane.removeAll();
            			pane.add(mainPanel);
            			players.clear();
            			displayNames.clear();
            			updatePlayerList();
            			pane.revalidate();
            			pane.repaint();
            			frame.setTitle("Basketball Statistics Tracking");
            			frame.getRootPane().setDefaultButton(buttonArray[SUBMIT_BUTTON]);
            			buttonArray[START_BUTTON].setEnabled(false);
            			Color bckgrnd = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
            			SETTINGS = new GameSettings();
            			SETTINGS.setSetting(bckgrnd, Setting.BACKGROUND_COLOR);
                    }
                };
            	confirmPane(pane, frame, buttonArray[HOME_BUTTON].getActionListeners()[0], button, 
            			    "Confirm Team?", TEAM_ICON, SETTINGS);
            }
        });
    	setButtonKey(this.buttonArray[START_BUTTON], START_BUTTON_KEY, 0);
    }
    
    // Post: Returns a file name for a new game so that there are no duplicate file names.
    public static String getFileName(String name) {
    	File file = new File(FILE_PATH + "\\" + GAMEFILESFOLDER);
    	List<String> names = new ArrayList<String>();
    	boolean found = false;
    	for (File f : file.listFiles()) {
    		if (f.getName().contains(name)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		return name;
    	} else {
    		for (File f : file.listFiles()) {
        		String n = f.getName().trim();
        		if (n.contains(FILETYPE)) {
        			n = n.replace("." + FILETYPE, "");
        			char[] c = n.toCharArray();
        			int l = c.length;
        			if (c[l - 2] == '_') {
                		names.add(c[l - 1] + "");
        			} else {
        				names.add(null);
        			}
        		}
        	}
        	if (!names.contains(null)) {
        		return name;
        	} else {
        		for (int i = 1; i <= names.size(); i++) {
        			if (names.contains(i + "")) {
        				continue;
        			} else {
        				return name + "_" + i;
        			}
        		}
        	}
    	}    	
    	return null;
    }
    
    // Post: Opens the Instructions window when pressed.
    public void createInstructionButton() {
    	this.buttonArray[INSTRUCTIONS_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			pane.removeAll();
    			frame.setTitle("Instructions");
    			pane.setLayout(new GridLayout(1, 1));
    			instructionsPanel.setSettings(SETTINGS);
    			pane.add(instructionsPanel.addElements(buttonArray[HOME_BUTTON]));
    			pane.revalidate();
    			pane.repaint();
    		}
    	});
    	setButtonKey(this.buttonArray[INSTRUCTIONS_BUTTON], INSTRUCTIONS_BUTTON_KEY, 0);
    }
    
    public void createHomeButton() {
    	this.buttonArray[HOME_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			pane.removeAll();
    			pane.add(mainPanel);
    			pane.revalidate();
    			pane.repaint();
    			frame.setTitle("Basketball Statistics Tracking");
    			frame.getRootPane().setDefaultButton(buttonArray[SUBMIT_BUTTON]);
            	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		}
    	});
    	setButtonKey(this.buttonArray[HOME_BUTTON], HOME_BUTTON_KEY, 0);
    }
    
    // Post: The "Load Old Game" button allows the user to load any previous game they have.
    //       This feature was implemented in case the program were to close during a game. This way,
    //       the user could easily resume their tracking from where they left off.
    public void createLoadGameButton() {
    	this.buttonArray[OLDGAMES_BUTTON].addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
            	File actual = new File(FILE_PATH + "\\" + GAMEFILESFOLDER);
            	int countCopy = countFiles();
            	pane.removeAll();
            	frame.setTitle("Load Old Games");
            	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            	JLabel header = formatLabel("  Load Old Games", FONT_SIZE, SETTINGS);
            	setIcon(header, OLDGAMES_BUTTON_ICON);
            	pane.add(panelfy(header, background, new GridBagLayout(), 
            			 new MatteBorder(BORDER_SIZE * 2, 0, BORDER_SIZE * 2, 0, background)));
                // If there are no old games available, the user is notified.
                if (countCopy <= 0) {
                	pane.add(panelfy(formatTextPane("There are no Old Games Available", FONT_SIZE * 5 / 6, SETTINGS),
			                 background, new GridBagLayout(),
			                 new MatteBorder(BORDER_SIZE * 5, 0, BORDER_SIZE * 2, 0, background)));
                } else { // countCopy > 0
                    JPanel oldGamePanel = new JPanel(new GridLayout(countCopy, 1));
                    oldGamePanel.setBackground(background);
                    JScrollPane scrollPane = addScrollPane(oldGamePanel);
                    scrollPane.setBorder(new LineBorder(DEFAULT_TEXT_BORDER_COLOR, 5, ROUNDED_BORDERS));
                    JPanel panePanel = panelfy(scrollPane, background, new GridLayout(1, 1), null);
                    panePanel.setMaximumSize(new Dimension(SCREENWIDTH * 2 / 3, SCREENHEIGHT * 2 / 3));
                    panePanel.setPreferredSize(new Dimension(SCREENWIDTH * 2 / 3, countCopy * BUTTON_HEIGHT * 2));
                    pane.add(panePanel);
                    /*
                    * If there is at least one old game available to load, every available game is
                    * displayed for the user to choose from. When the user clicks on the game they want,
                    * the program will automatically start up with all the players and their statistics
                    * from the last time the user left off.
                    */
                    for (File f : actual.listFiles()){
                    	String name = f.getName();
                        if (name.contains(FILETYPE) && !name.contains(GAMEDATA_FILE_NAME)) {
                            // Cuts out the FILETYPE at the end of the file name.
                            JButton oldGameButton = new JButton("   " + name.substring(0, name.length() - (FILETYPE.length() + 1)));
                            formatButton(oldGameButton, (int) BUTTON_HEIGHT * 5 / 2, BUTTON_HEIGHT * 2, FONT_SIZE * 5 / 12, SETTINGS);
                            oldGamePanel.add(oldGameButton);
                            oldGameButton.setBorder(null);
                            ReadFile gameFile = new ReadFile(name, SETTINGS);
                            boolean checkFile = gameFile.checkFile();
                            boolean gameOver = gameFile.isGameOver();
                            if (!checkFile && !gameOver) {
                            	setIcon(new JButton[] {oldGameButton}, 0, FILE_WARNING_ICON);
                            } else if (checkFile && gameOver) {
                            	setIcon(new JButton[] {oldGameButton}, 0, GAME_OVER_ICON);
                            } else if (gameOver && !checkFile) {
                            	try {
                                	Image icon = new ImageIcon(this.getClass().getResource(GAMEOVER_WARNING_ICON)).getImage();
                                	Image newImage = icon.getScaledInstance(ICON_SIZE * 25 / 11, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);  
                                	oldGameButton.setIcon(new ImageIcon(newImage));
                            	} catch (Exception except) {
                            		System.out.println(GAMEOVER_WARNING_ICON + " is not in the directory.");
                            		System.out.println("Make sure to store all icons in a source folder called 'Image'.");
                            	}
                            }
                            oldGameButton.setEnabled(checkFile);
                            oldGameButton.addActionListener(new AbstractAction() {
                                // When the user clicks the game they want to load, the file is read.
                                @SuppressWarnings("unchecked")
								public void actionPerformed(ActionEvent e) {
                                	ReadFile gameFile = new ReadFile(name, SETTINGS);
                                	Object[] data = gameFile.getFileData();
                                    GameSettings settings = (GameSettings) data[0];
                                    run((List<Player>) data[1], (List<Undo>) data[2], name, IS_SCAN, settings);
                                }
                            });
                        }
                    }
                }
                pane.add(panelfy(buttonArray[HOME_BUTTON], background, new GridBagLayout(),
  		              new MatteBorder(BORDER_SIZE * 2, 0, BORDER_SIZE * 2, 0, background)));
                pane.revalidate();
                pane.repaint();
            }
        });
    	setButtonKey(this.buttonArray[OLDGAMES_BUTTON], OLDGAMES_BUTTON_KEY, 0);
    }
    
    // Post: Adds function to the 'Close' button. Closes the program entirely.
    public void createCloseButton() {
    	this.buttonArray[CLOSE_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			frame.dispose();
    			settingsFrame.dispose();
    			System.exit(1);
    		}
    	});
    }
    
    // Post: Adds function to the 'Settings' buttons. Allows the user to configure the game settings however 
    //       they like. Includes actual game settings (Quarters/Halves, Fouls allowed, etc.) and background color.
    public void createSettingsButton() {  	
    	this.buttonArray[SETTINGS_BUTTON].addActionListener(new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			buttonArray[SETTINGS_BUTTON].setEnabled(false);
    		    JPanel colorPanel = new JPanel(new GridLayout(COLOR_NAMES.length, 1));
    		    JScrollPane scrollPane = addScrollPane(colorPanel);
    		    for (int i = 0; i < COLOR_NAMES.length; i++) {
    		    	JButton btn = new JButton(COLOR_NAMES[i]);
    		    	formatButton(btn, BUTTON_HEIGHT, BUTTON_HEIGHT, FONT_SIZE * 5 / 12, SETTINGS);
    		    	btn.setBackground(COLORS[i]);
    		    	btn.addActionListener(new AbstractAction() {
    		    		public void actionPerformed(ActionEvent e) {
    		    			Color bck = btn.getBackground();
	    					SETTINGS.setSetting(bck, Setting.BACKGROUND_COLOR);
    		    			for (JPanel panel : panels) {
    		    				panel.setBackground(bck);
    		    				addBorderPanel(panel);
    		    			}
    		    			for (JButton btn : buttonArray) {
    		    				btn.setBackground(bck);
    		    			}
    		    			updatePlayerList();
    		    		}
    		    	});
    		    	colorPanel.add(btn);
    		    }
    		    JPanel scrollPanel = new JPanel(new GridLayout(1, 1));
    		    scrollPanel.add(scrollPane);
    		    
    		    JPanel firstRow = new JPanel(new GridLayout(1, 4));		    
    		    JPanel secondRow = new JPanel(new GridLayout(1, 4));  		  
    		    JPanel thirdRow = new JPanel(new GridLayout(1, 4));  		    
    		    JPanel total = new JPanel(new GridLayout(3, 1));
    		    total.add(firstRow);
    		    total.add(secondRow);
    		    total.add(thirdRow);
    		    int fontSize = FONT_SIZE / 3;
    		    createRadioButtons(null, null, new ButtonGroup(), getButtonOptions(1, MAX_STARTERS), 
    		    		           firstRow, "Starters", Setting.NUMBER_OF_STARTERS, fontSize);
    		    JPanel timeouts = new JPanel(new GridLayout(1, 2));
    		    createRadioButtons(null, null, new ButtonGroup(), getButtonOptions(1, MAX_TIMEOUTS),
	    		                   timeouts, "Regulation", Setting.TIMEOUTS, fontSize);
    		    createRadioButtons(null, null, new ButtonGroup(), getButtonOptions(1, 4),
		                           timeouts, "Overtime", Setting.OT_TIMEOUTS, fontSize);
    		    setRadioButtonBorder(timeouts, firstRow, "Timeouts", fontSize + 5);
    		    JPanel fouls = new JPanel(new GridLayout(1, 4));
    		    createRadioButtons(null, null, new ButtonGroup(), getButtonOptions(4, MAX_PERSONAL_FOULS), 
	    		                   fouls, "Personal", Setting.PERSONAL_FOULS, fontSize);   	
	            createRadioButtons(null, null, new ButtonGroup(), getButtonOptions(1, MAX_TECHNICAL_FOULS), 
	    		                   fouls, "Technical", Setting.TECHNICAL_FOULS, fontSize);
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"1", "2", "3", "4"},
    		    				   fouls, "Flagrant I", Setting.FLAGRANT_I, fontSize);	
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"1", "2", "3", "4"},
	    				           fouls, "Flagrant II", Setting.FLAGRANT_II, fontSize);
    		    setRadioButtonBorder(fouls, secondRow, "Fouls Allowed", fontSize + 5);
    		    ButtonGroup hO = new ButtonGroup();
    		    ButtonGroup qO = new ButtonGroup();
    		    JPanel timings = new JPanel(new GridLayout(1, 4));
    		    createRadioButtons(qO, hO, new ButtonGroup(), new String[] {"Quarters", "Halves"}, 
    		    		           timings, "Game Type", Setting.PERIOD_TYPE, fontSize);   	
    		    createRadioButtons(null, null, qO, new String[] {"6 mins", "10 mins", "12 mins", "15 mins"}, 
    		    		           timings, "Quarter Length", Setting.GAME_LENGTH, fontSize);
    		    createRadioButtons(null, null, hO, new String[] {"10 mins", "20 mins", "25 mins", "30 mins"}, 
    		    		           timings, "Half Length", Setting.GAME_LENGTH, fontSize);
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"1 min", "2 mins", "3 mins", "5 mins"},
    		    		           timings, "Overtime Length", Setting.OVERTIME_LENGTH, fontSize);
    		    setRadioButtonBorder(timings, thirdRow, "Game Timings", fontSize + 5);
    		    enableButtons(hO, false);	    
    		 		    
    		    JPanel settingsButtons = new JPanel(new GridLayout(1, 2));
    		    JButton close = new JButton("Apply");
    		    formatButton(close, BUTTON_HEIGHT, BUTTON_HEIGHT / 3, FONT_SIZE * 2 / 5, SETTINGS);
    		    close.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    close.addActionListener(new AbstractAction() {
    		    	public void actionPerformed(ActionEvent e) {
    		    		settingsFrame.dispose();
    		    		updatePlayerList();
    		    	}
    		    });
    		    
    		    JButton defaultBtn = new JButton("Set Default");
    		    formatButton(defaultBtn, BUTTON_HEIGHT, BUTTON_HEIGHT / 3, FONT_SIZE * 2 / 5, SETTINGS);
    		    defaultBtn.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    defaultBtn.addActionListener(new AbstractAction() {
    		    	public void actionPerformed(ActionEvent e) {
    		    		SETTINGS = new GameSettings();
    		    		for (JPanel p : panels) {
    		    			p.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    			addBorderPanel(p);
    		    		}
    		    		for (JButton btn : buttonArray) {
    		    			btn.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    			btn.setForeground(DEFAULT_FONT_COLOR);
    		    		}
    		    		updatePlayerList();
    		    		settingsFrame.dispose();
    		    	}
    		    });   		    
    		    settingsButtons.add(close);
    		    settingsButtons.add(defaultBtn);
    		    
    		    JScrollPane totalScrollPane = addScrollPane(total);
    		    totalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    		    JPanel totalScrollPanel = new JPanel(new GridLayout(1, 1));
    		    totalScrollPanel.add(totalScrollPane);  
    		    
    		    JSplitPane pane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPanel, settingsButtons);
    		    pane3.setDividerLocation(SCREENHEIGHT / 2 + BUTTON_HEIGHT * 3);
    		    pane3.setEnabled(false);   
    		    
    		    JSplitPane pane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, total, pane3);
    		    pane1.setDividerLocation(SCREENWIDTH / 2 + BUTTON_HEIGHT * 5 / 2);
    		    pane1.setEnabled(false);
    		    
    		    JPanel panels = new JPanel();
    		    panels.add(pane1);
    		    panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));
    		    settingsFrame.setVisible(true);
    		    settingsFrame.add(panels);
    		    settingsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
    		    settingsFrame.addWindowListener(new WindowAdapter() {
    		        @Override
    		        public void windowClosing(WindowEvent windowEvent) {
    		            buttonArray[SETTINGS_BUTTON].setEnabled(true);
    		        }
    		        public void windowClosed(WindowEvent w) {
    		            buttonArray[SETTINGS_BUTTON].setEnabled(true);
    		        }
    		    });
    		}
    	});
    	//this.settingsFrame.getRootPane().setDefaultButton(close);
    	setButtonKey(this.buttonArray[SETTINGS_BUTTON], SETTINGS_BUTTON_KEY, 0);
    }
    
    // Post: Returns a String of numbers in the range of 'min' to 'max'.
    private String[] getButtonOptions(int min, int max) {
    	String[] options = new String[max - min + 1];
    	for (int i = min; i <= max; i++) {
    		options[i - min] = i + "";
    	}
    	return options;
    }
    
    // Post: Updates the list of players at the bottom of the GetPlayersPanel every time a player name
    //       is added or removed.
    private void updatePlayerList() {
    	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	for (int i = this.displayNames.size() - 1; i >= 0; i--) {
    		JLabel label = formatLabel(" - " + this.displayNames.get(i), FONT_SIZE / 2, SETTINGS);
    		label.setBackground(DEFAULT_BACKGROUND_COLOR);
    		label.setBorder(new MatteBorder(BORDER_SIZE * 3 / 2, BORDER_SIZE, BORDER_SIZE * 3 / 2, BORDER_SIZE, background));
    		label.setHorizontalTextPosition(JLabel.CENTER);
            panel.add(label);
    	}
    	panel.setBackground(background);
    	this.panels.add(panel);
    	JScrollPane pane = addScrollPane(panel);
    	// Hide scroll bar
    	JScrollBar vertical = pane.getVerticalScrollBar();
    	vertical.setPreferredSize(new Dimension(0, 0));
        pane.setPreferredSize(new Dimension(SCREENWIDTH / 6, SCREENHEIGHT / 4));
        pane.setBackground(background);
    	this.panels.add(panelfy(pane, background, new GridLayout(1, 1), null));
    	this.playerNames.setLayout(new BoxLayout(this.playerNames, BoxLayout.Y_AXIS));
    	this.playerNames.removeAll();
    	JLabel players = formatLabel("   Players: " + this.players.size(), FONT_SIZE * 4 / 7, SETTINGS);
    	players.setBorder(new MatteBorder(0, 0, BORDER_SIZE * 3, 0, background));
    	setIcon(players, PLAYER_ICON);
    	this.playerNames.setBorder(new MatteBorder(0, BORDER_SIZE * 6, BORDER_SIZE * 3, 0, background));
    	this.playerNames.add(players);
    	this.playerNames.add(pane);
    	this.playerNames.setBackground(background);
    	this.playerNames.repaint();
    	this.playerNames.revalidate();
    	this.panels.add(this.playerNames);
    }
    
    // Post: Re-Colors the border of the 'panel' given
    private void addBorderPanel(JPanel panel) {
		if (panel.getBorder() != null) {
			String text = panel.getBorder().getBorderInsets(panel).toString().substring(15);
			String[] bordNums = text.replaceAll("[a-z]+=", "").replaceAll("\\[", "").replaceAll("\\]", "").split(",");
			int[] border = new int[bordNums.length];
			for (int i = 0; i < bordNums.length; i++) {
				border[i] = Integer.valueOf(bordNums[i]);
			}
			panel.setBorder(new MatteBorder(border[0], border[1], border[2], border[3], 
					        (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR)));
		}
    }
    
    // Post: Creates a Group of Radio Buttons
    // Parameters:
    // 'first', 'second': Used for Quarters/Halves. Dis/enables those ButtonGroups.
    // 'total': The ButtonGroup used to Group the given Radio Buttons.
    // 'names': The text used for each Radio Button.
    // 'panel': The panel the Radio Buttons will be added to.
    // 'borderText': The text that appears in the border of the Radio Buttons.
    // 'setting': The settings for the game.
    // 'fontSize': The font size of the 'borderText'.
    private void createRadioButtons(ButtonGroup first, ButtonGroup second, ButtonGroup total, String[] names, 
    		                        JPanel panel, String borderText, Setting gameSetting, int fontSize) {
    	JPanel buttonPanel = new JPanel(new GridLayout(names.length, 1));
	    for (int i = 0; i < names.length; i++) {
	    	JRadioButton select = new JRadioButton(names[i]);
	    	select.setFont(new Font(DEFAULT_FONT_TYPE, Font.PLAIN, fontSize)); 		    	
	    	buttonPanel.add(select);
	    	total.add(select);
	    	// Pre-select default options in settings
	    	char selectText = names[i].charAt(0);
	    	switch (gameSetting) {
			case PERIOD_TYPE:
				char period;
				if ((boolean) SETTINGS.getSetting(gameSetting)) {
					period = 'Q';
				} else {
					period = 'H';
				}
				select.setSelected(selectText == period);
				break;
			case GAME_LENGTH:
			case OVERTIME_LENGTH:
				select.setSelected(Integer.valueOf(names[i].substring(0, 2).trim()) == (double) SETTINGS.getSetting(gameSetting));
				break;
			default:
				select.setSelected(Integer.valueOf(selectText + "") == (int) SETTINGS.getSetting(gameSetting));
				break;
			}
	    	select.addActionListener(new AbstractAction() {
	    		public void actionPerformed(ActionEvent e) {
	    			for (int i = 0; i < names.length; i++) {
	    				if (select.getText().equals(names[i])) {
	    					int value;
	    					try {
		    					value = Integer.valueOf(names[i].replace("mins", "").replace("min", "").trim());
	    					} catch (NumberFormatException nfe) {
	    						value = -1;
	    					}
	    					int selection = i + 1;
	    					switch (gameSetting) {
	    					case PERSONAL_FOULS:
	        					SETTINGS.setSetting(value, gameSetting);
	    						break;
	    					case NUMBER_OF_STARTERS:
	        					buttonArray[START_BUTTON].setEnabled(players.size() >= selection);
	    					case TECHNICAL_FOULS:
	    					case FLAGRANT_I:
	    					case FLAGRANT_II:
	    					case OT_TIMEOUTS:
	    					case TIMEOUTS:
	        					SETTINGS.setSetting(selection, gameSetting);
	        					break;
	    					case PERIOD_TYPE:
		        		    	enableButtons(first, selection % 2 != 0);
		        		    	enableButtons(second, selection % 2 == 0);
	        					SETTINGS.setSetting(selection % 2 != 0, gameSetting);
		    					break;
	    					case GAME_LENGTH:
	        					SETTINGS.setSetting((double) value, Setting.GAME_LENGTH);
	        		    		SETTINGS.setSetting((double) value, Setting.TIME_REMAINING);
		    					break;
	    					case OVERTIME_LENGTH:
	    						SETTINGS.setSetting((double) value, gameSetting);
	    						break;
	    					default:
	    						break;
	    					}
	    					break;
	    				}
	    			}
	    		}
	    	});
	    }
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    setRadioButtonBorder(buttonPanel, panel, borderText, fontSize);
    }
    
    // Post: If 'enable' is true, the buttons in the ButtonGroup 'buttons' are all enabled.
    //       If 'enable' is false, the buttons in the ButtonGroup 'buttons' are all disabled.
    private void enableButtons(ButtonGroup buttons, boolean enable) {
    	Enumeration<AbstractButton> buttonGroup = buttons.getElements();
    	while (buttonGroup.hasMoreElements()) {
    		AbstractButton button = buttonGroup.nextElement();
    		button.setSelected(false);
    		button.setEnabled(enable);
    	}
    }
    
    // Post: Returns the number of .BBALL files in the directory.
    @SuppressWarnings("static-access")
	private int countFiles() {
    	File file = new File(super.FILE_PATH + "\\" + GAMEFILESFOLDER);
        int countCopy = 0;
        for (File f : file.listFiles()) {
            if (f.getName().contains(FILETYPE)) {
            	countCopy++;
            }
        }
        return countCopy - 1;
    }
    
    // Post: Adds a border to a ButtonGroup of Radio Buttons.
    // Parameters:
    // 'buttonPanel': The panel that contains the ButtonGroup. 
    // 'panel': The panel the ButtonGroup will be added to with the new border.
    // 'borderText': The text that appears in the title of the border.
    // 'fontSize': The font size of the text in the title of the border.
    private void setRadioButtonBorder(JPanel buttonPanel, JPanel panel, String borderText, int fontSize) {
	    Border line = BorderFactory.createMatteBorder(2, 2, 2, 2, DEFAULT_TEXT_BORDER_COLOR);
	    Border bord = BorderFactory.createTitledBorder(line, borderText);
	    ((TitledBorder) bord).setTitleJustification(TitledBorder.CENTER);
	    buttonPanel.setBorder(bord);
	    ((TitledBorder) buttonPanel.getBorder()).setTitleFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, fontSize));	    
	    if (panel != null) {
		    panel.add(buttonPanel);
	    }
    }
    
    // Post: Adds all the Components created to the GetPlayersPanel and formats the GetPlayersPanel.
    public void addElements() {
    	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
    	this.pane.setLayout(new FlowLayout());
        
        // The panel that stores the 'Add Player' and 'Undo' buttons.
        JPanel buttonArray = new JPanel();
        formatPanel(buttonArray, new Component[] {this.buttonArray[SUBMIT_BUTTON], this.buttonArray[UNDO_BUTTON]}, 
        		    null, buttonArray.getLayout(), background);
        this.panels.add(buttonArray);
        
        // The panel that stores the 'Old Games', 'Instructions', 'Settings', and 'Close' buttons.
        JPanel instructSettingPanel = new JPanel(new GridLayout(4, 1));
        formatPanel(instructSettingPanel, new Component[] {this.buttonArray[OLDGAMES_BUTTON],
        												   this.buttonArray[INSTRUCTIONS_BUTTON],
        												   this.buttonArray[SETTINGS_BUTTON],
        												   this.buttonArray[CLOSE_BUTTON]},
        			null, instructSettingPanel.getLayout(), background);
        this.panels.add(instructSettingPanel);
        
        // The panel that frames the lower portion of the GetPlayersPanel that includes the four buttons
        // and the list of players that is updated as players are entered.
        JPanel[] frameComponents = {panelfy(instructSettingPanel, background, null, null), this.playerNames};
        this.panels.addAll(Arrays.asList(frameComponents));
        JPanel frame = new JPanel();
        formatPanel(frame, frameComponents, 
        		    null, new FlowLayout(), background);
        this.panels.add(frame);

        // All the elements in the GetPlayersPanel.
        JPanel[] totalComponents = {panelfy(formatLabel("Basketball Statistics Tracking", FONT_SIZE, SETTINGS), background,
                                    null, new MatteBorder(BORDER_SIZE * 3, 0, BORDER_SIZE * 4, 0, background)), 
        		                    panelfy(this.name, background, null, null), 
        		                    buttonArray,  
        		               panelfy(this.buttonArray[START_BUTTON], background, null, null)};
        this.panels.addAll(Arrays.asList(totalComponents));
        JPanel total = new JPanel();
        formatPanel(total, totalComponents, new MatteBorder(BORDER_SIZE * 2, BORDER_SIZE * 6, BORDER_SIZE * 6, BORDER_SIZE * 6, background), 
        		    null, background);
        total.add(frame, BorderLayout.SOUTH);
        this.panels.add(total);
        this.mainPanel = total;
        this.pane.add(this.mainPanel);
        try {
            this.frame.getRootPane().setDefaultButton(this.buttonArray[SUBMIT_BUTTON]);
        } catch (NullPointerException nullE) {}
    }
    
    // Post: Puts all the Components of the GetPlayersPanel into a window that opens when the user starts the program.
    public void frame() {
        this.frame.add(this.pane);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.getRootPane().setDefaultButton(this.buttonArray[SUBMIT_BUTTON]);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setVisible(true);
    }
    
    // Post: Returns a JPanel with all the components of a GetPlayersPanel. Used in the InstructionsPanel.
    public JPanel getPlayersPanel() {
    	JPanel panel = this.pane;
    	panel.setPreferredSize(new Dimension(SCREENWIDTH - (BUTTON_HEIGHT * 5 / 2), (int) getDimension(this.pane).getHeight()));
		panel.setBorder(new MatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, DEFAULT_TEXT_BORDER_COLOR));
    	return panel;
    }
}
