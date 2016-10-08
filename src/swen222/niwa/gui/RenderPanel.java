package swen222.niwa.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * panel containing the render window that displays the game world
 * @author Zoe
 *
 */
public class RenderPanel extends JPanel{
	
	RoomRenderer rr;
	
	public RenderPanel (RoomRenderer rr){
		setDoubleBuffered(true);
		this.rr = rr;
		
		setPreferredSize(new Dimension(640, 360));
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
	
	public void setRR(RoomRenderer rr) {
		this.rr = rr;
	}

}
