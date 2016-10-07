package swen222.niwa.demo;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Marc on 30/09/2016.
 */
public class DemoPlayer extends PlayerEntity {

	static final Sprite[] sprites;

	private Direction facing = Direction.NORTH;

	static {
		sprites = new Sprite[4];
		try {
			sprites[Direction.NORTH.ordinal()] = new Sprite(
					ImageIO.read(new File("resource/images/players/player-blue-side.png")), 0.5, 0.8
			);
			sprites[Direction.SOUTH.ordinal()] = sprites[Direction.NORTH.ordinal()];
			sprites[Direction.WEST.ordinal()] = new Sprite(
					ImageIO.read(new File("resource/images/players/player-blue-front.png")), 0.5, 0.8
			);
			sprites[Direction.EAST.ordinal()] = sprites[Direction.WEST.ordinal()];
		} catch (IOException e) {
			throw new Error("Couldn't load Image: ", e);
		}
	}

	public DemoPlayer(Location loc) {
		super(loc);
	}

	@Override
	public Sprite sprite(Direction facing) {
		//return sprites[0];
		return sprites[this.facing.relativeTo(facing).ordinal()];
	}

	@Override
	public boolean move(Direction d) {
		facing = d;
		return super.move(d);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + getLocation().toString();
	}


}
