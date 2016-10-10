package swen222.niwa.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import swen222.niwa.model.world.World;

/**
 * @author burnshami
 *
 */
public class WorldTests {

	World w1;
	World w2;


	@Test
	public void test01(){
		w1= World.newFromRandom(1, 1);
		w2= World.newFromRandom(1, 1);
		assertFalse(w1.getRoom(0, 0).equals(w2.getRoom(0, 0)));
	}
}
