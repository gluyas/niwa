package swen222.niwa.model.world;

import com.sun.istack.internal.Nullable;

import java.io.File;

/**
 * Top level representation of the game world. Stores all rooms and their geography.
 */
public class World {

	//private int width;
	//private int height;

							// TODO: solve issues outlined here
	private Room[][] map;   // there are some tricky problems for this format - it's quite tricky to figure out where a
							// given room is in this map, which is necessary for finding adjacent rooms when travelling
							// linking rooms may actually be the simplest solution

	public static World newFromFile(File f) {
		//TODO
		return null;
	}

	public static World newFromRandom() {
		//TODO
		return null;
	}

	private World(int width, int height) {
		// this.width = width;
		// this.height = height;
		this.map = new Room[width][height];
	}

	/**
	 * Get the Room at the specified co-ordinates
	 * @param col column
	 * @param row row
	 * @return null if there is no Room at the specified co-ordinates
	 */
	@Nullable
	// TODO: review if we want to allow empty Room spaces in the World - if not then update this method & javadoc
	public Room roomAt(int col, int row) {
		if (col < 0 || col >= map.length || row < 0 || row >= map[0].length) {
			throw new IllegalArgumentException("Co-ordinates out of bounds");
		} else {
			// assert map[row][col] != null : String.format("Room at (%d, %d) is null", col, row); //TODO: see above
			return map[row][col];
		}
	}

}
