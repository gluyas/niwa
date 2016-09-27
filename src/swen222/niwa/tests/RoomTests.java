package swen222.niwa.tests;

import java.io.File;

import org.junit.Test;

import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.Tile;

/**
 * Class for testing the creation of rooms
 * @author Jack U
 *
 */
public class RoomTests {
	
	File testRoom1 = new File("rooms/testRoom.xml");
	File testRoom2 = new File("rooms/testRoom2.xml");
	
	
	@Test
	public void testRoom1(){
		Room r = Room.newFromFile(testRoom1);
		assert(r.tileAt(Location.at(r,0,0)).getType().equals(Tile.TileType.GRASSTILE));
		assert(r.tileAt(Location.at(r,4,4)).getType().equals(Tile.TileType.GRASSTILE));
	}
	

}
