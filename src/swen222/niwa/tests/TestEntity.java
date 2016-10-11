package swen222.niwa.tests;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.World;

/**
 * Bare implementation for testing purposes.
 */
public class TestEntity extends Entity {

	public TestEntity(World.Location loc) {
		super(loc);
	}

	@Override
	public Sprite sprite(Direction facing) {
		return null;
	}

	@Override
	public boolean move(Direction d) {
		return super.move(d);
	}

	public World.Location moveTo(World.Location newLoc) {
		return setLocation(newLoc);
	}
}
