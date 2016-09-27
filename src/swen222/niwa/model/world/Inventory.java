package swen222.niwa.model.world;

import java.util.ArrayList;

import swen222.niwa.model.entity.Entity;

public class Inventory {

	public int InventorySize;
	public ArrayList<Entity> stuff;

	public Inventory() {
		// TODO Auto-generated constructor stub
		InventorySize=3;
		stuff = new ArrayList<Entity>();
	}

	public boolean isFull(){
		if(stuff.size()==InventorySize){
			return true;
		}
		return false;
	}
}
