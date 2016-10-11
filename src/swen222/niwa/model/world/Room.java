package swen222.niwa.model.world;

import swen222.niwa.file.RoomParser;

import java.io.File;
import java.io.Serializable;

/**
 * Micro-level representation of the world. Stores geometry as an array of Tiles
 *
 * @author Marc
 */
public class Room implements Serializable { // extends Observable if we make it mutable, but unlikely

	public final String name; //each room needs a name, may display this on GUI possibly

	// Where this room sits w
	public final int worldRow;
	public final int worldCol;

	public final int width;  // keep these fields final if we go for Rooms being immutable
	public final int height; // there doesn't seem like any good use case where these would need to change

	private Tile[][] tiles; // each location l corresponds to tiles[l.row][l.col]

	//private EntityTable<Entity> entities;   // undecided about this one - this would be the only mutable field in this class;
									// locations store the room they correspond to so it wouldn't complicate much to
									// just have one big List of EVERY entity in the world - that would also simplify
									// moving between rooms, only the entity's position would need to be updated,
									// and not transfer it between collections

	/**
	 * Gets the Tile in this RoomTuple at a specified Location
	 * @param loc the Location to find the Tile at
	 * @throws IllegalArgumentException if the Location is a different RoomTuple
	 * @return the Tile at that Location
	 */
	public Tile tileAt(Location loc) {
		if (loc.room != this) throw new IllegalArgumentException("Location is a different Room");
		else {
			assert tiles[loc.row][loc.col] != null : String.format("Tile at %s is null", loc.toString());
			return tiles[loc.row][loc.col];
		}
	}

	/**
	 * Create a room from a specified File
	 * @param f a File containing XML RoomTuple data
	 * @return the newly created RoomTuple
	 */
	public static Room newFromFile(File f, int worldCol, int worldRow) {

		RoomParser parser = new RoomParser(f);
		int width = parser.width;
		int height = parser.height;

		Room room = new Room("Default", worldCol, worldRow, width, height); //TODO: add name to the RoomTuple schema

		room.tiles = parser.getTiles();

		Prop[][] props = parser.getProps();
		for(int row = 0; row<height; row++){
			for(int col = 0; col<width; col++){
				if(props[row][col]!=null){
					room.tiles[row][col].addProp(props[row][col]);
				}
			}
		}

		return room;
	}

	// TODO: remove this - exists for testing purposes
	public static Room emptyRoom(int w, int h) {
		return new Room("Empty Room", 0, 0, w, h);
	}

	private Room(String name, int worldCol, int worldRow, int width, int height) {
		this.worldCol = worldCol;
		this.worldRow = worldRow;
		this.width = width;
		this.height = height;
		this.name = name;
		//this.entities = new HashEntityTable<>();
	}

}
