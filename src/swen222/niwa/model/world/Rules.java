package swen222.niwa.model.world;

import java.util.Set;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.entity.StaticEntity;
import swen222.niwa.model.entity.entities.PlayerEntity;
import swen222.niwa.model.entity.entities.Rune;
import swen222.niwa.model.entity.entities.RuneStone;
import swen222.niwa.model.entity.entities.Seed;
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
	public boolean move(PlayerEntity e, Direction dir){
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
		e.updateFacing(dir);
		return true;
	}


	/**
	 *Takes a seed from a players inventory and adds a point if above soil
	 *
	 * @param player
	 * @param item
	 * @return true if seed removed
	 */
	public boolean action(PlayerEntity player, ObjectEntity item){

		if(item instanceof Seed){//Planting a seed
			return plantSeed(player,(Seed)item);
		}

		if (item instanceof Rune){//Using a rune
			return triggerRuneStone(player,(Rune)item);
		}
		return false;
	}

	public boolean triggerRuneStone(PlayerEntity player, Rune rune){
		if(!sameRuneType(player,rune)){
			return false;
		}
		player.removeItem(rune);
		player.addPoint();
		transformRuneStone(player);
		return true;
	}



	public boolean plantSeed(PlayerEntity player, Seed seed){
		Location toPlant = player.getLocation();

		if(!toPlant.tile().getProp().getType().equals(PropType.SOIL)){
			return false;
		}
		player.removeItem(seed);
		player.addPoint();
		return true;
	}

	public boolean sameRuneType(PlayerEntity player, Rune rune){
		RuneStone stone =getRuneStone(player);
		if(stone==null){
			return false;
		}
		if(!stone.getType().equals(rune.getType())){
			return false;
		}
		return true;
	}

	public void transformRuneStone(PlayerEntity player){
		RuneStone stone =getRuneStone(player);//will never be null otherwise sameRuneType would return false above.
		addEntity(new Seed(stone.getLocation()));
		removeEntity(stone);
	}

	public RuneStone getRuneStone(PlayerEntity player){
		Set<Entity> entitiesAtPosition;
			try {
				entitiesAtPosition = entities.get(player.getLocation().move(player.getFacing()));// All entities in front of player
				for(Entity e: entitiesAtPosition){
					if(e instanceof StaticEntity){
						if((StaticEntity)e instanceof RuneStone){
							return (RuneStone)e;
							}
						}
					}
			} catch (InvalidLocationException e1) {
				e1.printStackTrace();
			}
			return null;

	}

	public void removeEntity(Entity e){
		entities.remove(e);
	}
	public void addEntity(Entity e){
		entities.add(e);
	}


}
