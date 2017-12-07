package swen222.niwa.editor;

import swen222.niwa.gui.graphics.RoomRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marc on 30/09/2016.
 */
public class EditorPanel extends JPanel {

	RoomRenderer rr;

	boolean puzzleMode;

	public EditorPanel(RoomRenderer rr) {
		puzzleMode= false;
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
		this.drawMode(g);

	}

	public void setMode(boolean puzzleMode){
		this.puzzleMode = puzzleMode;
		repaint();
	}

	private void drawMode(Graphics g){

		g.setFont(new Font("font",Font.PLAIN, 20));
		g.setColor(Color.BLACK);
		String debugText;
		if(!puzzleMode){
			debugText = "normalMode";
		}
		else{
			debugText = "puzzleMode";
		}
		g.drawString(debugText,(int)(-this.getWidth()/2.2),(int)(-this.getHeight()/2.5));

	}



	public void setRR(RoomRenderer rr) {
		this.rr = rr;
	}
}