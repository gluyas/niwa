package swen222.niwa.model.entity.entities;

import swen222.niwa.file.SpriteLoader.SpriteSet;
import swen222.niwa.gui.Sprite;
import swen222.niwa.model.entity.ChangingEntity;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public class RuneStone extends ChangingEntity{

	private String type;
	private SpriteSet sprites; 
	
	public RuneStone(Location loc, String type, SpriteSet sprites){
		super(loc);
		this.type = type;
		this.sprites = sprites;
	}
	
	public String getType(){
		return type;
	}
	
	@Override
	public Sprite sprite(Direction camera) {
		sprites.sprite(camera);
		return null;
	}

}
