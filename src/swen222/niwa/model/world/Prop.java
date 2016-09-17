package swen222.niwa.model.world;

import swen222.niwa.model.entity.Entity;
import java.awt.*;

/**
 * Interface for world objects which are fixed at the time of Room creation
 *
 * @author Marc
 */
public interface Prop {

	/**
	 * @return An Image representation of this prop. For use by the
	 */
	Image sprite();

	/**
	 * @return True if the given Entity can move onto a Tile with this prop. (or an equivalent case)
	 */
	//TODO: Hamish B please review - game rule implementation; move functionality to appropriate class if necessary
	boolean canPassThrough(Entity e);

}
