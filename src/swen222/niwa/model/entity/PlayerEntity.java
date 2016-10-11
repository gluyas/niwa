package swen222.niwa.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.util.Update;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 *
 * Represents a player on the gameworld
 * @author burnshami
 *
 */
public class PlayerEntity extends Entity {

	public final String name;
	private int points;
	private ArrayList<ObjectEntity> inventory;
	private int inventoryCapacity;
	private Direction facing = Direction.NORTH;
	private SpriteSet sprites;

	public PlayerEntity(Location loc, SpriteSet sprites, String name) {
		super(loc);
		this.setDescription("What a spooky ghost...");
		this.inventory = new ArrayList<>();
		this.inventoryCapacity = 9;
		this.points = 0;
		this.sprites = sprites;
		this.name = name;
	}

	@Override
	public Sprite sprite(Direction camera) {
		return sprites.facing(facing).sprite(camera);
	}

	public void addItem(ObjectEntity item){
		if(inventory.add(item)){
			setChanged();
			System.out.printf("ADDING %s to inv %s%n", item, this);
			notifyObservers((Update)()-> this.addItem(item));
		}
	}

	public List<ObjectEntity> getInventory() {
		return Collections.unmodifiableList(inventory);
	}

	public String getName(){
		return name;
	}

	public int getPoints(){
		return points;
	}

	public void removeItem(Entity item){
		if (inventory.remove(item)) {
			setChanged();
			notifyObservers((Update)()->this.removeItem(item));
		}
	}

	public Direction getFacing(){
		return facing;
	}

	public void updateFacing(Direction dir){
		facing=dir;
		setChanged();
		notifyObservers((Update)()->this.updateFacing(dir));
	}

	public void addPoints(int number){
		points=points+number;
		setChanged();
		notifyObservers((Update)()->this.addPoints(number));
	}

	public boolean canPickUp() {
		if (inventory.size() == inventoryCapacity) {
			return false;
		}
		return true;

	}
}
