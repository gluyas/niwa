package swen222.niwa.model.entity.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
public class Door extends ObjectEntity {



	private SpriteSet sprites ;
	private ArrayList<Statue> statues;
	private final String closedDesc = "This door is shut tight.";
	private final String openDesc = "An open door can mean a world of possibilities.";

	private boolean open;


	public Door(Location loc, SpriteSet sprites, ArrayList<Statue> triggers){
		super(loc);
		this.statues = triggers;
		this.sprites = sprites;
		this.setOpen(false);
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprites.sprite(camera);
	}

	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
		if(open){
			this.setDescription(openDesc);
		} else {
			this.setDescription(closedDesc);
		}
	}

	public ArrayList<Statue> getStatues(){
		return statues;
	}

	public boolean checkStatues(){
		for(Statue statue: statues){
			if(!statue.isTriggered()){
				return false;
			}
		}
		return true;
	}


}
