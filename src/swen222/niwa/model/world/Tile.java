package swen222.niwa.model.world;

import java.io.Serializable;
import java.util.Random;

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.gui.graphics.Visible;
import swen222.niwa.model.entity.Entity;

/**
 * Class for storing material and height information for a single unit of a Room. Can also hold a prop.
 *
 * @author Marc
 * @author Jack U
 */
public class Tile implements Visible, Serializable {

	private String type;
	public final int height;
	private SpriteSet sprites;
	public Prop prop;
	public boolean canOccupy;

	private Random random;

	public Tile( String type, int height, SpriteSet ss, boolean canOccupy) {
		this.type = type;
		this.height = height;
		this.sprites = ss;
		this.canOccupy = canOccupy;
	}

	public Tile(String type, int height, SpriteSet ss, boolean canOccupy, Prop prop) {
		this.type = type;
		this.height = height;
		this.sprites = ss;
		this.canOccupy = canOccupy;
		this.prop = prop;
	}

	/**
	 * Returns the height of the tile
	 * @return
	 */
	public int getHeight(){
		return this.height;
	}


	/**
	 * Gets the type of the tile
	 * @return
	 */
	public String getType(){
		return this.type;
	}

	/**
	 * Adds a prop to the tile
	 * @param p
	 */
	public void addProp(Prop p){
		this.prop=p;
	}

	public Prop getProp(){
		return prop;
	}


	/**
	 * Checks if a specified Entity may occupy the Location of this Tile
	 * Will only return true if the prop that is attached can be passed through
	 * @param e an Entity to check
	 * @return true if the Entity is allowed to occupy the Location of this Tile
	 */
	//TODO: Hamish B please review - game rule implementation; move functionality to appropriate class if necessary

	public boolean canOccupy(Entity e) {
		if (!canOccupy){return false;}
		if (getProp()==null){ // no prop
			return true;
		}
		else return prop.canPassThrough();
	}

	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

}


