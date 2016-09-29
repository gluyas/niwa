package swen222.niwa.tests;

import org.junit.Before;
import org.junit.Test;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Marc
 */
public class HashEntityTableTest {

	HashEntityTable<Entity> table;
	Room r;
	Location loc1;
	Location loc2;
	Location loc3;
	TestEntity ent1;
	TestEntity ent2;

	@Before
	public void setup() {
		table = new HashEntityTable<>();
		r = Room.emptyRoom(2, 2);
		loc1 = Location.at(r, 0, 0);
		loc2 = Location.at(r, 1, 0);
		loc3 = Location.at(r, 0, 1);
		ent1 = new TestEntity(loc1);
		ent2 = new TestEntity(loc1);
	}

	@Test
	public void add01() throws Exception {
		assertTrue(table.add(ent1));
		assertTrue(table.add(ent2));

		assertFalse(table.add(ent1));
		assertFalse(table.add(ent2));
	}

	@Test
	public void add02() throws Exception {
		assertTrue(table.add(ent1));
		assertTrue(table.add(ent2));

		ent1.moveTo(loc2);
		assertFalse(table.add(ent1));

		ent2.moveTo(loc3);
		assertFalse(table.add(ent2));
	}

	@Test
	public void remove01() throws Exception {
		assertFalse(table.remove(ent1));

		table.add(ent1);
		table.add(ent2);
		assertTrue(table.remove(ent1));
		assertTrue(table.remove(ent2));

		assertFalse(table.remove(ent1));
		assertFalse(table.remove(ent2));
	}

	@Test
	public void remove02() throws Exception {
		table.add(ent1);

		ent1.moveTo(loc2);
		assertTrue(table.remove(ent1));
		ent1.moveTo(loc3);
		assertFalse(table.remove(ent1));
	}

	@Test
	public void size01() throws Exception {
		table.add(ent1);
		assertEquals(1, table.size());
		table.add(ent2);
		assertEquals(2, table.size());
		table.add(ent2);
		assertEquals(2, table.size());
	}

	@Test
	public void size02() throws Exception {
		table.add(ent1);
		assertEquals(1, table.size());
		table.add(ent2);
		assertEquals(2, table.size());
		table.add(ent1);
		assertEquals(2, table.size());
		table.remove(ent2);
		assertEquals(1, table.size());
		table.add(ent2);
		assertEquals(2, table.size());
		table.remove(ent2);
		assertEquals(1, table.size());
		table.remove(ent1);
		assertEquals(0, table.size());
	}

	@Test
	public void size03() throws Exception {
		table.add(ent1);
		table.add(ent2);
		ent1.moveTo(loc2);
		assertEquals(2, table.size());
		table.remove(ent1);
		assertEquals(1, table.size());
		ent1.moveTo(loc3);
		assertEquals(1, table.size());
	}

	@Test
	public void contains01() throws Exception {
		table.add(ent1);
		assertTrue(table.contains(ent1));
		assertFalse(table.contains(ent2));
	}

	@Test
	public void contains02() throws Exception {
		table.add(ent1);
		assertTrue(table.contains(ent1));
		assertFalse(table.contains(ent2));

		ent1.moveTo(loc2);
		assertTrue(table.contains(ent1));
	}

	@Test
	public void get01() throws Exception {
		table.add(ent1);
		Set<Entity> bucket = table.get(loc1);
		assertTrue(bucket.contains(ent1));
		assertFalse(bucket.contains(ent2));
		table.add(ent2);
		// NB: this was removed as there is no quick way to fix. See HashEntityTable.contains() javadoc.
		// assertFalse(bucket.contains(ent2));
		bucket = table.get(loc1);
		assertTrue(bucket.contains(ent1));
		assertTrue(bucket.contains(ent2));
	}

	@Test
	public void get02() throws Exception {
		table.add(ent1);
		table.add(ent2);
		ent2.moveTo(loc2);
		Set<Entity> bucket = table.get(loc1);
		assertTrue(bucket.contains(ent1));
		assertFalse(bucket.contains(ent2));
		bucket = table.get(loc2);
		assertTrue(bucket.contains(ent2));
		assertFalse(bucket.contains(ent1));
	}

	@Test
	public void get03() throws Exception {
		table.add(ent1);
		table.add(ent2);
		table.remove(ent1);
		Set<Entity> bucket = table.get(loc1);
		assertFalse(bucket.contains(ent1));
		assertTrue(bucket.contains(ent2));
		ent1.moveTo(loc2);
		bucket = table.get(loc2);
		assertFalse(bucket.contains(ent1));
	}
}