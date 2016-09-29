package swen222.niwa.tests;

import swen222.niwa.gui.RoomRenderer;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.world.Room;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Marc
 */
public class ProjectionTest extends JFrame {

	Sprite img = new Sprite(ImageIO.read(new File("images/grass-block1.png")), 0.5, 0.25, 1);
	RoomRenderer rr = new RoomRenderer(Room.emptyRoom(5, 5));
	Canvas canvas = new Canvas();

	public ProjectionTest() throws HeadlessException, IOException{
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(canvas);
		addMouseListener(canvas);
		setVisible(true); // make sure we are visible!
		setSize(1000, 800);
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	public static void main(String[] args) throws IOException {
		new ProjectionTest();
	}

	private class Canvas extends JPanel implements MouseListener {
		@Override
		public void paint(Graphics g) {

			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			int[] v1 = rr.project(1, 1, 0);
			int[] v2 = rr.project(0, 1, 0);
			int[] v3 = rr.project(1, 0, 0);
			int[] v4 = rr.project(0, 0, 2);
			int[] v5 = rr.project(0, 1, 2);
			int[] v6 = rr.project(1, 0, 2);

			g.drawLine(v1[0], v1[1], v2[0], v2[1]);
			g.drawLine(v1[0], v1[1], v3[0], v3[1]);
			g.drawLine(v1[0], v1[1], v4[0], v4[1]);

			g.drawLine(v4[0], v4[1], v5[0], v5[1]);
			g.drawLine(v4[0], v4[1], v6[0], v6[1]);

			g.drawLine(v2[0], v2[1], v5[0], v5[1]);
			g.drawLine(v2[0], v2[1], v6[0], v6[1]);

			g.drawLine(v3[0], v3[1], v6[0], v6[1]);
			g.drawLine(v3[0], v3[1], v5[0], v5[1]);

			g.drawLine(v2[0], v2[1], v3[0], v3[1]);
			g.drawLine(v2[0], v2[1], v4[0], v4[1]);
			g.drawLine(v3[0], v3[1], v4[0], v4[1]);
			/*
			System.out.printf("v4: %s%n", Arrays.toString(v4));
			System.out.printf("v1: %s%n", Arrays.toString(v1));
			System.out.printf("v2: %s%n", Arrays.toString(v2));
			System.out.printf("v3: %s%n", Arrays.toString(v3));
			*/
			//g.fillRect(0, 0, 100, 100);
			g.setColor(Color.black);
			g.drawLine(500, 0, 500, 800);
			g.drawLine(0, 400, 1000, 400);
			g.translate(500, 400);

			for (int x = 0; x < 5; x ++) {
				for (int y = 0; y < 5; y++) {
					int[] pos = rr.project(x, y, Math.random()*3.5, 50);
					//int[] pos = rr.project(x, y);
					img.draw(g, pos[0], pos[1], 125);
					g.setColor(Color.ORANGE);
					g.drawLine(pos[0]-62, pos[1], pos[0]+62, pos[1]);
					int[] ceil = rr.project(x, y, -2, 50);
					g.setColor(Color.gray);
					g.drawLine(ceil[0], ceil[1], pos[0], pos[1]);
					g.setColor(Color.blue);
					g.fillOval(pos[0]-3, pos[1]-3, 6, 6);
					g.setColor(Color.red);
					g.fillOval(ceil[0]-3, ceil[1]-3, 6, 6);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			rr.rotateCCW();
			System.out.println(rr.getFacing());
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}

}
