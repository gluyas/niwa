package swen222.niwa;

import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.world.Room;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Marc on 9/10/2016.
 */
public class RoomTuple extends Observable implements Observer {

	public final Room tiles;
	public final EntityTable<Entity> et;

	public RoomTuple(Room tiles) {
		this.tiles = tiles;
		ObservableEntityTable<Entity> et = new HashEntityTable<>();
		et.addObserver(this);
		this.et = et;
	}

	@Override
	public void update(Observable o, Object arg) {

	}
}
