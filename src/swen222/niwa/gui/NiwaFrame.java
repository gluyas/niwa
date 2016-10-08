
package swen222.niwa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import swen222.niwa.Controller;
import swen222.niwa.model.world.Room;

/**
 * Top level frame for the application window.
 * 
 * @author Jack U
 *
 */

public class NiwaFrame extends JFrame implements Observer {

	final Controller control;

	final MenuBar menuBar;
	final RenderPanel renderPanel;
	final MainAppPanel gamePanel;
	final InventoryPanel invPanel;
	

	public NiwaFrame(Controller control) throws HeadlessException {
		super("Title");

		this.control = control;
		// not sure if works, resolve later
		Room stage = Room.newFromFile(new File("resource/rooms/testRoom.xml"));

		menuBar = new MenuBar(control);
		renderPanel = new RenderPanel(new RoomRenderer(stage));
		gamePanel = new MainAppPanel(control);
		

		this.setJMenuBar(menuBar);
		
		invPanel = new InventoryPanel(control);
		
		add(invPanel, BorderLayout.NORTH);
		add(renderPanel, BorderLayout.CENTER);
		add(gamePanel, BorderLayout.SOUTH);

		setMinimumSize(new Dimension(710,200));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();
		setVisible(true); // make sure we are visible!
		
		System.out.println(this.getWidth() + ", " + this.getHeight());
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
