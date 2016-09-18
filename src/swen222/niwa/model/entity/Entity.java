package swen222.niwa.model.entity;

import swen222.niwa.gui.Visible;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

import java.util.Observable;

/**
 * Superclass from which all game objects which can move around should extend.
 *
 * @author Marc
 */
//TODO: write and design this (maybe make into an interface)
public abstract class Entity extends Observable implements Visible {

	protected Location loc;

	/**
	 * Attempts to move this Entity a step in a specified Direction. Not sensitive to game rules - will only fail
	 * if the new Location is out of bounds.
	 *
	 * @param d the Direction to step in
	 * @return false if the move was unsuccessful; else true
	 */
	// TODO: Hamish B please review - game rule implementation; move functionality to appropriate class if necessary
	// this method is not sensitive to game rules - could be fine, but maybe move this method somewhere than can enforce
	public boolean move(Direction d) {
		try {
			setLocation(loc.move(d)); // this method will notify observers for us
			return true;
		} catch (Location.InvalidLocationException invalidMove) {
			return false;
		}
	}

	/**
	 * Set this Entity's location. Updates Observers
	 * @param newLoc new Location
	 */
	// TODO: maybe remove public access to this method, instead use ones that are sensitive to rules?
	public void setLocation(Location newLoc) {
		this.loc = newLoc;
		setChanged();
		notifyObservers(loc); // using loc as the update arg here - may not be used, maybe use an enum or something
		// notifyObservers(Update.MOVED) // something like this maybe? an enum is easy for Observers to check what it is
	}

	/**
	 * @return this Entity's current Location
	 */
	public Location getLocation() {
		return this.loc;
	}

}
