package swen222.niwa.model.entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 *
 * Represents a rune - can be picked up by a player and
 * used with a runeStone to gain seeds
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
		this.setName(type+" rune");
		this.setDescription("A rock with a strange "+type+"-like rune on it.");
	}
	public String toString(){
		return type;
	}
	public String getType(){
		return type;
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}
}
