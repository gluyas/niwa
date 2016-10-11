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
		// ((get someone 2 help w/ observer pattern and what it means for this))
		String selected = view.invPanel.getSelectedItem();
		switch (e.getActionCommand()) {
		case "Use Item":
			if (!selected.equals("Empty")&&!selected.equals("null")) {
				// do action stuff
				System.out.println("used " + selected);
				}
				// nothing to do
			break;
		case "Inspect":
			if (selected.equals("null")) {
				// do inspect stuff in game world if we support that, or do
				// nothing
				System.out.println("I don't know what that is.");
			} else if (!selected.equals("Empty")) {
				// do inspect stuff with this object
				// probs return a string held in that object
				System.out.println("it's " + selected);
			}
			break;
		case "Drop":
			if (!selected.equals("Empty")&&!selected.equals("null")) {
				// drop this object
				System.out.println("dropped " + selected);
				}
				// nothing to do
			break;
		case "Rotate CW(Q)":
			// rotate clockwise
			break;
		case "Rotate CCW(E)":
			// rotate counterclockwise
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
