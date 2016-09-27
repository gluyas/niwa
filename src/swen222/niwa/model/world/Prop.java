package swen222.niwa.model.world;

import swen222.niwa.gui.Sprite;
import swen222.niwa.gui.Visible;
import swen222.niwa.model.entity.Entity;
import java.awt.*;
import java.util.ArrayList;

/**
 * Interface for world objects which are fixed at the time of Room creation
 *
 * @author Marc
 */
public class Prop implements Visible {

	public propType type;
	public ArrayList<propType> abstractProps;

	public Prop(propType typ){
		type=type;
		abstractProps.add(propType.GRASS);
		abstractProps.add(propType.FLOWER);
	}
	public enum propType{
		GRASS,
		FLOWER,

		BAMBOO,
		BIGROCK,
		BIGTREE,
		BUSH,
		FENCE,
		SMALLROCK,
		SMALLTREE;

	}

	/**
	 * @return True if the given Entity can move onto a Tile with this prop. (or an equivalent case)
	 */
	boolean canPassThrough(Entity e){
		if(abstractProps.contains(type)){
			return true;
		}
		return false;
	}

	@Override
	public Sprite sprite() {
		// TODO Auto-generated method stub
		return null;
	}

}
