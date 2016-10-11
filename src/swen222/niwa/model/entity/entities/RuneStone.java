package swen222.niwa.model.entity.entities;

import swen222.niwa.file.SpriteLoader;
import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ChangingEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class RuneStone extends ChangingEntity{

	private String type;
	private static SpriteSet sprites;
	private Direction facing;

	public RuneStone(Location loc, String type, Direction dir){
		super(loc);
		this.type = type;
		this.sprites = getSSFromType(type);
		this.facing = dir;
		this.setDescription("A large stone with a strange "+type+"-like rune on it.");
	}

	public String getType(){
		return type;
	}

	private SpriteSet getSSFromType(String type){
		switch(type){
		case "circle":
			return SpriteLoader.get("runestone1");
		case "cross":
			return SpriteLoader.get("runestone2");
		case "lightning":
			return SpriteLoader.get("runestone3");
		default:
			return SpriteLoader.get("runestone1");
		}
	}


	@Override
	public Sprite sprite(Direction camera) {
		return sprites.facing(facing).sprite(camera);
	}

}
