package swen222.niwa.tests;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import swen222.niwa.model.world.Prop;
import swen222.niwa.model.world.RoomParser;
import swen222.niwa.model.world.Tile;

/**
 * Class for testing parsing rooms using an xml file.
 * @author Jack U
 *
 */
public class ParserTests {
	
	File testRoom1;
	File testRoom2;
	RoomParser parser;
	RoomParser parser2;
	
	@Before
	public void setup(){
		testRoom1 = new File("rooms/testRoom.xml");
		testRoom2 = new File("rooms/testRoom2.xml");
		parser = new RoomParser(testRoom1);
		parser2 = new RoomParser(testRoom2);
		
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
		
		assert(tileList[2][2].getHeight()==2);
	}
	
	@Test
	public void testTileCreation2(){
		Tile[][] tileList = parser2.getTiles();
		
		assert(tileList[2][2].getType().equals(Tile.TileType.WATERTILE));
		assert(tileList[4][3].getType().equals(Tile.TileType.STONETILE));
		
		assert(tileList[2][3].getHeight()==2);
	}
	
	@Test
	public void testPropCreation1(){
		Prop[][] props = parser.getProps();
		
		assert(props[0][1].getType().equals(Prop.PropType.BIGTREE));
		assert((props[3][2]==null));
		
	}
	

}
