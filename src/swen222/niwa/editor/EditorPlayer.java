package swen222.niwa.editor;

import swen222.niwa.gui.graphics.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * Created by Marc on 30/09/2016.
 */
public class EditorPlayer extends PlayerEntity {

	static final SpriteSet ss = SpriteSet.get("ghostRed");

	public EditorPlayer(Location loc) {
		super(loc, 2);
	}

	@Override
	public Sprite sprite(Direction camera) {
		//return sprites[0];
		//return sprites[this.facing.relativeTo(facing).ordinal()];
		//return ss.facing(facing).sprite(camera);
		return ss.facing(getFacing()).sprite(camera);
	}

	@Override
	public boolean move(Direction d) {
		updateFacing(d);
		return super.move(d);
	}
}
