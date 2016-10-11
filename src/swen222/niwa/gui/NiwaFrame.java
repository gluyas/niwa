
package swen222.niwa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import swen222.niwa.Client;
import swen222.niwa.model.world.Room;

/**
 * Top level frame for the application window.
 *
 * @author Jack U, Zoe
 *
 */

public class NiwaFrame extends JFrame implements Observer {

	final Client control;

	public final MenuBar menuBar;
	public final RenderPanel renderPanel;
	public final MainAppPanel gamePanel;
	public final InventoryPanel invPanel;

	public NiwaFrame(Client control) throws HeadlessException {
		super("✿　庭　～　ニ―ワッ　～　笑　❀");

		this.control = control;
		addKeyListener(control);
		setFocusable(true);
		// for testing
		//Room stage = Room.newFromFile(new File("resource/rooms/desertBowl.xml"));

		menuBar = new MenuBar(control);
		renderPanel = new RenderPanel(control);
		gamePanel = new MainAppPanel(control);
		invPanel = new InventoryPanel(control);

		this.setJMenuBar(menuBar);

		add(invPanel, BorderLayout.NORTH);
		add(renderPanel, BorderLayout.CENTER);
		add(gamePanel, BorderLayout.SOUTH);

		setMinimumSize(new Dimension(710, 200));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();
		setLocationRelativeTo(null);
		setVisible(true); // make sure we are visible!
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
