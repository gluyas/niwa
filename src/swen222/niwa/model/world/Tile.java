package swen222.niwa.model.world;

import swen222.niwa.gui.Sprite;
import swen222.niwa.gui.Visible;
import swen222.niwa.model.entity.Entity;

/**
 * Class for storing material and height information for a single unit of a Room. Can also hold a prop.
 *
 * @author Marc
 */
public class Tile implements Visible {

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
	 * @param e an Entity to check
	 * @return true if the Entity is allowed to occupy the Location of this Tile
	 */
	//TODO: Hamish B please review - game rule implementation; move functionality to appropriate class if necessary
	public boolean canOccupy(Entity e) {
		if (prop == null) return true;
		else return prop.canPassThrough(e);
	}

	@Override
	public Sprite sprite() {
		return this.texture.sprite(this.height); // using a Texture strategy
	}

	/**
	 * Proposed interface for how the texture will inform the rending pipeline of its appearance. May also
	 * affect game play in some way, eg: slippery ice. Probably follows a STRATEGY pattern.
	 */
	//TODO: design a standardised texture format for tiles
	public interface Texture {
		// nothing for now, may be of use though due to complex process of drawing tiles, esp. for things like
		// grass on top of dirt compared to just dirt on all 3 sides

		// general idea here is that the texture will be able to construct the isometric view of a block
		Sprite sprite(int height);
	}

}
