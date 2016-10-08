package swen222.niwa.net;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.entities.PlayerEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * The Player class relays events e.g. key presses, clicks to a character proxy in
 * the game. This is only used in a single player game as, in a multi-player game,
 * the slave does the job of this class.
 * 
 * @author Hamish M
 *
 */

public class Player extends PlayerEntity{
	
	//TODO: We might not actually need this class as it is only necessary in a single player scenario.
	// Would like some clarification for whether or not single player support is required.
	
	
	static final Sprite[] sprites;
	private final int uid; // a unique id
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

	public Player(int uid,Location loc) {
		super(loc);
		this.uid = uid;
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

}
