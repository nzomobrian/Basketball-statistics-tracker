// Brian Nzomo
// Basketball Statistics Tracker Program
// Table Class
// Opens a window with a table showing all player statistics in the classic
// Box Score format used in the NBA.

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class Table extends GUISettings {
    
	private static final long serialVersionUID = 1L;
	private GameSettings SETTINGS;			// Settings used to format the table
	private JScrollPane scrollPanel; 		// Scrollbar used for the table
	private JTable boxscore;				// Table with all information
    
	// Post: Constructs a new table according to the parameters given.
	// Parameters:
	// 'players': The list of players to be displayed in the table
	// 'data': The data for each cell of the table
	// 'width', 'height': Dimensions of the table
	// 'fontSize', 'headerFontSize': Font sizes
	// 'settings': Settings used to format the fonts and colors used in the table
    public Table(List<Player> players, String[][] data, int fontSize, int headerFontSize, GameSettings settings) {
        
        this.SETTINGS = settings;
        
        // Creates a new ArrayList of the existing players in order to accommodate for the
        // 'total' player which represents the totals for each statistic.
        List<Player> newPlayers = new ArrayList<Player>(players.size() + 1);
        newPlayers.addAll(players);
        
        if (players.size() > 1) {
        	// Adds a row at the bottom of the table representing the totals for each statistical category.
            Player total = new Player("Total");
            int[] totalStats = new int[STATISTIC_ABBREVIATIONS.length];
            for (Player player : players) {
            	int index = 0;
            	for (String stat : STATISTIC_ABBREVIATIONS) {
            		if (!(stat.equals("FG%") || stat.equals("3P%") || stat.equals("FT%") || stat.equals("Player"))) {
            			totalStats[index] += Integer.valueOf(player.getStat(stat + "*"));
            		}
            		index++;
            	}
            }
            for (int i = 1; i < totalStats.length; i++) {
            	total.setStat(STATISTIC_ABBREVIATIONS[i], String.valueOf(totalStats[i]));
            }
    		total.setStat(STATISTIC_ABBREVIATIONS[0], "Total");
            newPlayers.add(total);
            for (int column = 0; column < STATISTIC_ABBREVIATIONS.length; column++) {
            	data[newPlayers.size() - 1][column] = total.getStat(STATISTIC_ABBREVIATIONS[column]).replaceAll("_", " ");
            }
        }
                            
        this.boxscore = new JTable(data, STATISTIC_ABBREVIATIONS) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int data, int STATISTIC_ABBREVIATIONS) {
                return false;
            }
            public Component prepareRenderer(TableCellRenderer r, int data, int STATISTIC_ABBREVIATIONS) {
                Component c = super.prepareRenderer(r, data, STATISTIC_ABBREVIATIONS);
                //Change color of cell when selected
                if (isCellSelected(data, STATISTIC_ABBREVIATIONS)) {
                    c.setBackground(((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR) != Color.WHITE) ? 
                    		        (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR): Color.CYAN);
                } else {
                	c.setBackground(Color.WHITE);
                }  
                return c;
            }
        };
        //Get longest player name
        String longest = "";
        int max = 0;
        for (Player player : newPlayers) {
            if (player.getName().length() > max) {
                max = player.getName().length();
            }
        }
        for (Player player : newPlayers) {
        	if (player.getName().length() == max) {
        		longest = player.getName();
        		break;
        	}
        }
        int maxWidth = (int) getDimension(formatLabel(longest, fontSize, settings)).getWidth(); 
        
        JTableHeader header = this.boxscore.getTableHeader();
        header.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR));
        
        this.scrollPanel = new JScrollPane(this.boxscore);
        this.scrollPanel.getVerticalScrollBar().setUnitIncrement(6);
        this.scrollPanel.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR)); 
        
        int size;
        if (players.size() > 1) {
        	size = Math.min(SCREENHEIGHT / newPlayers.size(), FONT_SIZE * 20 / 3);
        } else {
        	size = SCREENHEIGHT * 3 / 4;
        }
        // Format Table
        this.boxscore.setFont(new Font(DEFAULT_FONT_TYPE, Font.PLAIN, fontSize));
        this.boxscore.setRowHeight(size);
        this.boxscore.setRowMargin(20);
        this.boxscore.getColumnModel().getColumn(0).setPreferredWidth(Math.max(maxWidth, FONT_SIZE * 10 / 3));
        this.boxscore.getTableHeader().setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, headerFontSize));
        
        // Center Data in cell
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for(int i = 0; i < STATISTIC_ABBREVIATIONS.length; i++) {
        	this.boxscore.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        this.boxscore.setEnabled(false);
        this.boxscore.setShowGrid(players.size() > 1);
    }
    
    // Post: Returns the JTable in a JPanel.
    public JPanel getTable() {
    	JPanel table = new JPanel(new GridLayout(1, 1));
    	table.add(this.scrollPanel);
     	table.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR));
    	return table;
    }
    
    // Post: Returns the JTable.
    public JTable getBoxScore() {
    	return this.boxscore;
    }
}
