package swen222.niwa.model.world;

import swen222.niwa.gui.Sprite;
import swen222.niwa.gui.Visible;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Tile.Texture;
import swen222.niwa.model.world.Tile.TileType;

import java.awt.*;
import java.util.ArrayList;

/**
 * Interface for world objects which are fixed at the time of Room creation
 *
 * @author Marc
 * @author Jack
 */
public class Prop implements Visible {

	public PropType type;
	public boolean canOccupy;
	private Texture texture;

	public Prop(PropType type){
		this.type=type;
		setPropVariables(type);
	}


	public enum PropType{

		BAMBOO,
		BIGROCK,
		BIGTREE,
		BUSH,
		FENCE,
		GRASS,
		SMALLROCK,
		SMALLTREE;

	}

	/**
	 * Sets variables for specific props, as different
	 * props have different behaviours
	 * @param type
	 */
	public void setPropVariables(PropType type){
		switch(type){
		case BAMBOO:
			this.texture = new DevTexture(DevTexture.bamboo);
			this.canOccupy = false;
			break;

		case BIGROCK:
			texture = new DevTexture(DevTexture.bigRock1);
			this.canOccupy = false;
			break;


		case BIGTREE:
			texture = new DevTexture(DevTexture.bigTree1);
			this.canOccupy = false;
			break;


		case BUSH:
			texture = new DevTexture(DevTexture.bush);
			this.canOccupy = false;
			break;


		case FENCE:
			texture = new DevTexture(DevTexture.fenceSide);
			this.canOccupy = false;
			break;

		case GRASS:
			texture = new DevTexture(DevTexture.flower);
			this.canOccupy = true;
			break;

		case SMALLROCK:
			texture = new DevTexture(DevTexture.smallRock);
			this.canOccupy = true;
			break;

		case SMALLTREE:
			texture = new DevTexture(DevTexture.smallTree);
			this.canOccupy = false;
			break;


		}

		}


	/**
	 * Returns the type of this prop
	 * @return
	 */
	public PropType getType(){
		return type;
	}


	/**
	 * @return True if the given Entity can move onto a Tile with this prop. (or an equivalent case)
	 */
	public boolean canPassThrough(){
		return canOccupy;
	}

	@Override
	public Sprite sprite() {
		// TODO Auto-generated method stub
		return null;
	}

}
