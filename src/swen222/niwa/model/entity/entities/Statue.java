package swen222.niwa.model.entity.entities;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ChangingEntity;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class Statue extends ChangingEntity {

	private SpriteSet sprites;
	private boolean triggered;
	private final String dormantDesc = "A stone statue. It looks like it's sleeping.";
	private final String activeDesc = "The statue is giving out an eerie glow...";

	public Statue(Location loc, SpriteSet sprites){
		super(loc);
		this.sprites = sprites;
		this.triggered = false;
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

	public boolean isTriggered() {
		return triggered;
	}
	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
		if(triggered){
			this.setDescription(activeDesc);
		} else {
			this.setDescription(dormantDesc);
		}
	}


}
