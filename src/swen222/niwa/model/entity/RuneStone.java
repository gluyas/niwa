package swen222.niwa.model.entity;

import swen222.niwa.file.SpriteLoader;
import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 *
 * Represents a runestone - Runestones can be used with
 * runes to give the player seeds.
 * @author burnshami
 *
 */
public class RuneStone extends ChangingEntity {

	private String type;

	private static final SpriteSet[] SPRITES = {
			SpriteLoader.get("runestone1"),
			SpriteLoader.get("runestone2"),
			SpriteLoader.get("runestone3"),
	};

	private int typeNum;
	private Direction facing;

	public RuneStone(Location loc, String type, Direction dir){
		super(loc);
		this.type = type;
		this.typeNum = getSSFromType(type);
		//this.SPRITES = getSSFromType(type);
		this.facing = dir;
		this.setDescription("A large stone with a strange "+type+"-like rune on it.");
	}

	public String getType(){
		return type;
	}

	private int getSSFromType(String type){
		switch(type) {
			case "circle":
				return 0;
			case "cross":
				return 1;
			case "lightning":
				return 2;
			default:
				return 0;
		}
	}


	@Override
	public Sprite sprite(Direction camera) {
		return SPRITES[typeNum].facing(facing).sprite(camera);
	}

}
