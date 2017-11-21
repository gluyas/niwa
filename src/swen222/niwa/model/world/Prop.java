package swen222.niwa.model.world;

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.gui.graphics.Visible;

import java.io.Serializable;

/**
 * Interface for world objects which are fixed at the time of Room creation
 *
 * @author Marc
 * @author Jack
 * @author Hamish M
 */
public class Prop implements Visible, Serializable {


	private final String type; // the type of prop
	private final String description; // flavour text description of this prop
	private SpriteSet sprites; // the props sprites which can change based on direction
	private final boolean canOccupy; // whether or not the prop can be occupied by a player

	public Prop(String type, SpriteSet sprites, boolean canOccupy, String description){
		this.type = type;
		this.description = description;
		this.sprites = sprites;
		this.canOccupy = canOccupy;
	}

	/**
	 * Returns the type of this prop
	 * @return
	 */
	public String getType(){
		return type;
	}

	/**
	 * Returns the description of this prop
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * @return True if the given Entity can move onto a Tile with this prop. (or an equivalent case)
	 */
	public boolean canPassThrough(){
		return canOccupy;
	}

	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

}
