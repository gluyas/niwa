package swen222.niwa.model.world;

/**
 * Tuple class for storing a Location. All Location instances must correspond to a valid Tile co-ordinate in a
 * specific Room. Co-ordinates are stored in row/col format to make it coherent with a 2D array.
 *
 * @author Marc
 */
public class Location {

	public final Room room;
	public final int row;
	public final int col;

	/**
	 * Create a new Location representation of the specified Room and co-ordinates.
	 * Use this method to guarantee that all Locations correspond to a valid co-ordinate
	 *
	 * @param room the Room that this Location is within
	 * @param col position from the left of the Room (x equivalent)
	 * @param row position from the top of the Room (y equivalent)
	 */
	public static Location at(Room room, int col, int row) {
		if (room == null) throw new IllegalArgumentException("Cannot create a Location with null Room");
		try {
			checkPos(room, col, row);
			return new Location(room, col, row);
		} catch (InvalidLocationException e) {
			// throws a RuntimeException here, as requiring a try/catch would be ridiculous - but makes sense for move()
			// it is much easier to ensure that a brand new Location is valid, compared to one created from it
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public Tile tile(){
		return room.tileAt(this);
	}

	/**
	 * Get the Location (in the same Room) adjacent to this one, in a specified Direction
	 *
	 * @param d Direction to move in
	 * @return a new Location object
	 * @throws InvalidLocationException if the new Location is out of bounds
	 */
	public Location move(Direction d) throws InvalidLocationException {
		if (d == null) throw new IllegalArgumentException("Cannot move in null direction");
		int newCol = col + d.relativeX();
		int newRow = row + d.relativeY();
		checkPos(room, newCol, newRow);
		return new Location(room, newCol, newRow);
	}

	/**
	 * @return true if the given arguments will create a valid Position with Postion.at(...)
	 */
	public static boolean isValidPosition(Room room, int col, int row) {
		return col >= 0 && col < room.width && row >= 0 && row < room.height;
	}

	/**
	 * @throws InvalidLocationException if col or row is outside the valid range.
	 */
	private static void checkPos(Room room, int col, int row) throws InvalidLocationException {
		if (!isValidPosition(room, col, row)) {
			throw new InvalidLocationException("Location out of bounds: "+col+", "+row);
		}
	}

	// private to ensure validity-checks using Location.at(...)
	private Location(Room room, int col, int row) {
		this.room = room;
		this.col = col;
		this.row = row;
	}

	public Room getRoom(){
		return room;
	}

	@Override
	public int hashCode() {
		return room.hashCode() ^ col ^ row;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location) {
			Location loc = (Location) obj;
			return loc.room == this.room && loc.col == this.col && loc.row == this.row;
		} else return false;
	}

	@Override
	public String toString() {
		return String.format("%s: (%d,%d)", room.toString(), col, row);
	}

	// this is a checked exception because I love watching you suffer. you would actually have to check anyway,
	// even if it was unchecked, so now you can (read: must) just do it with try/catch block.
	public static class InvalidLocationException extends Exception {
		public InvalidLocationException() {
			super();
		}

		public InvalidLocationException(String message) {
			super(message);
		}

		public InvalidLocationException(String message, Throwable cause) {
			super(message, cause);
		}

		public InvalidLocationException(Throwable cause) {
			super(cause);
		}

		protected InvalidLocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

}
