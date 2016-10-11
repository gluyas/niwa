package swen222.niwa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import swen222.niwa.gui.NiwaFrame;
import swen222.niwa.gui.WinDialog;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.World;
import swen222.niwa.net.Slave;

/**
 * Crux of interaction between the local model and the view.
 *
 * @author Jack U
 * @author Zoe
 * @author Marc
 *
 */
public class Client extends Observable implements ActionListener {

	private final NiwaFrame view;
	private final Slave slave;

	private World currentWorld;
	private Room currentRoom;
	private EntityTable<Entity> currentET;

	public Client(Slave slave) {
		this.slave = slave;
		this.view = new NiwaFrame(this);
		slave.setClient(this);
		slave.run();
	}

	public void gameWon(String scores){
		new WinDialog(view, scores);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// change this string to be the object description at merge
		String selected = view.invPanel.getSelectedItem();
		int slot = view.invPanel.getSelectedSlot();
		switch (e.getActionCommand()) {
		case "Use Item":
			if(slot != -1){
				slave.action(slot);
			}
			break;
		case "Inspect":
			if (slot != -1){
				view.gamePanel.updateText(selected);
			}
			break;
		case "Drop":
			if (slot != -1) {
				slave.drop(slot);
			}
			break;
		case "Rotate CW(Q)":
			// rotate clockwise
			view.renderPanel.getRR().rotateCW();
			break;
		case "Rotate CCW(E)":
			// rotate counterclockwise
			view.renderPanel.getRR().rotateCCW();
			break;
		case "New Game":
			// start a new game
			break;
		case "Save":
			// save the game
			break;
		case "Load":
			// load a saved game
			break;
		default:
			// unknown command
		}
	}

	// ACCESSORS: setters to be used by Slave, getters by GUI.

	public World getWorld() {
		return currentWorld;
	}

	public void setWorld(World newWorld) {
		this.currentWorld = newWorld;
	}

	public Room getRoom() {
		return currentRoom;
	}

	public EntityTable<Entity> getEntityTable() {
		return EntityTable.unmodifiable(currentET);
	}

	public void setRoom(Room newRoom, EntityTable<Entity> newET) {
		this.currentRoom = newRoom;
		this.currentET = newET;
	}

}
