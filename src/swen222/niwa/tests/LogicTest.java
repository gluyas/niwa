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
import swen222.niwa.model.world.World;

public class LogicTest {


	Room r,r2;
	PlayerEntity ent1;
	Rules logic;
	World world;


	@Before
	public void setup() {


		r= Room.newFromFile(new File("resource/rooms/logicTestRoom.xml"));
		r2= Room.newFromFile(new File("resource/rooms/desertBowl.xml"));
		world= new World(1,2);
		world.addRoom(0,1,r);
		world.addRoom(0, 0, r2);
		ent1 = new PlayerEntity(Location.at(r,0,0));
		logic = new Rules(world.getMap());
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

	@Test
	public void changeRoom(){
		System.out.println("started: " +ent1.getLocation().toString());
		logic.move(ent1,Direction.NORTH);
		System.out.println(ent1.getLocation().toString());
		assertTrue(ent1.getLocation().getRoom().equals(r2));
		
	}

}

