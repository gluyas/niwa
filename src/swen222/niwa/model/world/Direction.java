package swen222.niwa.model.world;

/**
 * Simple Direction enum. Provides utilities for changing Directions and numeric representations.
 *
 * @author Marc
 */
public enum Direction {

	NORTH,
	EAST,
	SOUTH,
	WEST;

	/**
	 * @return The opposite Direction to this one.
	 */
	public Direction opposite() {
		switch (this) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			default:
				throw new AssertionError("Direction not accounted for: " + this); // this should never happen
		}
	}

	/**
	 * @return The Direction 90 degrees clockwise from this one
	 */
	public Direction turnCW() {
		switch (this) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
			default:
				throw new AssertionError("Direction not accounted for: " + this); // this should never happen
		}
	}

	/**
	 * @return The Direction 90 degrees counter-clockwise from this one
	 */
	public Direction turnCCW() {
		switch (this) {
			case NORTH:
				return WEST;
			case EAST:
				return NORTH;
			case SOUTH:
				return EAST;
			case WEST:
				return SOUTH;
			default:
				throw new AssertionError("Direction not accounted for: " + this); // this should never happen
		}
	}

	/**
	 * Return the relative Direction of this one to another. In other words, if looking in the given Direction
	 * as North, get the Direction of this one relative to that.
	 * @param to the Direction this is being viewed from
	 * @return this Direction rotated by the bearing of the other
	 */
	public Direction relativeTo(Direction to) {
		switch (to) {
			case NORTH:
				return this;
			case EAST:
				return this.turnCCW();
			case SOUTH:
				return this.opposite();
			case WEST:
				return this.turnCW();
			default:
				throw new AssertionError("Direction not accounted for: " + this); // this should never happen
		}

	}

	/**
	 * Get this Directions' modifier for a west-to-east-ascending numeric position
	 * @return -1, 0, or 1
	 */
	public int relativeX() {
		switch (this) {
			case WEST:
				return -1;
			case EAST:
				return 1;
			default:
				return 0;
		}
	}

	/**
	 * Get this Directions' modifier for a north-to-south-ascending numeric position
	 * @return -1, 0, or 1
	 */
	public int relativeY() {
		switch (this) {
			case NORTH:
				return -1;
			case SOUTH:
				return 1;
			default:
				return 0;
		}
	}

	public static Direction fromString(String dir){
		switch(dir.toLowerCase()){
		case "n":
			return NORTH;
		case "e":
			return EAST;
		case "s":
			return SOUTH;
		case "w":
			return WEST;
		default:
			return NORTH;

		}
	}
}
