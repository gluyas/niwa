
package swen222.niwa.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Represents an item in the inventory
 * 
 * @author Zoe
 *
 */
public class InventoryBtn extends JToggleButton implements MouseListener {

	private boolean hover;
	private String item;// name of the item in this slot, "Empty" if nothing
						// there
	public static final int SIZE = 50;
	private Color color;// stand in for thumb nail image, for testing purposes
	private Image img;
	private int count;// how many of this kind of item the player has

	public InventoryBtn() {
		item = "Empty";
		count = 0;
		hover = false;
		// this is just here to test the drawing, will eventually take img from
		// sprite
		try {
			//img = ImageIO.read(new File("resource/images/entities/misc/seedThumb.png"));
			img = ImageIO.read(new File("resource/images/entities/runes/1Thumb.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		color = Color.WHITE;

		setFocusable(true);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setVisible(true);
		addMouseListener(this);

		setActionCommand(item);
		setToolTipText(item + ", " + count);
	}

	/**
	 * updates the item and count fields, tool tip and action command of the
	 * button
	 * 
	 * @param item
	 *            - the new item String
	 * @param count
	 *            - the new count integer
	 */
	public void updateButton(String item, int count, Image img) {
		this.item = item;
		this.count = count;
		this.img = img;
		setActionCommand(item);
		setToolTipText(item + ", " + count);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SIZE, SIZE);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int x = this.getWidth() / 2 - SIZE / 2;
		int y = this.getHeight() / 2 - SIZE / 2;

		if (this.isSelected()) {
			g.setColor(new Color(0xF27165));
		} else if (hover) {
			g.setColor(new Color(0xA9EF6B));
		} else {
			g.setColor(new Color(0xA4E2E8));
		}
		g.fillRect(x, y, SIZE, SIZE);

		g.setColor(color);
		g.fillRect(x + SIZE / 10, y + SIZE / 10, SIZE - 2 * (SIZE / 10), SIZE - 2 * (SIZE / 10));
		g.drawImage(img, x + SIZE / 10, y + SIZE / 10, SIZE - 2 * (SIZE / 10), SIZE - 2 * (SIZE / 10), null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		hover = true;
		repaint();

	}

	@Override
	public void mouseExited(MouseEvent e) {
		hover = false;
		repaint();

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
