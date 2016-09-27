package swen222.niwa.model.entity;

import java.util.ArrayList;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.world.Inventory;
import swen222.niwa.model.world.Location;

public class PlayerEntity extends Entity{

	public String name;
	public ArrayList<Entity> inventory;
	public int inventoryCapacity = 3;

	public PlayerEntity(Location loc) {
		super(loc);
		inventory= new ArrayList<Entity>();
	}

	@Override
	public Sprite sprite() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addItem(Entity item){
		inventory.add(item);
	}
	
	
	public boolean canPickUp(){
		if(inventory.size()==inventoryCapacity){
			return false;
		}
		return true;

	}
}
