package swen222.niwa.model.entity;

import java.awt.Image;

import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public abstract class ObjectEntity extends Entity{

	private String name;
	private Image thumbnail;

	public ObjectEntity(Location loc) {
		super(loc);
		// TODO Auto-generated constructor stub
	}

	public String getName(){
		return name;
	}

	public void setName(String s){
		name = s;
	}

	public Image getThumbnail(){
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail){
		this.thumbnail = thumbnail;
	}

}
