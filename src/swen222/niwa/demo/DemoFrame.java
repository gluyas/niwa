package swen222.niwa.demo;

import swen222.niwa.gui.graphics.RoomRenderer;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import static java.awt.event.KeyEvent.*;

/**
 * Scrap class for demonstrating at the lab.
 *
 * @author Marc
 */
public class DemoFrame extends JFrame implements Observer, KeyListener {

	DemoPanel panel;
	RoomRenderer rr;
	DemoPlayer p;
	String stageName = "resource";

	HashEntityTable<Entity> et = new HashEntityTable<>();


	public DemoFrame() {
		super("Garden Demo");
		rr = new RoomRenderer(null, null);

		load();

		panel = new DemoPanel(rr);
		add(panel);

		panel.setEnabled(true);
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(1280, 720));
		panel.setSize(1280, 720);
		addKeyListener(this);

		pack();
		setVisible(true); // make sure we are visible!
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//		Timer timer = new Timer(10, (e) -> this.repaint());
//		timer.setRepeats(true);
//		timer.start();
	}

	private void load() {
		JFileChooser chooser = new JFileChooser(stageName);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"XML map files", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			stageName = chooser.getSelectedFile().getAbsolutePath();
			refresh();
		} else System.err.println("Load failed");
	}

	private void refresh() {
		et.remove(p);
		et.deleteObserver(this);
		et = new HashEntityTable<>();

		Room newRoom = Room.newFromFile(new File(stageName), 0, 0, et);
		p = new DemoPlayer(Location.at(newRoom, 0, 0));
		et.add(p);
		et.addObserver(this);

		rr.setRoom(newRoom);
		rr.setET(et);

		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case VK_W:
				p.move(directionRelativeToMap(Direction.NORTH));
				break;

			case VK_A:

				p.move(directionRelativeToMap(Direction.WEST));
				break;

			case VK_S:

				p.move(directionRelativeToMap(Direction.SOUTH));
				break;

			case VK_D:

				p.move(directionRelativeToMap(Direction.EAST));
				break;

			case VK_Q:
				rr.rotateCW();
				repaint();
				break;

			case VK_E:
				rr.rotateCCW();
				repaint();
				break;

			case VK_R:
				refresh();
				break;

			case VK_L:
				load();
				break;

			case VK_N:
				rr.cycleDebugCoordinates();
				repaint();
				break;

			case VK_F5:
				refresh();
				break;
		}
		//repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	/**
	 * Returns the correct direction for the player to move, relative
	 * to the map orientation.
	 * @param d
	 * @return
	 */
	public Direction directionRelativeToMap(Direction d){

		Direction roomDirection = rr.getFacing();

		switch(roomDirection){

			case NORTH:
				return d;

			case WEST:
				return d.turnCCW();

			case SOUTH:
				d=d.turnCW();
				return d.turnCW();

			case EAST:
				return d.turnCW();
		}
		return d;

	}

	public static void main(String[] args) {
		new DemoFrame();
	}

}
