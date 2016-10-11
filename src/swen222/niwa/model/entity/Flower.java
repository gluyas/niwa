package swen222.niwa.model.entity;

import java.util.Random;

/**
 * Represents a flower - when a seed is planted it turns into a flower
 * Has different colours for different players .
 * @author Hamish m
 */

import swen222.niwa.file.SpriteLoader;
import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

public class Flower extends Entity{

	private String type;
	private static SpriteSet[][] SPRITES = {
			{
					SpriteLoader.get("redFlower1"),
					SpriteLoader.get("redFlower2"),
					SpriteLoader.get("redFlower3")
			},
			{
					SpriteLoader.get("blueFlower1"),
					SpriteLoader.get("blueFlower2"),
					SpriteLoader.get("blueFlower3")
			},
			{
					SpriteLoader.get("greenFlower1"),
					SpriteLoader.get("greenFlower2"),
					SpriteLoader.get("greenFlower3")
			},
			{
					SpriteLoader.get("blackFlower1"),
					SpriteLoader.get("blackFlower2"),
					SpriteLoader.get("blackFlower3")
			},
			{
					SpriteLoader.get("greyFlower1"),
					SpriteLoader.get("greyFlower2"),
					SpriteLoader.get("greyFlower3")
			}
	};

	int spriteNum;
	int spriteCol;

	public Flower(Location loc, String type) {
		super(loc);
		this.type = type;
		getRandomSprite(type);
		this.setDescription("What a lovely "+type+" flower.");
	}

	private void getRandomSprite(String type) {
		//generate a random int to append
		int randInt = new Random().nextInt(3)-1;
		spriteNum = randInt;
		switch(type){
		case "red":
			spriteCol = 0;
			break;
		case "blue":
			spriteCol = 1;
			break;
		case "green":
			spriteCol = 2;
			break;
		case "black":
			spriteCol = 3;
			break;
		case "grey":
			spriteCol = 4;
			break;
		default:
			spriteCol =0;
			break;
		}
	}

	@Override
	public Sprite sprite(Direction camera) {
		return SPRITES[spriteCol][spriteNum].sprite(camera);
	}

}
