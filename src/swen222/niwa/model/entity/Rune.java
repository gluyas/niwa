package swen222.niwa.model.entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
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
		Image thumb = null;
		try {
			if(type.equals("circle")){
				thumb = ImageIO.read(new File("resource/images/runes/1Thumb.png"));
			}else if(type.equals("cross")){
				thumb = ImageIO.read(new File("resource/images/runes/2Thumb.png"));
			}else{
				thumb = ImageIO.read(new File("resource/images/runes/3Thumb.png"));
			}

		} catch (IOException e) {
			// loading thumbnail failed
			e.printStackTrace();
		}
		this.setThumbnail(thumb);
		this.setName(type+" rune");
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
