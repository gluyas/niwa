package swen222.niwa.model.entity;

import java.util.Random;

/**
 * Represents a flower - when a seed is planted it turns into a flower
 * Has different colours for different players .
 * @author Hamish m
 */

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

public class Flower extends Entity{

	private String type;
	private static SpriteSet[][] SPRITES = {
			{
					SpriteSet.get("redFlower1"),
					SpriteSet.get("redFlower2"),
					SpriteSet.get("redFlower3")
			},
			{
					SpriteSet.get("blueFlower1"),
					SpriteSet.get("blueFlower2"),
					SpriteSet.get("blueFlower3")
			},
			{
					SpriteSet.get("greenFlower1"),
					SpriteSet.get("greenFlower2"),
					SpriteSet.get("greenFlower3")
			},
			{
					SpriteSet.get("blackFlower1"),
					SpriteSet.get("blackFlower2"),
					SpriteSet.get("blackFlower3")
			},
			{
					SpriteSet.get("greyFlower1"),
					SpriteSet.get("greyFlower2"),
					SpriteSet.get("greyFlower3")
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
		int randInt = new Random().nextInt(3);
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
