package swen222.niwa.model.world;

import java.util.Set;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.EntityTable;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.world.Location.InvalidLocationException;

/**
 * @author burnshami
 *
 */
public class Controller {

	public EntityTable<Entity> entities;
	public Controller(EntityTable<Entity> table){
		entities=table;
	}

	/**
	 * relies on only one object entity per location
	 * @param player
	 * @return
	 */
	public boolean pickUp(PlayerEntity player){
		Set<Entity> entitiesAtPosition = entities.get(player.getLocation()); // All entities at player location

		for(Entity e: entitiesAtPosition){
			if(e instanceof ObjectEntity){
				if(player.canPickUp()){
					player.addItem(e); // adds item to playerinventory
					entities.remove(e); //Is this correct way to handle removing items from map?
					return true;
				}
			}
		}
		return false;
	}

	public boolean move(Entity e, Direction dir){

		try {
			Location toGo =e.getLocation().move(dir);
			Set<Entity> entitiesInDirection =entities.get(toGo);//check for players in direction
			for(Entity entity: entitiesInDirection){
				if (entity instanceof PlayerEntity){
					return false;
				}
			}
			if(!toGo.room.tileAt(toGo).canOccupy(e)){//check for physical props in direction
				return false;
			}
		} catch (InvalidLocationException e1) {
			e1.printStackTrace();
			return false;
		}

		e.move(dir);
		return true;
	}







}
