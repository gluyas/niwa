package swen222.niwa.net;

import java.io.*;
import java.net.Socket;
import java.util.Collections;

import swen222.niwa.Client;
import swen222.niwa.model.entity.PlayerEntity;
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

public class Slave extends Thread {

	// INTERACTIONS
	public static final int PLAYER_ACTION = 'a';
	public static final int PLAYER_MOVE = 'm'; // following int indicates direction
	public static final int PLAYER_DROP = 'd';

	public static final int REQUEST_WORLD = 'w';
	public static final int REQUEST_ROOM = 'r';
	public static final int REQUEST_PLAYER = 'p';

	public static final int STATUS_READY = 'g';
	public static final int STATUS_STOP = 's';

	private final Socket socket;
	private DataOutputStream output;
	private ObjectInputStream input;

	private Client client;
	private ObservableEntityTable<Entity> et;

	private boolean exit = false;

	public Slave(Socket socket){
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			throw new Error(e);
		}
		this.socket = socket;
	}

	public void setClient(Client c) {
		this.client = c;
	}

	public boolean move(Direction d) {
		try {
			output.write(PLAYER_MOVE);
			output.write(d.ordinal());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean action(int selectedSlot) {
		try {
			output.write(PLAYER_ACTION);
			output.write(selectedSlot);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean drop(int selectedSlot) {
		try {
			output.write(PLAYER_DROP);
			output.write(selectedSlot);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void setReady(boolean ready) throws IOException {
		if (ready) output.write(STATUS_READY);
		else output.write(STATUS_STOP);
	}

	private void requestWorld() throws IOException, ClassNotFoundException {
		output.write(REQUEST_WORLD);
		System.out.println("Requesting world");
		while (!exit) {
			if(input.available() != 0 && input.read() == Master.SET_WORLD) {
				setWorld();
			}
		}
	}

	private void requestRoom() throws IOException, ClassNotFoundException {
		output.write(REQUEST_ROOM);
		System.out.println("REQUESTING ROOM");
		while (!exit) {
			if(input.available() != 0 && input.read() == Master.SET_ROOM) {
				setRoom();
			}
		}
	}

	private void requestPlayer() throws IOException, ClassNotFoundException {
		output.write(REQUEST_ROOM);
		System.out.println("REQUESTING ROOM");
		while (!exit) {
			if(input.available() != 0 && input.read() == Master.SET_ROOM){
				setPlayer();
				break;
			}
		}
	}

	private void setRoom() throws IOException, ClassNotFoundException {
		System.out.print("RECEIVING ROOM ... ");
		Room r = (Room) input.readObject();
		et = new HashEntityTable<>();
		client.setRoom(r, et);
		System.out.println("DONE");
	}

	public void setWorld() throws IOException, ClassNotFoundException {
		System.out.print("RECEIVING WORLD ... ");
		World w  = (World) input.readObject();
		client.setWorld(w);
		System.out.println("DONE");
	}

	private void setPlayer() throws IOException, ClassNotFoundException {
		System.out.print("RECEIVING PLAYER ...");
		PlayerEntity p = (PlayerEntity) input.readObject();
		client.setPlayer(p);
		System.out.println("DONE");
	}

	private void applyUpdate() throws IOException, ClassNotFoundException {
		Update ud = (Update) input.readObject();
		//System.out.println("APPLYING UPDATE "+ud);
		ud.apply();
		client.update();
	}

	private void addEntity() throws IOException, ClassNotFoundException {
		Entity e = (Entity) input.readObject();
		et.add(e);
		//System.out.println("ADDING "+e);
	}

	private void removeEntity() throws IOException, ClassNotFoundException {
		Entity e = (Entity) input.readObject();
		et.remove(e);
	}

	public void run(){
		if (client == null) return;
		try {
			while(!exit) {
				// read event
				if(input.available() != 0){
					int action = input.read();
					switch (action) {
						case Master.SET_WORLD:
							setWorld();
							break;

						case Master.SET_ROOM:
							setRoom();
							break;

						case Master.SET_PLAYER:
							setPlayer();
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
}
