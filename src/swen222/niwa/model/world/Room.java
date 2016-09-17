package swen222.niwa.model.world;

import swen222.niwa.model.entity.Entity;

import java.io.File;
import java.util.Set;

/**
 * Micro-level representation of the world. Stores geometry as an array of Tiles
 *
 * @author Marc
 */
public class Room { // extends Observable if we make it mutable, but unlikely

	public final int width;  // keep these fields final if we go for Rooms being immutable
	public final int height; // there doesn't seem like any good use case where these would need to change

	private final Tile[][] tiles; // each location l corresponds to tiles[l.row][l.col]

	private Set<Entity> entities;   // undecided about this one - this would be the only mutable field in this class;
									// locations store the room they correspond to so it wouldn't complicate much to
									// just have one big List of EVERY entity in the world - that would also simplify
									// moving between rooms, only the entity's position would need to be updated,
									// and not transfer it between collections

	/**
	 * Create a room from a specified File
	 * @param f a File containing XML Room data
	 * @return the newly created Room
	 */
	public static Room newFromFile(File f) {
		// TODO: write file parser, construct a new Room and fill in the Tiles
		// ensure that checks are in place so that there are no null Tiles
		return null;
	}

	/**
	 * Gets the Tile in this Room at a specified Location
	 * @param loc the Location to find the Tile at
	 * @throws IllegalArgumentException if the Location is a different Room
	 * @return the Tile at that Location
	 */
	public Tile tileAt(Location loc) {
		if (loc.room != this) throw new IllegalArgumentException("Location is a different Room");
		else {
			assert tiles[loc.row][loc.col] != null : String.format("Tile at %s is null", loc.toString());
			return tiles[loc.row][loc.col];
		}
	}

	private Room(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
	}

}
