package swen222.niwa.gui;

import swen222.niwa.Client;

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
public class RenderPanel extends JPanel implements Observer {

	private Client control;
	private RoomRenderer rr;

	public RenderPanel (Client control){
		setDoubleBuffered(true);
		this.control = control;
		this.rr = new RoomRenderer(control.getRoom());
		control.addObserver(this);

		setPreferredSize(new Dimension(1280, 720));
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

	public RoomRenderer getRR(){
		return rr;
	}

	public void setRR(RoomRenderer rr) {
		this.rr = rr;
	}

	@Override
	public void update(Observable o, Object arg) {
		rr.setRoom(control.getRoom());
		rr.setET(control.getEntityTable());
		repaint();
	}

}
