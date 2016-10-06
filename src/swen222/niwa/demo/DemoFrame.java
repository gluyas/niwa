package swen222.niwa.demo;

import swen222.niwa.gui.RoomRenderer;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;

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

	public DemoFrame(String stageName) {
		super("Garden Demo");

		this.stageName = stageName;
		Room stage = Room.newFromFile(new File(stageName));

		rr = new RoomRenderer(stage);
		panel = new DemoPanel(rr);
		add(panel);
		panel.setEnabled(true);
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(1280, 720));
		panel.setSize(1280, 720);
		addKeyListener(this);

		p = new DemoPlayer(Location.at(stage, 0, 0));
		if (!stage.addEntity(p)) throw new AssertionError();
		p.addObserver(this);

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
				p.move(Direction.NORTH);
				break;

			case VK_A:
				p.move(Direction.WEST);
				break;

			case VK_S:
				p.move(Direction.SOUTH);
				break;

			case VK_D:
				p.move(Direction.EAST);
				break;

			case VK_Q:
				rr.rotateCW();
				repaint();
				break;

			case VK_E:
				rr.rotateCCW();
				repaint();
				break;

			case VK_F5:
				refresh();
		}
		//repaint();
	}

	private void refresh() {
		rr.r.removeEntity(p);
		Room stage = Room.newFromFile(new File(stageName));
		rr = new RoomRenderer(stage);
		panel.setRR(rr);
		p.setLocation(Location.at(stage, 0, 0));
		stage.addEntity(p);
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {

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
