package swen222.niwa.gui;

import java.awt.*;

/**
 * Simple class containing an Image with an anchor point. Used by the Visible interface and GUI.
 *
 * @author Marc
 */
public class Sprite {

	public final Image img; //
	public final int aX;
	public final int aY;

	/**
	 * Create a new Sprite from a given Image, with an anchor point relative to it.
	 * @param img   the Image for this Sprite to represent
	 * @param aX    left (x) co-ordinate of the anchor point
	 * @param aY    top  (y) co-ordinate of the anchor point
	 */
	public Sprite(Image img, int aX, int aY) {
		this.img = img;
		this.aX = aX;
		this.aY = aY;
	}

	/**
	 * Draws this Sprite onto a specified Graphics object, anchored at given co-ordinates
	 * @param g the Graphics to draw onto
	 * @param x left position of the anchor
	 * @param y top position of the anchor
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(img, x - aX, y - aY, null);
	}

}
