// Brian Nzomo
// Basketball Statistics Tracker Program
// InstructionPanel Class
// Opens a window that shows the documentation of the program in a user friendly way.
// Paragraphs accompany sample windows that are user interactive and allow the user
// to understand how to use the program before they begin tracking their own game.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class InstructionPanel extends GUISettings {

	private static final long serialVersionUID = 1L;
	
	// Indices of buttons in the 'buttonArray'
	private static final int ABBREVIATIONS_BUTTON = 0;
		
	private static final String PARAGRAPH_SPLITTER = "*-*";
	// Name of the text file that contains the game data for the simulated games
	private static final String STARTER_KEY = "STARTER_KEY";
	// Used to convert numbers to letters for the sample players used
	private static final String[] NUMBER_NAMES = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
			                                      "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen"};
	
	private JFrame abbFrame; // The frame that opens when the 'Abbreviations' button is pressed
	private JFrame explainFrame; // The frame that opens when a statistic in the 'abbFrame' is pressed
    private JButton[] buttonArray; // All the buttons on the InstructionPanel
    private List<JTextArea> textFields; // All paragraphs used for the explanation/documentation text
    private GameSettings SETTINGS; // The settings used to configure the simulated games and the InstructionPanel
    
    // Post: Constructs a InstructionPanel with the given 'settings'.
    public InstructionPanel(GameSettings settings) {
    	this.SETTINGS = settings;
    	initalizeButtons();
		this.abbFrame = new JFrame("Abbreviations Explanations");
		this.abbFrame.setResizable(false);
		this.explainFrame = new JFrame("Explanation");
		this.explainFrame.setResizable(false);
		this.textFields = new ArrayList<JTextArea>();
		scanFile();
    }
    
    private void initalizeButtons() {
		String[] buttonNames = {" Abbreviations"};
		int[] sizes = {FONT_SIZE * 2 / 3};
		this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
		addButtonFunctions();
		int[] indices = {ABBREVIATIONS_BUTTON};
		String[] icons = {ABBREVIATIONS_BUTTON_ICON};
		formatIcons(this.buttonArray, indices, icons);
    }
    
    // Post: Changes the current settings to the 'settings' parameter.
    public void setSettings(GameSettings settings) {
    	this.SETTINGS = settings;
    }
    
    // Post: Scans the Instructions file with all the text that appears in the InstructionPanel.
    @SuppressWarnings("static-access")
	private void scanFile() {
    	Scanner scan = null;
    	Scanner lineScan = null;
		try {
			File file = new File(super.FILE_PATH + "\\" + INSTRUCTIONS_FOLDER + "\\" + TEXT_FILE_NAME);
			scan = new Scanner(file);
			lineScan = new Scanner(file);
			// Count the number of paragraphs separated by the PARAGRAPH_SPLITTER
	    	int textAreas = 0;
	    	int totalLines = 0;
			while (lineScan.hasNextLine()) {
				totalLines++;
				if (lineScan.nextLine().contains(PARAGRAPH_SPLITTER)) {
					textAreas++;
				}
			}
			// Format each text area for each paragraph
			for (int i = 0; i < textAreas; i++) {
				JTextArea area = formatTextArea(SCREENWIDTH - 400, BUTTON_HEIGHT * 2, SETTINGS);
				area.setFont(new Font(DEFAULT_FONT_TYPE, Font.PLAIN, FONT_SIZE * 4 / 7));
				this.textFields.add(area);
			}
			// Scan in the text from the input file
			List<String> fileLines = new ArrayList<String>(totalLines);
			while (scan.hasNextLine()) {
				fileLines.add(scan.nextLine());
			}
			List<String> text = new ArrayList<String>();
			String paragraph = "";
			for (String line : fileLines) {
				if (!line.contains(PARAGRAPH_SPLITTER)) {
					paragraph += (line + " ");
				} else {
					paragraph = paragraph.replaceAll(STARTER_KEY, (int) (SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) + "");
					text.add(paragraph);
					paragraph = "";
				}
			}	
			// Set the text of each text area in the InstructionsPanel
			int index = 0;
			for (JTextArea area : this.textFields) {
				area.setText(text.get(index));
				index++;
			}
		} catch (FileNotFoundException e) {
			System.out.println(TEXT_FILE_NAME + " was not found");
		}
    }
    
    // Post: Adds functions to each button in the 'buttonArray'.
    private void addButtonFunctions() {
        // Abbreviations button shows the user a list of all abbreviations used for statistics in
        // basketball and allows the user to press a button to learn more about that statistic
        this.buttonArray[ABBREVIATIONS_BUTTON].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Color secondaryColor = DEFAULT_BACKGROUND_COLOR;
        		Color textBorderColor = DEFAULT_TEXT_BORDER_COLOR;
        		Border border = new MatteBorder(5, 5, 5, 5, textBorderColor);
        		// STATISTIC ABBREVIATIONS COLUMN
        		// ****************************************************************************************
        		JLabel labelColumn = formatLabel("Statistic Name", 30, SETTINGS);
        		JPanel abbreviations = panelfy(labelColumn, null, new GridLayout(STATISTIC_ABBREVIATIONS.length + 1, 1), null);
        		labelColumn.setBackground(secondaryColor);
    			labelColumn.setBorder(border);
    			labelColumn.setOpaque(true);
        		// Add all abbreviated statistic names to the abbreviations column
        		for (String s : STATISTIC_ABBREVIATIONS) {
        			JLabel label = formatLabel(s, 25, SETTINGS);
        			label.setBackground(secondaryColor);
        			label.setOpaque(true);
        			label.setBorder(border);
        			abbreviations.add(label);
        		}
        		// STATISTIC EXPLANATIONS COLUMN
        		// ****************************************************************************************
        		JLabel descriptionColumn = formatLabel("Explanation", FONT_SIZE / 2, SETTINGS);
        		JPanel fullStatWords = panelfy(descriptionColumn, null, new GridLayout(STATISTIC_ENTIRE_WORDS.length + 1, 1), null);
        		descriptionColumn.setBackground(secondaryColor);
    			descriptionColumn.setBorder(border);
    			descriptionColumn.setOpaque(true);
				scanExplanationsFile(fullStatWords);
				
        		JPanel total = new JPanel();
        		formatPanel(total, new Component[] {abbreviations, fullStatWords}, null, new GridLayout(1, 2), null);
        		JScrollPane abbScrollPane = addScrollPane(total);
        		abbScrollPane.setPreferredSize(new Dimension(SCREENWIDTH / 2, abbFrame.getHeight()));
        		formatFrame(abbFrame, abbScrollPane, SCREENWIDTH / 2, 800);
        	}
        });
    }
    
    // Post: Adds a JButton with the full-length name of the statistic that corresponds to the abbreviated
    //       statistic name on the other column. 
    //       Adds an actionListener to the JButton that opens a new frame with a detailed explanation of 
    //       what that statistic is.
    @SuppressWarnings("static-access")
	private void scanExplanationsFile(JPanel panel) {
    	Scanner scanner = null;
		List<String> data = new ArrayList<String>(STATISTIC_ENTIRE_WORDS.length * 2);
		try {
			scanner = new Scanner(new File(super.FILE_PATH + "\\" + INSTRUCTIONS_FOLDER + "\\" + STAT_EXPLANATIONS_FILE));
			while (scanner.hasNextLine()) {
				data.add(scanner.nextLine());
			}
			// Add a JButton for each entire statistic word
			for (String fullStatWord : STATISTIC_ENTIRE_WORDS) {
				JButton label = new JButton(fullStatWord);
				formatButton(label, BUTTON_HEIGHT * 4, BUTTON_HEIGHT, FONT_SIZE * 5 / 12, SETTINGS);
				// When the button is pressed, a new window opens with a explanation from the 'data' list
				label.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int index = data.indexOf(label.getText());
						String explanationText = data.get(index + 1);
						JPanel explainPanel = new JPanel(new GridLayout(2, 1));
						JLabel explainLabel = formatLabel(data.get(index), FONT_SIZE * 4 / 7, SETTINGS);
						explainLabel.setOpaque(true);
						explainLabel.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR));
						explainPanel.add(explainLabel);
						JTextPane explain = formatTextPane(explanationText, FONT_SIZE / 3, SETTINGS);
						explainPanel.add(explain);
						formatFrame(explainFrame, explainPanel, SCREENWIDTH / 3, SCREENHEIGHT * 3 / 5);
					}
				});
				panel.add(label);
			}
		} catch (FileNotFoundException e1) {
			System.out.println(STAT_EXPLANATIONS_FILE + " does not exist.");
		}
    }
    
    // Indices of paragraphs in 'textFields'
    private static final int PARAGRAPH1 = 0;
    private static final int PARAGRAPH2 = 1;
    private static final int PARAGRAPH3 = 2;
    private static final int PARAGRAPH4 = 3;
    private static final int PARAGRAPH5 = 4;
    private static final int PARAGRAPH6 = 5;
    private static final int PARAGRAPH7 = 6;    
    private static final int PARAGRAPH8 = 7;
    private static final int MAX_SAMPLE_PLAYERS = 10;
    
    // Post: Adds all the Components in the Instructions Panel into one Frame
    public JPanel addElements(JButton homeButton) {
    	int numberOfStarters = (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS);
    	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);

    	JPanel[] textPanels = new JPanel[this.textFields.size()];
		for (int i = 0; i < textPanels.length; i++) {
			JPanel textPanel = new JPanel();
			textPanel.add(this.textFields.get(i));
			this.textFields.get(i).setBackground(background);
			textPanel.setBorder(new MatteBorder(30, 30, 30, 30, background));
	        textPanel.setBackground(background);
	        textPanels[i] = textPanel;
		}	
    	
		List<Player> samplePlayers = new ArrayList<Player>(MAX_SAMPLE_PLAYERS);
		// Add players to 'samplePlayers'
		for (int i = 1; i <= numberOfStarters; i++) {
			Player player = new Player("Player", NUMBER_NAMES[i - 1], "Player " + i + ".");
			player.setOnFloor();
			samplePlayers.add(player);
		}
        
		// Panel that stores the 'Main Menu' button
        JPanel menuPanel = panelfy(homeButton, background, null, null);
        
        // Panel that stores the 'Abbreviations' button
        JPanel abbreviate = panelfy(this.buttonArray[ABBREVIATIONS_BUTTON], background, null, null);
        this.buttonArray[ABBREVIATIONS_BUTTON].setBackground(background);

        // List of sample players that start on the bench
		List<Player> bench = new ArrayList<Player>();
		// The first ManagementPanel window showing what a ManagementPanel window looks like when the number of players
		// entered equals the number of starters selected by the user.
		// In this case, the bench is empty
        ManagementPanel mPanel = new ManagementPanel(samplePlayers, bench, new ArrayList<Undo>(), GAMEDATA_FILE_NAME, SETTINGS);
        mPanel.createTeamButtons();
        mPanel.createTableButton();
        mPanel.createStartStopButton();
        mPanel.createTimeoutButton();
        mPanel.createUndoButton();
        mPanel.addElements();
        
        // The panel that stores the first Management Panel constructed above ^
        JPanel frameMP = panelfy(mPanel.getManagementPanel(), background, null, null);
        
        // The bench is filled up with sample players numbered 6-10
        for (int i = numberOfStarters + 1; i <= MAX_SAMPLE_PLAYERS; i++) {
        	Player player = new Player("Player", NUMBER_NAMES[i - 1], "Player " + i + ".");
        	player.setOffFloor();
        	bench.add(player);
        }
        
        // For instructional purposes, the 6th player (first player off the bench) is fouled out by default to show the 
        // user what a fouled out player looks like
        bench.get(0).setStat("TF", (int) SETTINGS.getSetting(Setting.TECHNICAL_FOULS) + "");
        
        // The second version of the ManagementPanel showing what a ManagementPanel looks like when more players than starters
        // are entered
        ManagementPanel mPanelBench = new ManagementPanel(samplePlayers, bench, new ArrayList<Undo>(), GAMEDATA_FILE_NAME, SETTINGS);
        mPanelBench.createTableButton();
        mPanelBench.createUndoButton();
        mPanelBench.createPlayerButtons();
        mPanelBench.createTimeoutButton();
        mPanelBench.createStartStopButton();
        mPanelBench.addElements();      
        
        // The panel that stores the ManagementPanel constructed above ^
        // MPB (Management Panel w/ Bench)
        JPanel frameMPB = panelfy(mPanelBench.getManagementPanel(), background, null, null);
        
        // The GetPlayersPanel. Appears when the program is opened. Serves as a 'Main Menu'
        GetPlayersPanel gPPanel = new GetPlayersPanel(SETTINGS);
        gPPanel.createTextField();
        gPPanel.createSubmitButton();
        gPPanel.createUndoButton();
        gPPanel.addElements();
        
        // The panel that stores the GetPlayersPanel constructed above ^
        JPanel frameGPPanel = panelfy(gPPanel.getPlayersPanel(), background, null, null);
        
        // Puts all players into one list
        List<Player> totalSamplePlayers = new ArrayList<Player>(samplePlayers.size() + bench.size());
        totalSamplePlayers.addAll(samplePlayers);
        totalSamplePlayers.addAll(bench);
        
        // The StartersPanel that appears when more players are entered than starters
        StartersPanel startersPanel = new StartersPanel(totalSamplePlayers, GAMEDATA_FILE_NAME, SETTINGS);
        startersPanel.createPlayerButtons();
        startersPanel.createUndoButton();
        startersPanel.addElements();
        
        // The panel that stores the StartersPanel constructed above ^
        JPanel frameSP = panelfy(startersPanel.getStartersPanel(), background, null, null);
        
        // The panel that stores the header for the InstructionsPanel
		JPanel header = new JPanel();
		JLabel label = formatLabel("  Instructions", FONT_SIZE * 10 / 9, SETTINGS);
		setIcon(label, INSTRUCTIONS_BUTTON_ICON);
		header.add(label);
		header.setBackground(background);
		
		// The panel that stores all panels in the InstructionsPanel
		JPanel panels = new JPanel();
		// All elements in 'panels' in descending order (BoxLayout.Y_AXIS used)
		Component[] components = {header, textPanels[PARAGRAPH1], frameGPPanel, textPanels[PARAGRAPH2], frameSP,
						          textPanels[PARAGRAPH3], frameMP, textPanels[PARAGRAPH4], abbreviate, 
						          textPanels[PARAGRAPH5], frameMPB, textPanels[PARAGRAPH6], 
						          textPanels[PARAGRAPH7], textPanels[PARAGRAPH8], menuPanel, textPanels[PARAGRAPH8]};
		formatPanel(panels, components, null, null, background);
        
		// Add a scroll bar for the InstructionsPanel     
        return panelfy(addScrollPane(panels), background, new GridLayout(1, 1), null);
    }
}
