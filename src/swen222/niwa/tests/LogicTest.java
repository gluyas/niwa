package swen222.niwa.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.EntityTable;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.Rules;

public class LogicTest {

	EntityTable<Entity> table;
	Room r;
	Location loc1;
	Location loc2;
	Location loc3;
	Location loc4;
	TestEntity ent1;
	TestEntity ent2;
	Rules logic;


	@Before
	public void setup() {
		table = new EntityTable<>();
		File testRoom1 = new File("rooms/testRoom.xml");
		r= Room.newFromFile(testRoom1);
		r.addEntity(Location.at(r, 0, 0), ent1);
		logic = new Rules(table);
	}

	@Test
	public void move01(){
		logic.move(ent1, Direction.SOUTH);
		assertTrue(ent1.getLocation().equals(Location.at(r, 0, 1)));
	}

}

