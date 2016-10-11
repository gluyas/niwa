package swen222.niwa.model.world;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import swen222.niwa.file.SpriteLoader;
import swen222.niwa.model.entity.*;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.world.Location.InvalidLocationException;

/**
 *
 * All the rules for the game. This is used by the client to decide what changes
 * need to be made to the entities.
 * @author burnshami
 *
 */
public class Rules {

	//private EntityTable<Entity> entities;
	//public Room room;
	private World world;
	private EntityTable<Entity> [][] ets;

	public Rules(World world, EntityTable<Entity>[][] ets){
		this.world = world;
		this.ets = ets;
	}

	public EntityTable<Entity> getRoomEntities(Entity p) {
		return getRoomEntities(p.getLocation().room);
	}

	public EntityTable<Entity> getRoomEntities(Room r) {
		return ets[r.worldRow][r.worldCol];
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
	public boolean move(PlayerEntity player, Direction dir) {
		player.updateFacing(dir);

		if(outOfBounds(player,dir)){
			return tryLeaveRoom(player, dir);
		}
		if(!canMove(player,dir)){
			return false;
		}
		player.move(dir);
		pickUp(player);//pick up anything player is on
		checkStatues(player.getLocation().room);//Update statue states and opens door
		return true;
	}

	private boolean tryLeaveRoom(PlayerEntity player, Direction dir) {
		Room room = player.getLocation().room;
		int col = room.worldCol;
		int row = room.worldRow;
		Room newRoom;

		switch(dir){
			case NORTH:
				if(row!=0){//Top of map
					newRoom=world.roomAt(col+dir.relativeX(), row+dir.relativeY());
				}
				else{
					newRoom=world.roomAt(col, world.height-1);
				}
				return moveRoom(player,dir.opposite(),newRoom);//Entering from south spawn
			case EAST:
				if(col!=2){//Right of map
					newRoom=world.roomAt(col+dir.relativeX(), row+dir.relativeY());

				}
				else{
					newRoom=world.roomAt(0, row);
				}
				return moveRoom(player,dir.opposite(),newRoom);//Entering from south spawn
			case SOUTH:
				if(row!=2){//Bottom of map
					newRoom=world.roomAt(col+dir.relativeX(), row+dir.relativeY());
					return moveRoom(player,dir.opposite(),newRoom);//Entering from south spawn
				}
				else{
					newRoom=world.roomAt(col,0);
				}
				return moveRoom(player,dir.opposite(),newRoom);//Entering from south spawn
			case WEST:
				if(col!=0){//West of map
					newRoom=world.roomAt(col+dir.relativeX(), row+dir.relativeY());
					return moveRoom(player,dir.opposite(),newRoom);//Entering from south spawn
				}
				else{
					newRoom=world.roomAt(world.width-1, row);
				}
				return moveRoom(player,dir.opposite(),newRoom);//Entering from south spawn
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

	private boolean moveRoom(PlayerEntity player, Direction entrySide,Room newRoom) {
		getRoomEntities(player).remove(player);
		ets[newRoom.worldRow][newRoom.worldCol].add(player);
		System.out.println(ets[newRoom.worldRow][newRoom.worldCol]);
		player.setLocation(newRoom.getSpawn(entrySide));
		return true;
	}

	private void checkStatues(Room r) {
		for(Entity e: getRoomEntities(r)){
			if(e instanceof Statue){
				if(playerOn(e)){
					((Statue) e).setTriggered(true);
				}
				else{
					((Statue) e).setTriggered(false);
				}
			}
		}
		checkDoors(r);
	}

	private void checkDoors(Room r){
		for(Entity e: getRoomEntities(r)){
			if(e instanceof Door){
				if(((Door) e).checkStatues()){//if all the statues connected to this door are triggered
					((Door) e).setOpen(true);//set the door to open
				}
			}
		}
	}

	private boolean playerOn(Entity statue) {
		for(Entity e: getRoomEntities(statue).get(statue.getLocation())){
			if(e instanceof PlayerEntity){
				return true;
			}
		}
		return false;
	}



	private boolean canMove(PlayerEntity player, Direction dir){
		try {
			Location toGo = player.getLocation().move(dir);
			Location from = player.getLocation();

			Set<Entity> entitiesInDirection = getRoomEntities(player).get(toGo);//check for players in direction
			for(Entity entity: entitiesInDirection){
				if (entity instanceof PlayerEntity){
						return false;
				}
				if (entity instanceof Door){
					if(!((Door) entity).isOpen()){
						return false;
					}
				}
				if (entity instanceof RuneStone){
					return false;
				}
			}

			if(!toGo.tile().canOccupy(player)){//check for physical props in direction
				return false;
			}

			if(from.tile().getHeight()-toGo.tile().getHeight()<-1){//togo is two steps higher
				return false;
			}
		} catch (InvalidLocationException e1) {
			//e1.printStackTrace();
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
		EntityTable<Entity> entities = getRoomEntities(player);
		Set<Entity> entitiesAtPosition = entities.get(player.getLocation()); // All entities at player location

		for(Entity e: entitiesAtPosition){
			if(e instanceof ObjectEntity){
				if(player.canPickUp()){

					player.addItem((ObjectEntity)e); // adds item to playerinventory
					for (Object o : player.getInventory()) System.out.println(o);
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
		Room room = player.getLocation().room;
		EntityTable<Entity> entities = getRoomEntities(room);

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
	public boolean action(PlayerEntity player, int slot){
		Room room = player.getLocation().room;
		ObjectEntity item = player.getInventory().get(slot);

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

		if(!toPlant.tile().getProp().getType().equals("soil")){
			return false;
		}
		player.removeItem(seed);
		addEntity(new Flower(toPlant,player.getName()));
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
		addEntity(new Seed(stone.getLocation(), SpriteLoader.get("seed")));
		removeEntity(stone);
	}

	/**
	 * Gets rune stone in front of player
	 * @param player
	 * @return
	 */
	private RuneStone getRuneStone(PlayerEntity player){
		Set<Entity> entitiesAtPosition;
		EntityTable<Entity> entities = getRoomEntities(player);
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
		getRoomEntities(e).remove(e);
	}
	private void addEntity(Entity e){
		getRoomEntities(e).add(e);
	}

//----------------------------------------------------------------------------------------------------------------

	//Updating entity helper methods

//----------------------------------------------------------------------------------------------------------------

	public void addPoints(PlayerEntity player,int number){
		player.addPoints(number);
	}

}
