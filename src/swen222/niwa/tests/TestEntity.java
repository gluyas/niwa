package swen222.niwa.tests;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * Bare implementation for testing purposes.
 */
public class TestEntity extends Entity {

	public TestEntity(Location loc) {
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

	public Location moveTo(Location newLoc) {
		return setLocation(newLoc);
	}
}
