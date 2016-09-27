package swen222.niwa.model.entity;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.world.Inventory;
import swen222.niwa.model.world.Location;

public class PlayerEntity extends Entity{

	public String name;
	public Inventory holding;

	public PlayerEntity(Location loc) {
		super(loc);
		holding =new Inventory();
	}

	@Override
	public Sprite sprite() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean pickUp(){
		if(!canPickUp()){
			return false;
		}
		return true;
	}

	public boolean canPickUp(){
		if(holding.isFull()){
			return false;
		}
		return true;

	}
}
