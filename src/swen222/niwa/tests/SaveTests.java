package swen222.niwa.tests;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import swen222.niwa.model.entity.entities.PlayerEntity;
import swen222.niwa.model.entity.entities.Seed;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Prop;
import swen222.niwa.model.world.Room;
import swen222.niwa.file.RoomParser;
import swen222.niwa.file.SaveParser;
import swen222.niwa.file.SpriteLoader;
import swen222.niwa.model.world.Tile;

/**
 * Class for testing parsing rooms using an xml file.
 * @author Jack U
 *
 */
public class SaveTests {

	File crag = new File("resource/rooms/crag.xml");
	File desertBowl = new File("resource/rooms/desertBowl.xml");
	File mountain = new File("resource/rooms/mountain.xml");
	File valley2 = new File("resource/rooms/valley2.xml");

	SaveParser p = new SaveParser();

	@Test
	public void testSaving(){

		//testMapSave();
		//testPlayerSave();
		testEntitySave();


	}



	public void testParse(){
		SaveParser p = new SaveParser();
		p.writeSave();
	}



	public void testMapSave(){
		SaveParser p = new SaveParser();

		Room [][] testMap = new Room[2][2];

		testMap[0][0]= Room.newFromFile(crag);
		testMap[0][1]= Room.newFromFile(crag);
		testMap[1][0]= Room.newFromFile(mountain);
		testMap[1][1]= Room.newFromFile(valley2);

		p.saveMap(testMap,2,2);

	    p.writeSave();

	}

	public void testPlayerSave(){

		SaveParser p = new SaveParser();

		PlayerEntity testPlayer = new PlayerEntity(Location.at(Room.newFromFile(mountain),3,3),SpriteLoader.get("ghostred"), "Donny");
		System.out.println("player created");

		PlayerEntity players [] = new PlayerEntity [1];
		players[0]=testPlayer;

		p.savePlayers(players);
		System.out.println("players saved");

		p.writeSave();

	}

	public void testEntitySave(){

		Seed s1 = new Seed(Location.at(Room.newFromFile(mountain),2,2),SpriteLoader.get("seed"));
		Seed s2 = new Seed(Location.at(Room.newFromFile(mountain),2,2),SpriteLoader.get("seed"));

		HashEntityTable entities = new HashEntityTable();
		entities.add(s1);
		entities.add(s2);


		SaveParser p = new SaveParser();

		p.saveEntities(entities);





	}




}
