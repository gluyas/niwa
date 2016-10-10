package swen222.niwa.model.entity.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class Door extends ObjectEntity {



	static final Sprite sprite ;
	private ArrayList<Statue> statues;

	private boolean open;

	static {
		try {
			sprite = new Sprite (
					ImageIO.read(new File("resource/images/entities/door-front.png")),
					0.5, 0.8);
		} catch (IOException e) {
			throw new Error("Couldn't load image", e);
		}

	}

	public Door(Location loc, ArrayList<Statue> triggers){
		super(loc);
		this.statues = triggers;
		open=false;
	}
	@Override
	public Sprite sprite(Direction camera) {
		return sprite;
	}

	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
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
