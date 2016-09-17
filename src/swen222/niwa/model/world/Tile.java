package swen222.niwa.model.world;

import swen222.niwa.model.entity.Entity;

/**
 * Class for storing material and height information for a single unit of a Room. Can also hold a prop.
 *
 * @author Marc
 */
public class Tile {

	public final int height;
	public final Texture texture;
	public final Prop prop;

	public Tile(int height, Texture texture) {
		this.height = height;
		this.texture = texture;
		this.prop = null;
	}

	public Tile(int height, Texture texture, Prop prop) {
		this.height = height;
		this.texture = texture;
		this.prop = prop;
	}

	/**
	 * Checks if a specified Entity may occupy the Location of this Tile
	 * @param e an Entity
	 * @return true if the Entity is allowed to occupy the Location of this Tile
	 */
	//TODO: Hamish B please review - game rule implementation; move functionality to appropriate class if necessary
	public boolean canOccupy(Entity e) {
		if (prop == null) return true;
		else return prop.canPassThrough(e);
	}

	/**
	 * Proposed class for how the texture will inform the rending pipeline of its appearance. May also
	 * affect game play in some way, eg: slippery ice. Probably follows a STRATEGY pattern.
	 */
	//TODO: design a standardised texture format for tiles
	public static class Texture {

	}
}
