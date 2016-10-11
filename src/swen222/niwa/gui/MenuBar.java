package swen222.niwa.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import swen222.niwa.Client;


/**
 * 
 * The menu bar for the application window. 
 * Has 'New Game', 'Save', and 'Load' options.
 * @author Jack U
 *
 */
public class MenuBar extends JMenuBar{

	final Client control;
	
	final JMenu gameMenu;
	
	
	final JMenuItem newGame;
	final JMenuItem save;
	final JMenuItem load;
	
	public MenuBar(Client control){
		this.control=control;
		
		gameMenu = new JMenu("Game");
		
		newGame = new JMenuItem("New Game");
		save = new JMenuItem("Save");
		load = new JMenuItem("Load");
		
		newGame.addActionListener(control);
		save.addActionListener(control);
		load.addActionListener(control);
		
		gameMenu.add(newGame);
		gameMenu.add(save);
		gameMenu.add(load);
		
		add(gameMenu);
		
		setVisible(true); //make sure we are visible!
	}

}
