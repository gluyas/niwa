package swen222.niwa.model.world;

import com.sun.istack.internal.Nullable;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Top level representation of the game world. Stores all rooms and their geography.
 */
public class World implements Serializable {

	Room crossroads,niceForest;
	ArrayList<Room> rooms;
							// TODO: solve issues outlined here
	private Room[][] map;   // there are some tricky problems for this format - it's quite tricky to figure out where a
							// given room is in this map, which is necessary for finding adjacent rooms when travelling
							// linking rooms may actually be the simplest solution
	private int width;
	private int height;


	public static World newFromRandom(int width, int height) {
		World world = new World(width, height);
		world.rooms = new ArrayList<Room>();
		world.initialiseRooms();
		world.assignRooms();
		return world;
	}

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

	public World(int width, int height) {
		this.height=height;
		this.width=width;
		map= new Room[width][height];
	}

	private void initialiseRooms() {
		File[] maps = new File("resource/rooms").listFiles();
		for(File file: maps){
			rooms.add(Room.newFromFile(file));
		}
		Collections.shuffle(rooms);
	}

	private void assignRooms(){
		if(height*width>rooms.size()){
			System.out.println("Too big a map");
			return;
		}
		for(int x=0; x<width;x++){
			for(int y=0;y<height;y++){
				addRoom(x,y,rooms.get(0));
				rooms.remove(0);
				Collections.shuffle(rooms);
			}
		}
	}


	//for testing
	private void setRoom(int col, int row, Room r){
		map[col][row] = r;
	}

	public Room[][] getMap(){
		return map;
	}
	public void addRoom(int col, int row,Room room){
		map[col][row]=room;
		room.setPosition(col, row);
	}

	public Room getRoom(int x, int y){
		return map[x][y];
	private World(int width, int height) {
		this.width = width;
		this.height = height;
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

	//TODO: implement this
	public Location getSpawn() {
		return Location.at(roomAt(0, 0), 0, 0);
	}

}
