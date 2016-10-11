package swen222.niwa.model.world;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.gui.Visible;

/**
 * Interface for world objects which are fixed at the time of Room creation
 *
 * @author Marc
 * @author Jack
 * @author Hamish M
 */
public class Prop implements Visible {


	private final String type; // the type of prop
	private SpriteSet sprites; // the props sprites which can change based on direction
	private final boolean canOccupy; // whether or not the prop can be occupied by a player

	public Prop(String type, SpriteSet sprites, boolean canOccupy){
		this.type = type;
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
	 * @return True if the given Entity can move onto a Tile with this prop. (or an equivalent case)
	 */
	public boolean canPassThrough(){
		return canOccupy;
	}

	public String getDescription(){
		return null;
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

}
