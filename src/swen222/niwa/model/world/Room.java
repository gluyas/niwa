package swen222.niwa.model.world;

import swen222.niwa.file.RoomParser;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.Tile.Texture;
import swen222.niwa.model.world.Tile.TileType;

import java.io.File;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Set;

/**
 * Micro-level representation of the world. Stores geometry as an array of Tiles
 *
 * @author Marc
 */
public class Room { // extends Observable if we make it mutable, but unlikely

	public final String name; //each room needs a name, may display this on GUI possibly

	public final int width;  // keep these fields final if we go for Rooms being immutable
	public final int height; // there doesn't seem like any good use case where these would need to change

	private Tile[][] tiles; // each location l corresponds to tiles[l.row][l.col]

	public static Location[] spawnLocs;

	private EntityTable<Entity> entities;   // undecided about this one - this would be the only mutable field in this class;
									// locations store the room they correspond to so it wouldn't complicate much to
									// just have one big List of EVERY entity in the world - that would also simplify
									// moving between rooms, only the entity's position would need to be updated,
									// and not transfer it between collections

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

	/**
	 * @return an EntityTable containing all Entities that are currently in this Room.
	 */
	public EntityTable<? extends Entity> getEntityTable() {
		// TODO: implement this - depends on design decisions regarding entity storage
		//return null;
		return this.entities;
	}

	//TODO: javadoc!
	public boolean addEntity(Entity e) {
		return entities.add(e);
	}

	public boolean removeEntity(Object o) {
		return entities.remove(o);
	}

	/**
	 * Create a room from a specified File
	 * @param f a File containing XML Room data
	 * @return the newly created Room
	 */
	public static Room newFromFile(File f) {

		RoomParser parser = new RoomParser(f);
		int width = parser.width;
		int height = parser.height;


		Room room = new Room("Default", width,height); //TODO: add name to the Room schema

		room.tiles = parser.getTiles();

		Prop[][] props = parser.getProps();
		for(int row = 0; row<height; row++){
			for(int col = 0; col<width; col++){
				if(props[row][col]!=null){
					room.tiles[row][col].addProp(props[row][col]);
				}
			}
		}

		spawnLocs = new Location [4];

		//needs to read through spawns and convert them into locations
		int[][] spawns = parser.getSpawns();
		for(int i = 0; i< 4; i++){
			int[] coord = spawns[i];
			spawnLocs[i]= Location.at(room,coord[0],coord[1]);
		}

		return room;
	}



	// TODO: remove this - exists for testing purposes
	public static Room emptyRoom(int w, int h) {
		return new Room("Empty Room", w, h);
	}

	public static void addEntity(Location loc, Entity player){
		player.setLocation(loc);
	}

	private Room(String name, int width, int height) {
		this.width = width;
		this.height = height;
		this.name = name;
		this.entities = new HashEntityTable<>();
	}

}
