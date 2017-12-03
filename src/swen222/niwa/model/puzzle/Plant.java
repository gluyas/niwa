package swen222.niwa.model.puzzle;

import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Visible;
import swen222.niwa.model.world.Direction;

import java.util.function.Function;

/**
 * Plant class provides overridable behaviour.
 */
public class Plant implements Visible {
	public final Type type;
	int hits = 0;

	public Plant(Type type) {
		this.type = type;
	}

	/**
	 * Trigger this Plant's effect.
	 * @param spell the Spell frame that triggered this Plant
	 * @return a Spell instance with the modifications invoked by the Plant's effect
	 */
	public Spell trigger(Spell spell) {
		hits += 1;
		return spell;
	}

	/**
	 * Called when the Spell that triggered this Plant's effect fails. Reverts a single call of the trigger method.
	 * Only override if the Plant's trigger method causes side effects.
	 * @param spell the Spell that was returned from the trigger method
	 */
	public void undo(Spell spell) {
		hits -= 1;
	}

	/**
	 * Reset this Plant to its initial state.
	 * Only override if the subclass maintains local state.
	 */
	public void reset() {
		hits = 0;
	}

	public boolean activated() {
		return hits > 0;
	}

	@Override
	public Sprite sprite(Direction camera) {
		return null;
	}

	// CLASS IMPLEMENTATIONS - using suppliers as a dirty way of

	public enum Type {
		BASIC,
		PINWHEEL;

		public Plant make() {
			return TYPES[this.ordinal()].apply(this);
		}
	}

	// this array holds factory methods to produce new Plants, with each entry corresponding to a single Type value
	@SuppressWarnings("unchecked")
	private static final Function<Type, Plant>[] TYPES = new Function[]{
			(Function<Type, Plant>) Plant::new,
			(Function<Type, Plant>) Pinwheel::new,
	};

	private static final SpriteSet[] SPRITES = new SpriteSet[] {
			SpriteSet.get(""),
	};

	public static class Pinwheel extends Plant {
		Pinwheel(Type type) {
			super(type);
		}

		@Override
		public Spell trigger(Spell spell) {
			if (hits == 0) {
				spell = spell.dir(spell.dir.turnCW());
			} else if (hits == 1) {
				spell = spell.dir(spell.dir.turnCCW());
			}
			return super.trigger(spell);
		}
	}
}
