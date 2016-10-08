package swen222.niwa.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.entities.PlayerEntity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;
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
	PlayerEntity ent1;
	Rules logic;


	@Before
	public void setup() {

		table = new HashEntityTable<>();
		File testRoom1 = new File("resource/rooms/testRoom.xml");
		r= Room.newFromFile(testRoom1);
		ent1 = new PlayerEntity(Location.at(r,0,0));
		table.add(ent1);
		logic = new Rules(table);
	}

	@Test
	public void move01(){
		logic.move(ent1, Direction.SOUTH);
		assertTrue(ent1.getLocation().equals(Location.at(r, 0, 1)));
	}

	@Test
	public void tryMoveUp(){
		logic.move(ent1, Direction.SOUTH);
		logic.move(ent1, Direction.EAST);
		assertTrue(ent1.getLocation().equals(Location.at(r,0,1)));
	}

}

