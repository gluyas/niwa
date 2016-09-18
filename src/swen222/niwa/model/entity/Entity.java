package swen222.niwa.model.entity;

import swen222.niwa.gui.Visible;
import swen222.niwa.model.world.Location;

import java.util.Observable;

/**
 * Superclass from which all game objects which can move around should extend.
 */
//TODO: write and design this (maybe make into an interface)
public abstract class Entity extends Observable implements Visible {

	protected Location loc;

}
