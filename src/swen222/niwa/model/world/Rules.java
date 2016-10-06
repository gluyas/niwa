package swen222.niwa.model.world;

import java.util.Set;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.EntityTable;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.entity.Seed;
import swen222.niwa.model.world.Location.InvalidLocationException;
import swen222.niwa.model.world.Prop.PropType;

/**
 * @author burnshami
 *
 */
public class Rules {

	public EntityTable<Entity> entities;
	public Rules(EntityTable<Entity> table){
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

	/**
	 * Removes an item from player inventory and adds it to the space on the ground of player
	 *
	 * @param player
	 * @param item
	 * @return
	 */
	public boolean drop(PlayerEntity player,ObjectEntity item){
		Set<Entity> entitiesAtPosition = entities.get(player.getLocation()); // All entities at player location
		for(Entity e: entitiesAtPosition){
			if(e instanceof ObjectEntity){
				return false;//if object already there
			}
		}
		player.removeItem(item);
		item.setLocation(player.getLocation()); //places item
		return true;
	}



	/**
	 * Moves an entity one space in a room, if there is no entity in the chosen direction nor impassable terrain
	 * @param e
	 * @param dir
	 * @return
	 */
	public boolean move(Entity e, Direction dir){
		try {
			Location toGo =e.getLocation().move(dir);
			Location from =e.getLocation();

			Set<Entity> entitiesInDirection =entities.get(toGo);//check for players in direction
			for(Entity entity: entitiesInDirection){
				if (entity instanceof PlayerEntity){
					return false;
				}
			}

			if(!toGo.tile().canOccupy(e)){//check for physical props in direction
				return false;
			}

			if(from.tile().getHeight()-toGo.tile().getHeight()>1){//to go is two steps lower
				return false;
			}
			if(from.tile().getHeight()-toGo.tile().getHeight()<-1){//togo is two steps higher
				return false;
			}
		} catch (InvalidLocationException e1) {
			e1.printStackTrace();
			return false;
		}

		e.move(dir);
		return true;
	}


	/**
	 *Takes a seed from a players inventory and adds a point if above soil
	 *
	 * @param player
	 * @param seed
	 * @return true if seed removed
	 */
	public boolean plant(PlayerEntity player, ObjectEntity seed){
		Location toPlant = player.getLocation();
		if(!(seed instanceof Seed)){
			return false;
		}
		if(!toPlant.tile().getProp().getType().equals(PropType.SOIL)){
			return false;
		}
		player.removeItem(seed);
		player.addPoint();
		return true;
	}

}
