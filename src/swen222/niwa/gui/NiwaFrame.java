
package swen222.niwa.gui;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import swen222.niwa.Controller;

/**
 * Top level frame for the application window.
 * @author Jack U
 *
 */

public class NiwaFrame extends JFrame implements Observer{
	
	final Controller control;
	
	final MenuBar menuBar;
	final GamePanel gamePanel;
	final InventoryPanel invPanel;
	
	
	public NiwaFrame(Controller control) throws HeadlessException{
		super("Title");
		
		this.control = control;
		
		menuBar=new MenuBar(control);
		gamePanel=new GamePanel(control);
		invPanel = new InventoryPanel(control);
		
		this.setJMenuBar(menuBar);
		add(gamePanel,BorderLayout.CENTER);
		add(invPanel, BorderLayout.WEST);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		pack();
		setVisible(true); // make sure we are visible!
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}

