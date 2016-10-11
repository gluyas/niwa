package swen222.niwa.net;

import static java.awt.event.KeyEvent.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

import swen222.niwa.demo.DemoFrame;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.util.Update;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.world.World;

/**
 * A slave connection receives information about the current state of the board
 * and relays that into the local copy of the board. The slave connection also
 * notifies the master connection of key presses by the player.
 *
 * @author Hamish M
 * @author Marc
 */

public class Slave extends Thread implements KeyListener{

	// INTERACTIONS
	public static final int PLAYER_ACTION = 'a';
	public static final int PLAYER_MOVE = 'm'; // following int indicates direction

	public static final int REQUEST_WORLD = 'w';
	public static final int REQUEST_ROOM = 'r';

	public static final int STATUS_READY = 'g';
	public static final int STATUS_STOP = 's';

	private static ObservableEntityTable<Entity> et = new HashEntityTable<>();
	private World currentWorld;
	private static Room currentRoom;
	private final Socket socket;
	private DataOutputStream output;
	private ObjectInputStream input;
	private DemoFrame gameWindow = new DemoFrame(null, this);

	private boolean exit = false;

	//TODO: Very much just a place holder at the moment, will obviously need further implementation

	public Slave(Socket socket){
		this.socket = socket;
	}

	private void setReady(boolean ready) throws IOException {
		if (ready) output.write(STATUS_READY);
		else output.write(STATUS_STOP);
	}

	private void requestWorld() throws IOException, ClassNotFoundException {
		output.write(REQUEST_WORLD);
		System.out.println("Requesting world");
		while (!exit) {
			if(input.available() != 0 && input.read() == Master.LOAD_WORLD){
				loadWorld();
			}
		}
	}

	public void loadWorld() throws IOException, ClassNotFoundException {
		System.out.println("RECEIVING WORLD");
		setWorld((World) input.readObject());
	}

	private void setWorld(World w) {
		this.currentWorld = w;
	}

	private void requestRoom() throws IOException, ClassNotFoundException {
		output.write(REQUEST_ROOM);
		System.out.println("REQUESTING ROOM");
		while (!exit) {
			if(input.available() != 0 && input.read() == Master.LOAD_ROOM){
				setRoom((Room) input.readObject());
				break;
			}
		}
	}

	private void loadRoom() throws IOException, ClassNotFoundException {
		System.out.println("RECEIVING ROOM");
		setRoom((Room) input.readObject());
	}

	private void setRoom(Room r) {
		this.currentRoom = r;
		et = new HashEntityTable<>();
		gameWindow.setRoom(r);
	}

	private void applyUpdate() throws IOException, ClassNotFoundException {
		Update ud = (Update) input.readObject();
		//System.out.println("APPLYING UPDATE "+ud);
		ud.apply();
	}

	private void addEntity() throws IOException, ClassNotFoundException {
		Entity e = (Entity) input.readObject();
		et.add(e);
	}

	private void removeEntity() throws IOException, ClassNotFoundException {
		Entity e = (Entity) input.readObject();
		et.remove(e);
	}

	public void run(){
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			while(!exit) {
				// read event
				if(input.available() != 0){
					int action = input.read();
					switch (action) {
						case Master.LOAD_WORLD:
							loadWorld();
							break;

						case Master.LOAD_ROOM:
							loadRoom();
							break;

						case Master.APPLY_UPDATE:
							applyUpdate();
							break;

						case Master.ADD_ENTITY:
							addEntity();
							break;

						case Master.RM_ENTITY:
							removeEntity();
							break;
					}


					/*
					switch (action) {
						case 'r': // rooms
							System.out.println("master sent me a room");
							// TODO: initial room state, should only be sent once
							// and then the remaining mutable objects will be sent
							// over e.g. entities
							currentRoom = (Room) input.readObject();
							// no current room
							if(gameWindow == null){
								gameWindow = new DemoFrame(currentRoom, this);
							}else{ // update the room to render
								gameWindow.panel.rr = new RoomRenderer(currentRoom);
							}
							gameWindow.repaint();
							break;
						case 'p':
							// TODO: updated player state
							System.out.println("received a player");
							DemoPlayer p = (DemoPlayer) input.readObject();;
							System.out.println(p);
							et.add(p);
							for (Entity e : et) System.out.println(e.toString());
							gameWindow.repaint();
							break;
						case 's':
							// TODO: updated seed state
							break;

					}*/
				}
			}
			socket.close(); // release socket ... v.important!

		} catch(Exception e) {
			throw new Error(e);
		}
	}


	private static String inputString(String msg){
		System.out.println(msg);
		while(true){ // loop indefinitely
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			try{
				return input.readLine(); // return the user's input
			}catch(IOException e){

			}
		}
	}

	//TODO: fix this horrible non-encapsulation
	public static ObservableEntityTable<Entity> getEntityTable() {
		return et;
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		try{
			int code = e.getKeyCode();
			switch (code) {
			case VK_W: // move up
				output.write(PLAYER_MOVE);
				output.write(Direction.NORTH.ordinal());
				break;

			case VK_A: // move left
				output.write(PLAYER_MOVE);
				output.write(Direction.WEST.ordinal());
				break;

			case VK_S: // move down
				output.write(PLAYER_MOVE);
				output.write(Direction.SOUTH.ordinal());
				break;

			case VK_D: // move right
				output.write(PLAYER_MOVE);
				output.write(Direction.EAST.ordinal());
				break;

			case VK_Q: // rotate cw
				break;

			case VK_E: // rotate ccw
				break;

			case VK_F: // interaction

				break;
			}
		}catch(IOException ee){

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
