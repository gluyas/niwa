package swen222.niwa.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * panel containing the render window that displays the game world
 * @author Zoe
 *
 */
public class RenderPanel extends JPanel implements Observer{

	private RoomRenderer rr;

	public RenderPanel (RoomRenderer rr){
		setDoubleBuffered(true);
		this.rr = rr;

		setPreferredSize(new Dimension(360, 550));
	}

	@Override
	public void paint(Graphics g){
		GradientPaint grad = new GradientPaint(
				0, 0, new Color(0xfff0f5),
				0, this.getHeight(), new Color(0xffdab9)
		);
		((Graphics2D) g).setPaint(grad);
		g.fillRect(0,0,getWidth(), getHeight());
		rr.draw(g, this.getWidth(), this.getHeight());
	}

	/**
	 * Returns the RoomRenderer used by this panel
	 * @return
	 */
	public RoomRenderer getRR(){
		return rr;
	}

	/**
	 * Sets the RoomRenderer used by this panel
	 * @param rr
	 */
	public void setRR(RoomRenderer rr) {
		this.rr = rr;
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

}
