package swen222.niwa.model.entity.entities;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.StaticEntity;
import swen222.niwa.model.entity.entities.Rune.runeType;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class RuneStone extends StaticEntity{

	public runeType type;
	public RuneStone(Location loc, runeType type) {
		super(loc);
		this.type=type;
	}
	
	public RuneStone(Location loc) {
		super(loc);
	
	}

	public runeType getType(){
		return type;
	}
	@Override
	public Sprite sprite(Direction camera) {
		// TODO Auto-generated method stub
		return null;
	}

}
