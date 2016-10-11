package swen222.niwa.model.entity;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ObjectEntity;
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
		this.setDescription("A seed, perhaps you should plant it and see what grows.");
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}


}
