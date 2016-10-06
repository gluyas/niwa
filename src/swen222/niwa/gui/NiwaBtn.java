package swen222.niwa.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class NiwaBtn extends JButton implements MouseListener {

	private boolean hover;
	private boolean click;
	private String label;

	public NiwaBtn(String label) {
		hover = false;
		click = false;
		this.label = label;

		setFocusable(true);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setVisible(true);
		addMouseListener(this);

		setActionCommand(label);
		setToolTipText(label);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(90, 40);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (click) {
			g.setColor(Color.RED);
		} else if (hover) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.GREEN);
		}

		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		FontMetrics fm = g.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(label, g);
		int x = (this.getWidth() - (int) r.getWidth()) / 2;
		int y = this.getHeight() - (int) r.getHeight() / 2 - fm.getAscent() / 2 - fm.getDescent();

		g.setColor(Color.BLACK);
		g.drawString(label, x, y);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		hover = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		hover = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		click = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		click = false;
		repaint();
	}

}
