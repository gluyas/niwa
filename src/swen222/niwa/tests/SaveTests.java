package swen222.niwa.tests;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import swen222.niwa.model.world.Prop;
import swen222.niwa.model.world.Room;
import swen222.niwa.file.RoomParser;
import swen222.niwa.file.SaveParser;
import swen222.niwa.model.world.Tile;

/**
 * Class for testing parsing rooms using an xml file.
 * @author Jack U
 *
 */
public class SaveTests {



	@Before
	public void setup(){
		
	}


	@Test
	public void testParse(){
		SaveParser p = new SaveParser();
	}
	
	public void testMapSave(){
		SaveParser p = new SaveParser();
		
		//Room [][] testMap = new Room[1][1];
		//testMap[0][0]=new Room
		
		//p.saveMap 
	}




}
