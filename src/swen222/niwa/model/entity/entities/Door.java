package swen222.niwa.model.entity.entities;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class Door extends ObjectEntity {
	


	static final Sprite sprite ;

	static {
		try {
			sprite = new Sprite (
					ImageIO.read(new File("resource/images/entities/door-front.png")),
					0.5, 0.8);
		} catch (IOException e) {
			throw new Error("Couldn't load image", e);
		}

	}

	public Door(Location loc){
		super(loc);
	}
	@Override
	public Sprite sprite(Direction facing) {
		return sprite;
	}


}
