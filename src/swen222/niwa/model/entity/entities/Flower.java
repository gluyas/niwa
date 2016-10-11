package swen222.niwa.model.entity.entities;

import java.util.Random;

import swen222.niwa.file.SpriteLoader;
import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

public class Flower extends Entity{

	private String type;
	private static SpriteSet sprites;

	public static final SpriteSet[] RED = {
			SpriteLoader.get("redFlower1"),
			SpriteLoader.get("redFlower2"),
			SpriteLoader.get("redFlower3")
	};
	public static final SpriteSet[] BLUE = {
			SpriteLoader.get("blueFlower1"),
			SpriteLoader.get("blueFlower2"),
			SpriteLoader.get("blueFlower3")
	};
	public static final SpriteSet[] GREEN = {
			SpriteLoader.get("greenFlower1"),
			SpriteLoader.get("greenFlower2"),
			SpriteLoader.get("greenFlower3")
	};
	public static final SpriteSet[] BLACK = {
			SpriteLoader.get("blackFlower1"),
			SpriteLoader.get("blackFlower2"),
			SpriteLoader.get("blackFlower3")
	};
	public static final SpriteSet[] GREY = {
			SpriteLoader.get("greyFlower1"),
			SpriteLoader.get("greyFlower2"),
			SpriteLoader.get("greyFlower3")
	};

	public Flower(Location loc, String type) {
		super(loc);
		this.type = type;
		this.sprites = getRandomSprite(type);
	}

	private SpriteSet getRandomSprite(String type) {
		//generate a random int to append
		int randInt = new Random().nextInt(3)+1;
		switch(type){
		case "red":
			return SpriteLoader.get("redFlower"+randInt);
		case "blue":
			return SpriteLoader.get("blueFlower"+randInt);
		case "green":
			return SpriteLoader.get("greenFlower"+randInt);
		case "black":
			return SpriteLoader.get("blackFlower"+randInt);
		case "grey":
			return SpriteLoader.get("greyFlower"+randInt);
		default:
			return SpriteLoader.get("blueFlower"+randInt);
		}
	}

	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

}
