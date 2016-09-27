package swen222.niwa.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

/**
 * Represents an item in the inventory
 * @author Zoe
 *
 */
public class InventoryBtn extends JToggleButton implements MouseListener {

	private boolean hover;
	private String item;// name of the item in this slot, null if empty
	private Dimension dimension;
	private static final int SIZE = 50;
	private Color color;// stand in for thumb nail image, for testing purposes
	private int count;// how many of this kind of item the player has

	public InventoryBtn() {
		hover = false;
		dimension = new Dimension(SIZE, SIZE);
		color = Color.CYAN;

		setFocusable(true);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setVisible(true);
		addMouseListener(this);

		setActionCommand(item);
		setToolTipText(item + ", " + count);
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Dimension getPreferredSize() {
		return dimension;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.isSelected()) {
			g.setColor(Color.RED);
			g.fillRect(0, 0, SIZE, SIZE);
		}else{
			g.setColor(new Color(206, 237, hover ? 109 : 164));
			g.fillRect(0, 0, SIZE, SIZE);
		}

		g.setColor(getColor());
		g.fillRect(SIZE/10, SIZE/10, SIZE - 2*(SIZE/10), SIZE - 2*(SIZE/10));
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

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
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
