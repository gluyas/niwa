package swen222.niwa.demo;

import swen222.niwa.file.SpriteLoader;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.entities.PlayerEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by Marc on 30/09/2016.
 */
public class DemoPlayer extends PlayerEntity {

	static final SpriteLoader.SpriteSet ss = SpriteLoader.get("ghost-blue");

	private Direction facing = Direction.NORTH;

	public DemoPlayer(Location loc) {
		super(loc);
	}

	@Override
	public Sprite sprite(Direction camera) {
		//return sprites[0];
		//return sprites[this.facing.relativeTo(facing).ordinal()];
		return ss.sprite(camera);
	}

	@Override
	public boolean move(Direction d) {
		facing = d;
		return super.move(d);
	}
}
