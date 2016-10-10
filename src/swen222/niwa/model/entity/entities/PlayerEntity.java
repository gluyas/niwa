package swen222.niwa.model.entity.entities;

import java.util.ArrayList;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class PlayerEntity extends Entity{

	public String name;
	public int points;
	public ArrayList<Entity> inventory;
	public int inventoryCapacity;
	public Direction facing;

	public PlayerEntity(Location loc) {
		super(loc);
		inventory= new ArrayList<Entity>();
		inventoryCapacity=3;
		points=0;
	}

	@Override
	public Sprite sprite(Direction camera) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addItem(Entity item){
		inventory.add(item);
	}

	public void removeItem(Entity item){
		if (inventory.remove(item)) {
			setChanged();
			//notifyObservers()
		}
	}
	public Direction getFacing(){
		return facing;
	}

	public void updateFacing(Direction dir){
		facing=dir;
	}

	public void addPoints(int number){
		while(number!=0){
			points++;
			number--;
		}
	}

	public boolean canPickUp(){
		if(inventory.size()==inventoryCapacity){
			return false;
		}
		return true;

	}
}
