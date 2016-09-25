package swen222.niwa.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import swen222.niwa.Controller;


/**
 * 
 * The menu bar for the application window. 
 * Has 'New Game', 'Save', and 'Load' options.
 * @author Jack U
 *
 */
public class MenuBar extends JMenuBar implements ActionListener {

	final Controller control;
	
	final JMenu gameMenu;
	
	
	final JMenuItem newGame;
	final JMenuItem save;
	final JMenuItem load;
	
	public MenuBar(Controller control){
		this.control=control;
		
		gameMenu = new JMenu("Game");
		
		newGame = new JMenuItem("New Game");
		save = new JMenuItem("Save");
		load = new JMenuItem("Load");
		
		newGame.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);
		
		gameMenu.add(newGame);
		gameMenu.add(save);
		gameMenu.add(load);
		
		add(gameMenu);
		
		setVisible(true); //make sure we are visible!
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
