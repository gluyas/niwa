package swen222.niwa.model.world;

import swen222.niwa.file.RoomParser;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.entities.Door;
import swen222.niwa.model.entity.entities.Rune;
import swen222.niwa.model.entity.entities.RuneStone;
import swen222.niwa.model.entity.entities.Seed;
import swen222.niwa.model.entity.entities.Statue;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observer;
import java.util.Set;
import java.io.Serializable;

/**
 * Micro-level representation of the world. Stores geometry as an array of Tiles
 *
 * @author Marc
 * @author Jack U
 */
public class Room implements Serializable { // extends Observable if we make it mutable, but unlikely

	public final String name; //each room needs a name, may display this on GUI possibly

	// Where this room sits w
	public final int worldRow;
	public final int worldCol;

	public final int width;  // keep these fields final if we go for Rooms being immutable
	public final int height; // there doesn't seem like any good use case where these would need to change

	private Tile[][] tiles; // each location l corresponds to tiles[l.row][l.col]

	public static Location[] spawnLocs; // the locations of the areas the player can enter from, NESW

	public static EntityTable<Entity> entities;   // undecided about this one - this would be the only mutable field in this class;
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
	public static Room newFromFile(File f, int worldCol, int worldRow) {

	public static Room newFromFile(File f) {
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

		Room room = RoomBuilder.buildRoom(parser);


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
		this.entities = new HashEntityTable<>();
	}

	public static class RoomBuilder{


		private static Room buildRoom(RoomParser parser){

			int width = parser.width;
			int height = parser.height;

			String name = parser.getName();

			Room room = new Room(name, width, height);
			// provide the parser a reference to the room so that it can set up locations
			parser.setLocationRoom(room);
			room.tiles = parser.getTiles();

			Prop[][] props = parser.getProps();
			for(int row = 0; row<height; row++){
				for(int col = 0; col<width; col++){
					if(props[row][col]!=null){
						room.tiles[row][col].addProp(props[row][col]);
					}
				}
			}

			// add the entities
			entities = parser.getEntities();

			spawnLocs = new Location [4];

			//needs to read through spawns and convert them into locations
			int[][] spawns = parser.getSpawns();
			for(int i = 0; i< 4; i++){
				int[] coord = spawns[i];
				spawnLocs[i]= Location.at(room,coord[0],coord[1]);
			}


			return room;
		}

	}

}
