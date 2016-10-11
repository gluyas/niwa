package swen222.niwa.model.entity.entities;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class Rune extends ObjectEntity {

	private String type;
	private SpriteSet sprites;

	public Rune (Location loc){
		super(loc);
	}


	public Rune (Location loc, String type, SpriteSet sprites){
		super(loc);
		this.type = type;
		this.sprites = sprites;
		this.setDescription("A rock with a strange "+type+"-like rune on it.");
	}

	public String getType(){
		return type;
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}
}
