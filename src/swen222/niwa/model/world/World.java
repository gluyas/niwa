package swen222.niwa.model.world;

import com.sun.istack.internal.Nullable;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Top level representation of the game world. Stores all rooms and their geography.
 */
public class World implements Serializable {

	public final int width;
	public final int height;

							// TODO: solve issues outlined here
	private Room[][] map;   // there are some tricky problems for this format - it's quite tricky to figure out where a
							// given room is in this map, which is necessary for finding adjacent rooms when travelling
							// linking rooms may actually be the simplest solution

	public World(Room r) {
		height = 1;
		width = 1;
		map = new Room[1][1];
		map[0][0] = r;
	}

	public static World newFromFile(File f) {
		//TODO
		return null;
	}

	public static World newFromRandom() {
		//TODO
		return null;
	}

	private World(int width, int height) {
		this.width = width;
		this.height = height;
		this.map = new Room[width][height];
	}

	/**
	 * Get the RoomTuple at the specified co-ordinates
	 * @param col column
	 * @param row row
	 * @return null if there is no RoomTuple at the specified co-ordinates
	 */
	@Nullable
	// TODO: review if we want to allow empty RoomTuple spaces in the World - if not then update this method & javadoc
	public Room roomAt(int col, int row) {
		if (col < 0 || col >= map.length || row < 0 || row >= map[0].length) {
			throw new IllegalArgumentException("Co-ordinates out of bounds");
		} else {
			// assert map[row][col] != null : String.format("RoomTuple at (%d, %d) is null", col, row); //TODO: see above
			return map[row][col];
		}
	}

	//TODO: implement this
	public Location getSpawn() {
		return Location.at(roomAt(0, 0), 0, 0);
	}

}
