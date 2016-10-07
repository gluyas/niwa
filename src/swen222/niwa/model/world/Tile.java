package swen222.niwa.model.world;

import java.util.Random;

import swen222.niwa.gui.Sprite;
import swen222.niwa.gui.Visible;
import swen222.niwa.model.entity.Entity;

/**
 * Class for storing material and height information for a single unit of a Room. Can also hold a prop.
 *
 * @author Marc
 * @author Jack U
 */
public class Tile implements Visible {

	public enum TileType{
		GRASSTILE,
		STONETILE,
		SANDTILE,
		DIRTTILE,
		WATERTILE,
		KOITILE,
	}

	public TileType type;
	public final int height;
	public Texture texture;
	public Prop prop;
	public boolean canOccupy;

	private Random random;

	public Tile(int height, TileType type) {
		this.height = height;
		this.type = type;
		random = new Random();
		setTileVariables(type);



	}

	public Tile(int height, TileType type, Prop prop) {
		this.height = height;
		this.type = type;
		this.prop = prop;
		random = new Random();
		setTileVariables(type);
	}

	/**
	 * Sets variables for specific tiles, as different
	 * tiles have different behaviours
	 * @param type
	 */
	public void setTileVariables(TileType type){

		switch(type){
		case GRASSTILE:


			int grassRandom = random.nextInt(3);

			switch (grassRandom){
			case 0:
				this.texture = new DevTexture(DevTexture.grassBlock1);
				break;
			case 1:
				this.texture = new DevTexture(DevTexture.grassBlock2);
				break;
			case 2:
				this.texture = new DevTexture(DevTexture.grassBlock3);
				break;

			default:
				this.texture = new DevTexture(DevTexture.grassBlock1);
				break;
			}

			this.canOccupy = true;
			break;

		case STONETILE:
			texture = new DevTexture(DevTexture.stoneBlock);
			this.canOccupy = true;
			break;


		case SANDTILE:
			texture = new DevTexture(DevTexture.sandBlock);
			this.canOccupy = true;
			break;


		case DIRTTILE:
			texture = new DevTexture(DevTexture.dirtBlock);
			this.canOccupy = true;
			break;


		case WATERTILE:

			int waterRandom = random.nextInt(2);

			switch (waterRandom){
			case 0:
				this.texture = new DevTexture(DevTexture.waterBlock1);
				break;
			case 1:
				this.texture = new DevTexture(DevTexture.waterBlock3);
				break;
			default:
				this.texture = new DevTexture(DevTexture.waterBlock1);
				break;
			}


			this.canOccupy = false;
			break;

		case KOITILE:
			texture = new DevTexture(DevTexture.waterBlock2);
			this.canOccupy = false;

		}


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
	public TileType getType(){
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
		else return prop.canPassThrough();
	}

	@Override
	public Sprite sprite(Direction facing) {
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


