package swen222.niwa.model.entity.entities;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class Rune extends ObjectEntity {

	public runeType image;
	
	public Rune (Location loc){
		super(loc);
	}
	
	
	public Rune (Location loc, runeType type){
		super(loc);
		image=type;

	}

	public enum runeType{
		CIRCLE,
		LIGHTNING,
		CROSS;
	}

	public runeType getType(){
		return image;
	}
	@Override
	public Sprite sprite(Direction camera) {
		// TODO Auto-generated method stub
		return null;
	}
}
