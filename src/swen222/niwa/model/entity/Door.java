package swen222.niwa.model.entity;

import java.util.ArrayList;

import swen222.niwa.gui.graphics.SpriteSet;

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.util.Update;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 * Door that can be opened when all the statues have been occupied in the room
 *
 */
public class Door extends ChangingEntity {

	private static final SpriteSet SPRITES_OPEN = SpriteSet.get("openDoor");
	private static final SpriteSet SPRITES_CLOSED = SpriteSet.get("closedDoor");
	private ArrayList<Statue> statues;
	private final String closedDesc = "This door is shut tight.";
	private final String openDesc = "An open door can mean a world of possibilities.";

	private boolean open;
	private Direction facing;


	public Door(Location loc, ArrayList<Statue> triggers, Direction facing){
		super(loc);
		this.statues = triggers;
		this.facing = facing;
		this.setOpen(false);
	}
	@Override
	public Sprite sprite(Direction camera) {
		if (isOpen()) return SPRITES_OPEN.facing(facing).sprite(camera);
		else return SPRITES_CLOSED.facing(facing).sprite(camera);
	}

	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
		if(open){
			this.setDescription(openDesc);
		} else {
			this.setDescription(closedDesc);
		}
		setChanged();
		notifyObservers((Update)()->this.setOpen(open));
	}

	public ArrayList<Statue> getStatues(){
		return statues;
	}

	/**
	 * Checks if all the statues are triggered
	 * @return
	 */
	public boolean checkStatues(){
		System.out.println("Statues"+statues.size());
		for(Statue statue: statues){
			if(!statue.isTriggered()){

				return false;
			}
		}
		System.out.println("Statues all triggered");
		return true;
	}


}
