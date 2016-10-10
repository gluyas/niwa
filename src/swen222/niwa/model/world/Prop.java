package swen222.niwa.model.world;

import java.util.Random;

import swen222.niwa.gui.Sprite;
import swen222.niwa.gui.Visible;
import swen222.niwa.model.world.Tile.Texture;

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

	private Random random;

	public Prop(PropType type){
		random = new Random();
		this.type=type;
		setPropVariables(type);

	}


	public enum PropType{

		BAMBOO,
		BIGROCK,
		BIGTREE,
		BUSH,
		FENCESIDE,
		FENCEFRONT,
		GRASS,
		PATH1,
		PATH2,
		SMALLTREE,
		SOIL,
		ZEN1,
		ZEN2;

	}

	/**
	 * Sets variables for specific props, as different
	 * props have different behaviours
	 * @param type
	 */
	public void setPropVariables(PropType type) {
		
		int textureDecider = 0;
		
		switch (type) {
			case BAMBOO:
				this.texture = new DevTexture(DevTexture.bamboo, 0.5, 0.79);
				this.canOccupy = false;
				break;

			case BIGROCK:

				textureDecider = getRandomInt(2);

				if(textureDecider == 0){
					texture = new DevTexture(DevTexture.bigRock1, 0.5, 0.83);
				}
				else{
					texture = new DevTexture(DevTexture.bigRock2, 0.5, 0.83);
				}

				this.canOccupy = false;
				break;


			case BIGTREE:

				textureDecider = getRandomInt(3);

				if(textureDecider == 0){
					texture = new DevTexture(DevTexture.bigTree1, 0.5, 0.83);
				}
				else if (textureDecider == 1){
					texture = new DevTexture(DevTexture.bigTree2, 0.5, 0.83);
				}
				else {
					texture = new DevTexture(DevTexture.bigTree3, 0.5, 0.83);
				}

				this.canOccupy = false;
				break;


			case BUSH:
				texture = new DevTexture(DevTexture.bush, 0.47, 0.84);
				this.canOccupy = false;
				break;


			case FENCESIDE:
				texture = new DevTexture(DevTexture.fenceSide,0.5,0.82);
				this.canOccupy = false;
				break;

			case FENCEFRONT:
				texture = new DevTexture(DevTexture.fenceFront,0.5,0.82);
				this.canOccupy = false;
				break;

			case GRASS:
				texture = new DevTexture(DevTexture.grass,0.5,0.82);
				this.canOccupy = true;
				break;

			case PATH1:
				texture = new DevTexture(DevTexture.path1Side,0.5,0.82);
				this.canOccupy = true;
				break;

			case PATH2:
				texture = new DevTexture(DevTexture.path2Side,0.5,0.82);
				this.canOccupy = true;
				break;

			case SOIL:
				texture = new DevTexture(DevTexture.soil,0.5,0.84);
				this.canOccupy = true;
				break;
				
			case SMALLTREE:
				
				textureDecider = getRandomInt(3);

				if(textureDecider == 0){
					texture = new DevTexture(DevTexture.smallTree1, 0.5, 0.83);
				}
				else if (textureDecider == 1){
					texture = new DevTexture(DevTexture.smallTree2, 0.5, 0.83);
				}
				else {
					texture = new DevTexture(DevTexture.smallTree3, 0.5, 0.83);
				}
				
				
				this.canOccupy = false;
				break;


			case ZEN1:
				texture = new DevTexture(DevTexture.zen1, 0.5, 0.835);
				this.canOccupy = false;
				break;

			case ZEN2:
				texture = new DevTexture(DevTexture.zen2, 0.5, 0.835);
				this.canOccupy = false;
				break;
			

		}
	}
	
	private int getRandomInt(int i){
		return random.nextInt(i);
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
	public Sprite sprite(Direction facing) {
		// TODO Auto-generated method stub
		return texture.sprite(0);
	}

}
