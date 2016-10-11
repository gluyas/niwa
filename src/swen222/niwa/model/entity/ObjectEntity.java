package swen222.niwa.model.entity;

import java.awt.Image;

import swen222.niwa.model.world.Location;

/**
 * @author burnshami
 *
 */
public abstract class ObjectEntity extends Entity {

	private String name;
	private Image thumbnail; // to be displayed on inventory buttons

	public ObjectEntity(Location loc) {
		super(loc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get this ObjectEntity's name string
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * set this ObjectEntity's name string
	 *
	 * @param s
	 *            - new name
	 */
	public void setName(String s) {
		name = s;
	}

	/**
	 * Get this ObjectEntity's thumbnail image
	 *
	 * @return
	 */
	public Image getThumbnail() {
		return thumbnail;
	}

	/**
	 * set this ObjectEntity's thumbnail image
	 *
	 * @param thumbnail
	 *            - new thumbnail image
	 */
	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}

}
