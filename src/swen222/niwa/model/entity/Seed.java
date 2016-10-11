package swen222.niwa.model.entity;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class Seed extends ObjectEntity {

	private SpriteSet sprites;


	public Seed(Location loc, SpriteSet sprites){
		super(loc);
		this.sprites = sprites;
		this.setName("seed");
		this.setDescription("A seed, perhaps you should plant it and see what grows.");
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

	public String toString(){
		return this.getName();
	}



}
