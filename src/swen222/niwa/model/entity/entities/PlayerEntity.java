package swen222.niwa.model.entity.entities;

import java.util.ArrayList;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class PlayerEntity extends Entity {

	private String name;
	private String type;
	private int points;
	private ArrayList<ObjectEntity> inventory;
	private int inventoryCapacity;
	private Direction facing;
	private SpriteSet sprites;

	public PlayerEntity(Location loc, SpriteSet sprites, String type) {
		super(loc);
		this.setDescription("What a spooky ghost...");
		this.inventory = new ArrayList<ObjectEntity>();
		this.inventoryCapacity = 9;
		this.points = 0;
		this.sprites = sprites;
	}

	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

<<<<<<< Updated upstream
	public void addItem(ObjectEntity item) {
=======
	public String getType(){
		return type;
	}

	public void addItem(ObjectEntity item){
>>>>>>> Stashed changes
		inventory.add(item);
	}

	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public void removeItem(Entity item) {
		if (inventory.remove(item)) {
			setChanged();
			// notifyObservers()
		}
	}

	public Direction getFacing() {
		return facing;
	}

	public void updateFacing(Direction dir) {
		facing = dir;
	}

	public void addPoints(int number) {
		while (number != 0) {
			points++;
			number--;
		}
	}

	public boolean canPickUp() {
		if (inventory.size() == inventoryCapacity) {
			return false;
		}
		return true;

	}
}
