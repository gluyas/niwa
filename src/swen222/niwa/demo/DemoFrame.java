package swen222.niwa.demo;

import swen222.niwa.gui.graphics.RoomRenderer;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.Rules;
import swen222.niwa.model.world.World;

import javax.swing.*;
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
	String stageName;
	Rules rules;
	World world;

	public DemoFrame(String stageName) {
		super("Garden Demo");

		this.stageName = stageName;
		Room stage = Room.newFromFile(new File(stageName), 0, 0);
		world = new World(1,1);
		//rules = new Rules(world.getMap());

		p = new DemoPlayer(Location.at(stage, 0, 0));
		//if (!stage.addEntity(p)) throw new AssertionError();
		p.addObserver(this);

		EntityTable<Entity> et = new HashEntityTable<>();
		et.add(p);

		rr = new RoomRenderer(stage, et);

		panel = new DemoPanel(rr);
		add(panel);
		panel.setEnabled(true);
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(1280, 720));
		panel.setSize(1280, 720);
		addKeyListener(this);

		pack();
		setVisible(true); // make sure we are visible!
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
				rules.move(p,directionRelativeToMap(Direction.NORTH));
				break;

			case VK_A:

				rules.move(p,directionRelativeToMap(Direction.WEST));
				break;

			case VK_S:

				rules.move(p,directionRelativeToMap(Direction.SOUTH));
				break;

			case VK_D:

				rules.move(p,directionRelativeToMap(Direction.EAST));
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
				rules.action(p,0);
				repaint();
				break;

			case VK_F5:
				refresh();
				break;
		}
		//repaint();
	}

	private void refresh() {
		//rr.setRoom(Room.newFromFile(new File(stageName), 0, 0));
		//rr.r.removeEntity(p);
//		Room stage = Room.newFromFile(new File(stageName), 0, 0);
//		rr = new RoomRenderer(stage);
//		panel.setRR(rr);
//		p.setLocation(Location.at(stage, 0, 0));
//		stage.addEntity(p);
//		repaint();
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
		if (args.length == 0) {
			System.err.println("Please provide a map to load in arguments");
			return;
		}
		try {
			new DemoFrame(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
