package swen222.niwa.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.SpriteSet;
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

	private static final SpriteSet[] SPRITES = {
			SpriteSet.get("ghostBlue"),
			SpriteSet.get("ghostRed"),
			SpriteSet.get("ghostGreen"),
			SpriteSet.get("ghostGrey"),
			SpriteSet.get("ghostBlack")
	};

	public final String name;
	private final int type;

	private int points;

	private ArrayList<ObjectEntity> inventory;
	private int inventoryCapacity;
	private Direction facing = Direction.NORTH;
	private SpriteSet sprites;

	public PlayerEntity(Location loc, int type) {
		super(loc);
		this.setDescription("What a spooky ghost...");
		this.inventory = new ArrayList<>();
		this.inventoryCapacity = 9;
		this.points = 0;
		this.type = type;
		this.name = getNameFromType(type);
	}

	private static String getNameFromType(int type) {
		switch (type) {
			case 0:
				return "blue";
			case 1:
				return "red";
			case 2:
				return "green";
			case 3:
				return "grey";
			case 4:
				return "black";
			default:
				return "blue";
		}
	}

	@Override
	public Sprite sprite(Direction camera) {
		return SPRITES[type].facing(facing).sprite(camera);
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
