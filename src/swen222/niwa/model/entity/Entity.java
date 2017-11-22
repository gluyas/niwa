package swen222.niwa.model.entity;

import swen222.niwa.gui.graphics.Visible;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.util.Update;

import java.io.Serializable;
import java.util.Observable;

/**
 * Superclass from which all game objects which can move around should extend. Subclasses must manage the Location
 * field through methods provided by this class.
 *
 * @author Marc
 */
public abstract class Entity extends Observable implements Visible, Serializable{

	private final boolean fixedPosition;
	private Location loc; // private so that notifyObservers in moveTo cannot be omitted
	private String description; // the flavour text description of this Entity

	/**
	 * Create a new Entity at the given Location
	 * @param loc starting Location for the Entity
	 */
	public Entity(Location loc) {
		this.fixedPosition = false;
		this.loc = loc;
	}

	public Entity(Location loc, boolean fixedPosition) {
		this.fixedPosition = fixedPosition;
		this.loc = loc;
	}

	/**
	 * Set this Entity's location. If it changes, updates Observers with the return value as the argument.
	 * @param newLoc new Location
	 * @return the previous Location of this Entity
	 */
	// TODO: maybe remove public access to this method, instead use ones that are sensitive to rules?
	// this should probably remain as a lower access base method though - responsible for some important functions.
	// perhaps make a MovementRule strategy or something, that each Entity can have reference to, and easily check here.
	public final Location setLocation(Location newLoc) { // final method to prevent notifyObservers omission
		if (fixedPosition) throw new AssertionError("Attempted to move fixed Entity" + this);
		//System.out.println("setting location "+this);
		if (newLoc.equals(loc)) return loc; // don't want to update if nothing happens
		Location oldLoc = loc;
		this.loc = newLoc;
		setChanged();
		notifyObservers(new LocationUpdate(oldLoc, newLoc)); // this is tightly coupled with HashEntityTable. please be careful about making changes
		return oldLoc;
	}

	/**
	 * @return this Entity's current Location
	 */
	public final Location getLocation() {
		return this.loc;
	}

	/**
	 * Attempts to move this Entity a step in a specified Direction. Not sensitive to game rules - will only fail
	 * if the new Location is out of bounds. Updates Observers with the previous position value.
	 *
	 * @param d the Direction to step in
	 * @return false if the move was unsuccessful; else true
	 */
	// TODO: Hamish B please review - game rule implementation; move functionality to appropriate class if necessary
	// this method is not sensitive to game rules - could be fine, but maybe move this method somewhere than can enforce
	public boolean move(Direction d) {
		try {
			Location newLoc = loc.move(d);
			setLocation(newLoc); // this method will notify observers for us
			return true;
		} catch (Location.InvalidLocationException invalidMove) {
			return false;
		}
	}

	/**
	 * Get this Entity's description
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Sets the description of this Entity
	 * @param s
	 */
	public void setDescription(String s){
		description = s;
	}

	public class LocationUpdate implements Update {

		public final Location from;
		public final Location to;

		public LocationUpdate(Location from, Location to) {
			this.from = from;
			this.to = to;
		}

		public Entity subject() {
			return Entity.this;
		}

		@Override
		public void apply() {
			Entity.this.setLocation(to);
		}

		@Override
		public String toString() {
			return String.format("LocUD: %s from:%s to:%s", Entity.this, from, to);
		}
	}

}
