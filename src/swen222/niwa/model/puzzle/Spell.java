package swen222.niwa.model.puzzle;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Tile;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Represents a single frame of a Spell being cast
 */
public class Spell implements Iterable<Spell> {

	private static final Tile.Type[] SPELL_CONDUCTORS = {Tile.Type.GRASS, Tile.Type.DIRT};

	public final Location loc;
	public final Direction dir;

	public final Spell parent;
	public final Puzzle.Cell terminal;

	private final Consumer<Spell> undo;

	private Spell(Spell parent, Direction dir, Location loc, Puzzle.Cell terminal, Consumer<Spell> undo) {
		this.loc = loc;
		this.dir = dir;
		this.parent = parent;
		this.terminal = terminal;
		this.undo = undo;
	}

	/**
	 * Casts a Spell. This method is responsible for all game state changes it causes.
	 *
	 * @param entities 	An EntityTable containing the entities in the Room
	 * @param origin	The Location for the Spell to start from.
	 * @param direction	The Direction for the Spell to move in
	 * @return 			The final Spell in the chain. If the invocation was unsuccessful, this will have a
	 * 					<code>terminal</code> value of <code>null</code>.
	 */
	public static Spell invoke(EntityTable<? super Entity> entities, Location origin, Direction direction) {
		Spell fringe = new Spell(null, direction, origin, null, null);
																	// TILE LOGIC
		if (!validTile(fringe)) return fringe;						// no previous tile on 0th iteration

		while (true) {
			Puzzle.Cell cell = entities.getAny(fringe.loc, Puzzle.Cell.class);
			if (cell != null) {
				Location oldLoc = fringe.loc;
				fringe = cell.trigger(fringe);						// perform cell and plant logic

				if (fringe.loc != oldLoc) {                        	// if fringe was moved, skip movement logic
					if (validTile(fringe)) {
						continue;
					} else {
						undoAll(fringe);
						return fringe;								// return bad position to show Plant-induced move
					}
				}

				if (cell.wall(fringe.dir)) {						// MOVEMENT LOGIC
					if (cell.puzzle().capture(fringe)) {
						return fringe.terminal(cell);				// successful invocation
					}
				}
			}

			try {													// advance the iteration
				Location nextLoc = fringe.loc.move(fringe.dir);
				fringe = fringe.child(fringe.dir, nextLoc);

				if (!validTile(fringe)) {
					undoAll(fringe);
					return fringe.parent;							// return Spell from before it goes onto bad tile
				}
			} catch (Location.InvalidLocationException e) {
				undoAll(fringe);									// spell fails if it can't move
				return fringe;
			}
		}
	}

	private static boolean validTile(Spell fringe) {
		return Arrays.stream(SPELL_CONDUCTORS).anyMatch(t -> t == fringe.loc.tile().type);
	}

	private static void undoAll(Spell fringe) {
		for (Spell spell : fringe) {								// applied in reverse chronological order
			if (spell.undo != null) spell.undo.accept(spell);
		}
	}

	// PSEUDO-BUILDER METHODS (for use in Plant effects)

	public Spell dir(Direction dir) {
		dir = (dir == null) ? this.dir : dir;
		return new Spell(this.parent, dir, this.loc, this.terminal, this.undo);
	}

	public Spell loc(Location loc) {
		loc = (loc == null) ? this.loc : loc;
		return new Spell(this.parent, this.dir, loc, this.terminal, this.undo);
	}

	public Spell undo(Consumer<Spell> undo) {
		return new Spell(this.parent, this.dir, this.loc, this.terminal, undo);
	}

	public Spell child(Direction dir, Location loc) {
		dir = (dir == null) ? this.dir : dir;
		loc = (loc == null) ? this.loc : loc;
		return new Spell(this, dir, loc, null, null);
	}

	private Spell terminal(Puzzle.Cell terminal) {
		return new Spell(this.parent, this.dir, this.loc, terminal, undo);
	}

	@Override
	public Iterator<Spell> iterator() {
		return new Iterator<Spell>() {
			Spell next = Spell.this;

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public Spell next() {
				if (!hasNext()) throw new NoSuchElementException();
				Spell prev = next;
				next = next.parent;
				return prev;
			}
		};
	}
}
