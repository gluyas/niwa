package swen222.niwa;

import static java.awt.event.KeyEvent.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


import swen222.niwa.gui.NiwaFrame;
import swen222.niwa.gui.WinDialog;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.World;
import swen222.niwa.model.world.Location.InvalidLocationException;
import swen222.niwa.net.Slave;

/**
 * Crux of interaction between the local model and the view.
 *
 * @author Jack U
 * @author Zoe
 * @author Marc
 *
 */
public class Client extends Observable implements ActionListener, KeyListener {

	private final NiwaFrame view;
	private final Slave slave;

	private World currentWorld;
	private PlayerEntity currentPlayer;
	private Room currentRoom;
	private ObservableEntityTable<Entity> currentET;

	public Client(Slave slave) {
		this.slave = slave;
		this.view = new NiwaFrame(this);
		slave.setClient(this);
		slave.run();
	}

	// ACCESSORS: setters to be used by Slave, getters by GUI.

	public World getWorld() {
		return currentWorld;
	}

	public void setWorld(World newWorld) {
		this.currentWorld = newWorld;
		setChanged();
		notifyObservers();
	}

	public Room getRoom() {
		return currentRoom;
	}

	public ObservableEntityTable<Entity> getEntityTable() {
		return currentET;
	}

	public void setRoom(Room newRoom, ObservableEntityTable<Entity> newET) {
		this.currentRoom = newRoom;
		this.currentET = newET;
		setChanged();
		notifyObservers(this);
	}

	public PlayerEntity getPlayer() {
		return currentPlayer;
	}

	public void setPlayer(PlayerEntity newPlayer) {
		currentPlayer = newPlayer;
		setChanged();
		notifyObservers();
	}

	public String somethingToSee(PlayerEntity player){
		String description= "";
		try {
			Location inFront = player.getLocation().move(player.getFacing());
			if(inFront.tile().getProp() != null){
				return inFront.tile().getProp().getDescription();
			}
			for(Entity e:currentET.get(inFront)){
				description=description+"\n"+e.getDescription();
			}
			return description;
		} catch (InvalidLocationException e) {
			return "";
		}
	}

	public void update() {
		setChanged();
		notifyObservers();
	}

	public void gameWon(String scores){
		new WinDialog(view, scores);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// change this string to be the object description at merge
		int slot = view.invPanel.getSelectedSlot();
		switch (e.getActionCommand()) {
			case "Use Item":
				if(slot != -1){
					slave.action(slot);
				}
				break;

			case "Inspect":
				if (slot != -1){
					view.gamePanel.updateText(view.invPanel.getItemDescription());
				}else{
					view.gamePanel.updateText(somethingToSee(getPlayer()));
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
				// run a new game
				break;

			case "Save":
				// save the game
				break;

			case "Load":
				// load a saved game
				break;

			default:
				return;
				// unknown command
		}
		view.requestFocusInWindow();
		//System.out.println("ACTION!");
		update();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		int slot = view.invPanel.getSelectedSlot();
		Direction camera = view.renderPanel.getRR().getFacing();
		switch (code) {
			case VK_W: // move up
				slave.move(Direction.NORTH.relativeTo(camera));
				break;

			case VK_A: // move left
				slave.move(Direction.WEST.relativeTo(camera));
				break;

			case VK_S: // move down
				slave.move(Direction.SOUTH.relativeTo(camera));
				break;

			case VK_D: // move right
				slave.move(Direction.EAST.relativeTo(camera));
				break;

			case VK_Q: // rotate cw
				view.renderPanel.getRR().rotateCW();
				update();
				break;

			case VK_E: // rotate ccw
				view.renderPanel.getRR().rotateCCW();
				update();
				break;

			case VK_F: // interaction
				slave.action(slot);
				break;

			case VK_I:
				if (slot != -1){
					view.gamePanel.updateText(view.invPanel.getItemDescription());
				}
				break;

			case VK_G:
				if (slot != -1) slave.drop(slot);
				break;
		}
		view.requestFocusInWindow();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	/**
	 * Creates a client socket and then starts running the client on a slave
	 * connection.
	 */
	public static void run(String host, int port){
		try {
			// Create the client's socket
			Socket socket = new Socket(host, port);
			System.out.println("CLIENT HAS BEEN CONNECTED TO " + host + " : " + port);
			Slave s = new Slave(socket);
			new Client(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
