package swen222.niwa.model.entity.entities;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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



	static final Sprite sprite;
	private boolean triggered;


	static {
		try {
			sprite = new Sprite (
					ImageIO.read(new File("resource/images/entities/statue.png")),
					0.5, 0.8);
		} catch (IOException e) {
			throw new Error("Couldn't load image", e);
		}

	}

	public Statue(Location loc){
		super(loc);
		triggered=false;
	}
	@Override
	public Sprite sprite(Direction facing) {
		return sprite;
	}

	public boolean isTriggered() {
		return triggered;
	}
	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}


}
