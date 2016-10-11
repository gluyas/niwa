
package swen222.niwa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JFrame;
import swen222.niwa.Controller;
import swen222.niwa.model.world.Room;

/**
 * Top level frame for the application window.
 *
 * @author Jack U
 * @author kraemezoe
 */

public class NiwaFrame extends JFrame{

	final Controller control;

	public final MenuBar menuBar;
	public final RenderPanel renderPanel;
	public final MainAppPanel gamePanel;
	public final InventoryPanel invPanel;

	public NiwaFrame(Controller control) throws HeadlessException {
		super("niwa");

		this.control = control;
		// for testing
		Room stage = Room.newFromFile(new File("resource/rooms/desertBowl.xml"));

		menuBar = new MenuBar(control);
		renderPanel = new RenderPanel(new RoomRenderer(stage));
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

}
