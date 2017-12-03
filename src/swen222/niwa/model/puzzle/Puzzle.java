package swen222.niwa.model.puzzle;

import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public final class Puzzle {

	private static final SpriteSet testSprite = SpriteSet.get("blackFlower1");

	private final List<Cell> cells = new ArrayList<>();

	private State state;

	public State state() {
		return state;
	}

	/**
	 * Attempt to capture this Puzzle with a given Spell
	 * @param spell the Spell to capture with
	 * @return true if this was successfully captured; false if already captured
	 */
	public boolean capture(Spell spell) {
		if (state == State.VIRGIN) {
			state = State.CAPTURED;
			checkComplete();
			return true;
		} else return false;
	}

	private void reset() {
		cells.stream()
				.map(cell -> cell.plant)
				.forEach(Plant::reset);
	}

	private void checkComplete() {
		if (state == State.CAPTURED && cells.stream().allMatch(Cell::activated)) {
			state = State.COMPLETE;
		}
	}

	/**
	 * Entity used to define Puzzle layout, and holds relevant data
	 */
	public final class Cell extends Entity {	// TODO: get the Cells into the level's EntityTable

		private final BitSet walls = new BitSet(Direction.values().length);
		private final boolean rooted;

		private Plant plant;

		private Cell(Location loc, boolean rooted, Direction ... walls) {
			super(loc, true);
			cells.add(this);

			this.rooted = rooted;

			for (Direction wall : walls) {
				this.walls.set(wall.ordinal());
			}
		}

		/**
		 * Entry point for an invoked spell
		 * @param spell the Spell that enters this Cell
		 * @return a modified Spell object with the changes imparted by the Plant
		 */
		public Spell trigger(Spell spell) {
			if (plant != null) {
				spell = plant.trigger(spell);
				checkComplete();
			}
			return spell;
		}

		/**
		 * Removes the Plant in this Cell, if permitted
		 * @return the Plant type that was held; null if none present or not permitted
		 */
		public Plant.Type uproot() {
			if (!rooted && plant != null && Puzzle.this.state == State.VIRGIN) {
				Plant.Type type = plant.type;
				plant = null;
				return type;
			} else return null;
		}

		private void undo(Spell spell) {

		}

		public boolean wall(Direction move) {
			return walls.get(move.ordinal());
		}

		public Puzzle puzzle() {
			return Puzzle.this;
		}

		public boolean activated() {
			return plant != null && plant.activated();
		}

		@Override
		public Sprite sprite(Direction camera) {
			return testSprite.sprite(camera);
		}
	}

	public enum State {
		VIRGIN,
		CAPTURED,
		COMPLETE,
	}
}
