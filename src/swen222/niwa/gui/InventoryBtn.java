
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

	public static final int SIZE = 50;
	// name of the item in this slot, "Empty" if nothing there
	private String item;
	// image of the item, null if empty
	private Image img;
	// how many of this kind of item the player has, 0 if empty
	private int count;
	private boolean hover;

	public InventoryBtn() {
		item = "Empty";
		img = null;
		count = 0;
		hover = false;
		// this is just here to test the drawing, will eventually take img from
		// sprite
//		try {
//			// img = ImageIO.read(new
//			// File("resource/images/entities/misc/seedThumb.png"));
//			img = ImageIO.read(new File("resource/images/entities/runes/1Thumb.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		setFocusable(true);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setVisible(true);
		addMouseListener(this);

		setActionCommand(item);
		setToolTipText(item + ", " + count);
	}

	/**
	 * updates the item, img, and count fields, tool tip and action command of the
	 * button to reflect what is currently stored here
	 * 
	 * @param item
	 *            - the new item String
	 * @param count
	 *            - the new count integer
	 * @param img
	 *            - the image for this item
	 */
	public void updateButton(String item, int count, Image img) {
		this.item = item;
		this.count = count;
		this.img = img;
		setActionCommand(item);
		setToolTipText(item + ", " + count);
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

		g.setColor(Color.WHITE);
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
