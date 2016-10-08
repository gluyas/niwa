package swen222.niwa.model.entity.entities;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

public class Rune extends ObjectEntity {

	public runeType image;
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
	public Sprite sprite(Direction facing) {
		// TODO Auto-generated method stub
		return null;
	}
}
