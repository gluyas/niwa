package swen222.niwa.editor;

import swen222.niwa.gui.graphics.RoomRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marc on 30/09/2016.
 */
public class EditorPanel extends JPanel {

	RoomRenderer rr;

	public EditorPanel(RoomRenderer rr) {
		setDoubleBuffered(true);
		this.rr = rr;
	}

	@Override
	public void paint(Graphics g) {
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