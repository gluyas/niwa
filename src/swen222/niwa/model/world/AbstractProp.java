package swen222.niwa.model.world;

import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.Entity;

public class AbstractProp implements Prop {

	@Override
	public Sprite sprite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canPassThrough(Entity e) {
		// TODO Auto-generated method stub
		return false;
	}

}
