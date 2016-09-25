package swen222.niwa.tests;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import swen222.niwa.model.world.RoomParser;
import swen222.niwa.model.world.Tile;

/**
 * Class for testing parsing rooms using an xml file.
 * @author Jack U
 *
 */
public class ParserTests {
	
	File f;
	RoomParser parser;
	
	@Before
	public void setup(){
		f = new File("rooms/testRoom.xml");
		parser = new RoomParser(f);
		
	}
	
	
	@Test
	public void testWidth(){
		if(parser==null){System.out.println("null");}
		assert(parser.getWidth()==5);
	}
	
	@Test
	public void testHeight(){
		assert(parser.getHeight()==5);		
	}
	
	@Test
	public void testTileCreation(){
		Tile[][] tileList = parser.getTiles();
		assert(tileList[0][0].getType().equals(Tile.TileType.GRASSTILE));
		assert(tileList[4][4].getType().equals(Tile.TileType.GRASSTILE));
		assert(tileList.length==5);
		assert(tileList[0].length==5);
	}
	

}
