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
import swen222.niwa.model.world.Tile.Texture;
import swen222.niwa.model.world.Tile.TileType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

	public int col;
	public int row;

	private Tile[][] tiles; // each location l corresponds to tiles[l.row][l.col]

	public static Location[] spawnLocs; // the locations of the areas the player can enter from, NESW

	public EntityTable<Entity> entities;   // undecided about this one - this would be the only mutable field in this class;
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

	public void setPosition(int x, int y){
		col=x;
		row=y;
	}

	/**
	 *
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
		Room room = RoomBuilder.buildRoom(parser);

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

	public static class RoomBuilder{


		private static Room buildRoom(RoomParser parser){

			int width = parser.width;
			int height = parser.height;

			String name = parser.getName();

			Room room = new Room(name, width,height);

			room.tiles = parser.getTiles();

			Prop[][] props = parser.getProps();
			for(int row = 0; row<height; row++){
				for(int col = 0; col<width; col++){
					if(props[row][col]!=null){
						room.tiles[row][col].addProp(props[row][col]);
					}
				}
			}

			//creating entities - currently represented by a set
			Collection<Entity> entities = new HashSet<Entity>();

			//needs to convert string into entities
			String[][] strings = parser.getEntities();
			for(int row = 0; row<height; row++){
				for(int col = 0; col<width; col++){
					if(strings[row][col]!=null){
						entities.add(stringToEntity(strings[row][col],row,col,room));
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


		//TODO: Make these able to take in different facing sprites and construct them
		//appropriately
		private static Entity stringToEntity(String s, int row, int col, Room r){

			Location loc = Location.at(r, col, row);

			switch(s){

			case "seed":
				return new Seed(loc);

			case "door":
				return null;

			case "rune":
				return new Rune(loc);

			case "runeStone":
				return new RuneStone(loc);

			case "statue":
				return new Statue(loc);

			default:
				throw new Error("Could not find an entity of the name" + s + ", please " +
			    "double check the naming convention in the 'Room' class");


			}

		}

	}

}
