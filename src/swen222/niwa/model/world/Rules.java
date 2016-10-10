package swen222.niwa.model.world;

import java.util.Set;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.entity.ChangingEntity;
import swen222.niwa.model.entity.entities.Door;
import swen222.niwa.model.entity.entities.PlayerEntity;
import swen222.niwa.model.entity.entities.Rune;
import swen222.niwa.model.entity.entities.RuneStone;
import swen222.niwa.model.entity.entities.Seed;
import swen222.niwa.model.entity.entities.Statue;
import swen222.niwa.model.world.Location.InvalidLocationException;
import swen222.niwa.model.world.Prop.PropType;

/**
 * @author burnshami
 *
 */
public class Rules {

	public EntityTable<Entity> entities;
	public Room room;
	public Room[][] world;
	public Rules(Room[][] world){
		this.world = world;
	}

//----------------------------------------------------------------------------------------------------------------

	//MOVE LOGIC

//----------------------------------------------------------------------------------------------------------------


	/**
	 * Moves an entity one space in a room, if there is no entity in the chosen direction nor impassable terrain
	 * Also picks up anyitems in their path
	 * @param player
	 * @param dir
	 * @return
	 */
	public boolean move(PlayerEntity player, Direction dir){
		setRoom(player);

		if(outOfBounds(player,dir)){
			return tryLeaveRoom(player, dir);
		}
		if(!canMove(player,dir)){
			return false;
		}
		player.move(dir);
		pickUp(player);//pick up anything player is on
		player.updateFacing(dir);
		checkStatues();//Update statue states and opens door
		return true;
	}

	private void setRoom(PlayerEntity player){
		room= player.getLocation().getRoom();
		entities =room.entities;
	}

	private boolean tryLeaveRoom(PlayerEntity player, Direction dir) {
		int col = room.col;
		int row = room.row;
		Room newRoom;
		switch(dir){
		case NORTH:
			if(row!=0){//Top of map
				newRoom=world[col][row-1];
				return moveRoom(player,2,newRoom);//Entering from south spawn
			}
			break;
		case EAST:
			if(col!=2){//Right of map
				newRoom=world[col+1][row];
				return moveRoom(player,4,newRoom);//Entering from West spawn
			}
			break;
		case SOUTH:
			if(row!=2){//Bottom of map
				newRoom=world[col][row+1];
				return moveRoom(player,0,newRoom);//Entering from north spawn
			}
			break;
		case WEST:
			if(col!=0){//West of map
				newRoom=world[col-1][row];
				return moveRoom(player,3,newRoom);//entering from east spawn
			}
			break;
		}
		return false;
}

	private boolean outOfBounds(PlayerEntity player, Direction dir){
		try {
			player.getLocation().move(dir);
		} catch (InvalidLocationException e) {
			return true;
			}
		return false;
	}

	private boolean moveRoom(PlayerEntity player, int entrySide,Room newRoom) {
		entities.remove(player);
		newRoom.addEntity(newRoom.spawnLocs[entrySide], player);
		return true;
	}

	private void checkStatues() {
		for(Entity e: entities){
			if(e instanceof Statue){
				if(playerOn(e)){
					((Statue) e).setTriggered(true);
				}
				else{
					((Statue) e).setTriggered(false);
				}
			}
		}
		checkDoors();
	}

	private void checkDoors(){
		for(Entity e: entities){
			if(e instanceof Door){
				if(((Door) e).checkStatues()){//if all the statues connected to this door are triggered
					((Door) e).setOpen(true);//set the door to open
				}
			}
		}
	}

	private boolean playerOn(Entity statue) {
		for(Entity e: entities.get(statue.getLocation())){
			if(e instanceof PlayerEntity){
				return true;
			}
		}
		return false;
	}



	private boolean canMove(PlayerEntity player, Direction dir){
		try {
			Location toGo =player.getLocation().move(dir);
			Location from =player.getLocation();

			Set<Entity> entitiesInDirection =entities.get(toGo);//check for players in direction
			for(Entity entity: entitiesInDirection){
				if (entity instanceof PlayerEntity){
						return false;
				}
			}

			if(!toGo.tile().canOccupy(player)){//check for physical props in direction
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
		return true;
	}

	/**
	 * relies on only one object entity per location
	 * @param player
	 * @return
	 */
	private boolean pickUp(PlayerEntity player){
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
		setRoom(player);

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




//----------------------------------------------------------------------------------------------------------------

	//ACTION LOGIC

//----------------------------------------------------------------------------------------------------------------

	/**
	 *Performs an action depending on whether the item selected is a seed or a rune
	 *
	 * @param player
	 * @param item
	 * @return true if seed removed
	 */
	public boolean action(PlayerEntity player, ObjectEntity item){
		setRoom(player);

		if(item instanceof Seed){//Planting a seed
			return plantSeed(player,(Seed)item);
		}
		if (item instanceof Rune){//Using a rune
			return triggerRuneStone(player,(Rune)item);
		}
		return false;
	}

	/**
	 * Checks whether the rune and runestone are the same, if so runestone turns into a seed and player gets a point
	 * @param player
	 * @param rune
	 * @return
	 */
	private boolean triggerRuneStone(PlayerEntity player, Rune rune){
		if(!sameRuneType(player,rune)){
			return false;
		}
		player.removeItem(rune);
		addPoints(player,3);
		transformRuneStone(player);
		return true;
	}



	/**
	 * Checks whether the player is on soil, if they are the player loses the seed and gets a point
	 * @param player
	 * @param seed
	 * @return
	 */
	private boolean plantSeed(PlayerEntity player, Seed seed){
		Location toPlant = player.getLocation();

		if(!toPlant.tile().getProp().getType().equals(PropType.SOIL)){
			return false;
		}
		player.removeItem(seed);
		addPoints(player,1);
		return true;
	}

	/**
	 * Checks if rune is same as runestone in front of player
	 * @param player
	 * @param rune
	 * @return
	 */
	private boolean sameRuneType(PlayerEntity player, Rune rune){
		RuneStone stone =getRuneStone(player);
		if(stone==null){
			return false;
		}
		if(!stone.getType().equals(rune.getType())){
			return false;
		}
		return true;
	}

	/**
	 * Turns runestone into seed
	 * @param player
	 */
	private void transformRuneStone(PlayerEntity player){
		RuneStone stone =getRuneStone(player);//will never be null otherwise sameRuneType would return false above.
		addEntity(new Seed(stone.getLocation()));
		removeEntity(stone);
	}

	/**
	 * Gets rune stone in front of player
	 * @param player
	 * @return
	 */
	private RuneStone getRuneStone(PlayerEntity player){
		Set<Entity> entitiesAtPosition;
			try {
				entitiesAtPosition = entities.get(player.getLocation().move(player.getFacing()));// All entities in front of player
				for(Entity e: entitiesAtPosition){
					if(e instanceof ChangingEntity){
						if((ChangingEntity)e instanceof RuneStone){
							return (RuneStone)e;
							}
						}
					}
			} catch (InvalidLocationException e1) {
				e1.printStackTrace();
			}
			return null;

	}

	private void removeEntity(Entity e){
		entities.remove(e);
	}
	private void addEntity(Entity e){
		entities.add(e);
	}

//----------------------------------------------------------------------------------------------------------------

	//Updating entity helper methods

//----------------------------------------------------------------------------------------------------------------

	public void addPoints(PlayerEntity player,int number){
		player.addPoints(number);
	}

}
