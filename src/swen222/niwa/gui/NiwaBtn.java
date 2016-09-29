package swen222.niwa.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class NiwaBtn extends JButton implements MouseListener{
	
	private boolean hover;
	private String label;
	
	public NiwaBtn(String label){
		hover = false;
		this.label = label;
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.setColor(new Color(0, 200, hover ? 109: 250));
		g.drawRect(0, 0, this.getWidth(), this.getHeight());
		
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(label, g);
		int x = this.getWidth() - (int) r.getWidth()/2;
		int y = 0;
		
		g.setColor(Color.BLACK);
		g.drawString(label, x, y);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
