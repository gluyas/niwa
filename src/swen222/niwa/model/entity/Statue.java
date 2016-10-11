package swen222.niwa.model.entity;

import swen222.niwa.file.SpriteLoader;
import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.graphics.Sprite;
import swen222.niwa.model.util.Update;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class Statue extends ChangingEntity {

	private static SpriteSet SS_DORMANT = SpriteLoader.get("statueDormant");
	private static SpriteSet SS_ACTIVE = SpriteLoader.get("statueActive");
	private SpriteSet ss;
	private boolean triggered;
	private final String dormantDesc = "A stone statue. It looks like it's sleeping.";
	private final String activeDesc = "The statue is giving out an eerie glow...";

	public Statue(Location loc){
		super(loc);
		this.triggered = false;
	}
	@Override
	public Sprite sprite(Direction camera) {
		if (triggered) return SS_ACTIVE.sprite(camera);
		else return SS_DORMANT.sprite(camera);
	}

	public boolean isTriggered() {
		return triggered;
	}
	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
		if(triggered){
			this.setDescription(activeDesc);
		} else {
			this.setDescription(dormantDesc);
		}
		setChanged();
		notifyObservers((Update)()->this.setTriggered(triggered));
	}


}
