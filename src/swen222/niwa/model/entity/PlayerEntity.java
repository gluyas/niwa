package swen222.niwa.model.entity;

import java.util.ArrayList;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.world.Location;

public class PlayerEntity extends Entity{

	public String name;
	public int points;
	public ArrayList<Entity> inventory;
	public int inventoryCapacity;

	public PlayerEntity(Location loc) {
		super(loc);
		inventory= new ArrayList<Entity>();
		inventoryCapacity=3;
		points=0;
	}

	@Override
	public Sprite sprite() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addItem(Entity item){
		inventory.add(item);
	}

	public void removeItem(Entity item){
		inventory.remove(item);
	}

	public void addPoint(){
		points++;
	}

	public boolean canPickUp(){
		if(inventory.size()==inventoryCapacity){
			return false;
		}
		return true;
	}
}
