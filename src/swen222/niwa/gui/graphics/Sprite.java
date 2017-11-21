package swen222.niwa.gui.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * Simple class containing an Image with an anchor point. Used by the Visible interface and GUI.
 *
 * @author Marc
 */
public class Sprite {

	private transient Image img; // need to make transient + custom encoding/decoding methods
	private final double width;
	private final double aX;
	private final double aY;


	/**
	 * Create a new Sprite from a given Image, with an anchor point relative to it.
	 * @param img       the Image for this Sprite to represent
	 * @param aX        left (x) co-ordinate of the anchor point as a proportion of the width of the image
	 * @param aY        top  (y) co-ordinate of the anchor point as a proportion of the hieght of the image
	 * @param width     the width of the sprite relative to blocks e.g. width of 1 = 1 block
	 */
	public Sprite(Image img, double aX, double aY, double width) {
		this.img = img;
		this.aX = aX;
		this.aY = aY;
		this.width = width;
	}

	/**
	 * Create a new Sprite from a given Image, with an anchor point relative to it. Defaults width to one block
	 * @param img   the Image for this Sprite to represent
	 * @param aX    left (x) co-ordinate of the anchor point as a proportion of the width of the image
	 * @param aY    top  (y) co-ordinate of the anchor point as a proportion of the hieght of the image
	 */
	public Sprite(Image img, double aX, double aY) {
		this.img = img;
		this.aX = aX;
		this.aY = aY;
		this.width = 1;
	}
	
	/**
	 *  Create a new Sprite from a given Image, this is the most default version of a Sprite.
	 * @param img
	 */
	public Sprite(Image img){
		this.img = img;
		this.aX = 0.5;
		this.aY = 0.25;
		this.width = 1;
	}

	public Image getImage() {
		return img;
	}

	/**
	 * Draws this Sprite onto a specified Graphics object, anchored at given co-ordinates
	 * @param g             the Graphics to draw onto
	 * @param x             left position of the anchor
	 * @param y             top position of the anchor
	 * @param blockWidth    width in pixels of a block on the screen
	 */
	public void draw(Graphics g, int x, int y, double blockWidth) {

		int pixelWidth = (int) (width*blockWidth);
		double scale = 1.0*pixelWidth/img.getWidth(null);

		int pixelHeight = (int) (img.getHeight(null)*scale);
		//int imgWidth = img.getWidth(null);

		int pixelAX = (int) (aX*pixelWidth);
		int pixelAY = (int) (aY*pixelHeight);

		g.drawImage(img, x - pixelAX, y - pixelAY, pixelWidth, pixelHeight, null);
	}
}
