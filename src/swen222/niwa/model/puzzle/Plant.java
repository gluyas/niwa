package swen222.niwa.model.puzzle;

import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Visible;
import swen222.niwa.model.world.Direction;

import java.awt.*;
import java.io.Serializable;
import java.util.function.Function;

/**
 * Plant class provides overridable behaviour.
 */
public class Plant implements Visible, Serializable {
	public final Type type;
	int hits = 0;
	private SpriteSet sprites;

	public Plant(Type type) {
		this.type = type;
	}

	/**
	 * Trigger this Plant's effect.
	 *
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
	 *
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
		return SPRITES[type.ordinal()].sprite(camera);
	}

	// CLASS IMPLEMENTATIONS - using suppliers as a dirty way of

	public enum Type {
		BASIC,
		PINWHEEL,
		ORCHID,
		LOTUS,
		IRIS,
		IRISMINI,
		GATEPLANT;

		public Plant make() {
			return TYPES[this.ordinal()].apply(this);
		}
	}

	/*
	Return a plant of the next type in the list of enums
	 */
	public Plant cycleType(Boolean right) {
		int typeNum = type.ordinal();
		if(right) {
			typeNum++;
		}
		else{
			typeNum--;
		}
		if(typeNum>=Type.values().length){
			typeNum = 0;
		}
		if(typeNum<0){
			typeNum=Type.values().length-1;
		}
		return (Type.values()[typeNum]).make();

	}

	public int getHits(){
		return hits;
	}


	// this array holds factory methods to produce new Plants, with each entry corresponding to a single Type value
	@SuppressWarnings("unchecked") //TODO: check the type!
	private static final Function<Type, Plant>[] TYPES = new Function[]{
			(Function<Type, Plant>) Plant::new,
			(Function<Type, Plant>) Pinwheel::new,
			(Function<Type, Plant>) Orchid::new,
			(Function<Type, Plant>) Lotus::new,
			(Function<Type, Plant>) Iris::new,
			(Function<Type, Plant>) IrisMini::new,
			(Function<Type, Plant>) GatePlant::new
	};

	//placeholder sprites
	private static final SpriteSet[] SPRITES = {
			SpriteSet.get("redFlower2"),
			SpriteSet.get("pinwheel"),
			SpriteSet.get("orchid"),
			SpriteSet.get("lotus"),
			SpriteSet.get("iris"),
			SpriteSet.get("iris-mini"),
			SpriteSet.get("gateflower")
	};

	public static class Pinwheel extends Plant {
		Pinwheel(Type type) {
			super(type);
		}

		@Override
		public Spell trigger(Spell spell) {
			if ((hits & 1) == 0 || hits==0) {
				spell = spell.dir(spell.dir.turnCW());
			} else{
				spell = spell.dir(spell.dir.turnCCW());
			}
			return super.trigger(spell);
		}

	}

	public static class Orchid extends Plant {
		Orchid(Type type) { super(type); }

	}

	public static class Lotus extends Plant {
		Lotus(Type type) {
			super(type);
		}

	}

	public static class Iris extends Plant {
		Iris(Type type) {
			super(type);
		}

		@Override
		public Spell trigger(Spell spell) {
			if ((hits & 1) == 0) {
				spell = spell.dir(spell.dir.turnCW());
			} else{
				spell = spell.dir(spell.dir.turnCCW());
			}
			return super.trigger(spell);
		}

	}

	public static class IrisMini extends Plant {
		IrisMini(Type type) {
			super(type);
		}

		@Override
		public Spell trigger(Spell spell) {
			if ((hits & 1) == 0) {
				spell = spell.dir(spell.dir.turnCW());
			} else{
				spell = spell.dir(spell.dir.turnCCW());
			}
			return super.trigger(spell);
		}

	}

	public static class GatePlant extends Plant {
		GatePlant(Type type) {super(type); }

	}



}
