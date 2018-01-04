package swen222.niwa.editor;

import swen222.niwa.gui.graphics.RoomRenderer;
import swen222.niwa.model.puzzle.Plant;
import swen222.niwa.model.world.Direction;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marc on 30/09/2016.
 */
public class EditorPanel extends JPanel {

	RoomRenderer rr;

	boolean puzzleMode;
	Plant previewPlant;

	public EditorPanel(RoomRenderer rr) {
		previewPlant = new Plant(Plant.Type.BASIC);
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
		this.displayMode(g);
		this.displayPreviewPlant(g);

	}

	public void setMode(boolean puzzleMode){
		this.puzzleMode = puzzleMode;
		repaint();
	}

	private void displayMode(Graphics g){

		g.setFont(new Font("font",Font.PLAIN, 20));
		g.setColor(Color.BLACK);
		String debugText;
		if(!puzzleMode){
			debugText = "Normal Mode";
		}
		else{
			debugText = "Puzzle Mode";
		}
		g.drawString(debugText,(int)(-this.getWidth()/2.2),(int)(-this.getHeight()/2.5));

	}

	private void displayPreviewPlant(Graphics g){
		previewPlant.drawSprite(g, Direction.NORTH, (int)(this.getWidth()/2.2),(int)(-this.getHeight()/2.5), 150);
	}

	public void setPreviewPlant(Plant p){
		this.previewPlant = p;
		repaint();
	}

	private void displayHitsNum(Graphics g){

	}



	public void setRR(RoomRenderer rr) {
		this.rr = rr;
	}
}