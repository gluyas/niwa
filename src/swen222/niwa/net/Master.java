package swen222.niwa.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import swen222.niwa.Server;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.entity.PlayerEntity;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Room;

/**
 * A master connection receives events from a slave connection via a socket.
 * These events are registered within the game. The master connection is also
 * responsible for transmitting information to the slave about the current game
 * state.
 *
 * @author Hamish M
 * @author Marc
 */
public class Master extends Thread implements Observer {

	public static final int SET_WORLD = 'w';
	public static final int SET_ROOM = 'r';
	public static final int SET_PLAYER = 'p';

	public static final int ADD_ENTITY = 'e';
	public static final int RM_ENTITY = 'm';

	public static final int APPLY_UPDATE = 'u';

	public final String name; // username for the player - account will be stored under this.

	private Room currentRoom;
	private ObservableEntityTable<Entity> et;
	private PlayerEntity player;


	private final Server server;
	private final Socket socket;

	private DataInputStream input;
	private ObjectOutputStream output;
	private boolean close = false;

	public Master(Socket socket, String name, Server server) {
		this.name = name;
		this.socket = socket;
		this.server = server;
		this.player = server.join(this);
		//this.player.addObserver(this);
		this.currentRoom = player.getLocation().room;
		this.et = server.getEntityTable(currentRoom);
		et.addObserver(this);

		try {
			input = new DataInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			if (o == player && arg instanceof Entity.LocationUpdate) {
				Entity.LocationUpdate ud = (Entity.LocationUpdate) arg;
				if (ud.to.room != currentRoom){
					System.out.println("not in current room");
					setRoom(ud.to.room);
				}

			} else if (o == et && arg instanceof ObservableEntityTable.ElementUpdate) {
				// Entity table updates cannot be serialised as the tables themselves are not - we need to control it
				// via messages over net
				if (arg instanceof ObservableEntityTable.AddElementUpdate) {
					ObservableEntityTable.AddElementUpdate ud = (ObservableEntityTable.AddElementUpdate) arg;
					sendAdd(ud.e);
				} else if (arg instanceof ObservableEntityTable.RemoveElementUpdate) {
					ObservableEntityTable.RemoveElementUpdate ud = (ObservableEntityTable.RemoveElementUpdate) arg;
					sendRemove(ud.e);
				}
			} else {
				sendUpdate(arg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAdd(Entity e) throws IOException {
		output.write(ADD_ENTITY);
		output.writeObject(e);
		output.flush();
	}

	private void sendRemove(Entity e) throws IOException {
		output.write(RM_ENTITY);
		output.writeObject(e);
		output.flush();
	}

	private void setRoom(Room r) throws IOException {
		this.et.deleteObserver(this);
		this.currentRoom = r;
		this.et = server.getEntityTable(currentRoom);
		this.et.addObserver(this);
		sendRoom();
		System.out.println("SENDING PLAYER");
	}

	private void sendWorld() throws IOException {
		output.write(SET_WORLD);
		output.writeObject(server.world);
		output.flush();
		System.out.println("SENDING WORLD "+server.world);
	}

	private void sendRoom() throws IOException {
		output.write(SET_ROOM);
		output.writeObject(currentRoom); //TODO: set current room to the player's initial value!
		for (Entity e : server.getRoomEntities(currentRoom)) {
			output.write(ADD_ENTITY);
			output.writeObject(e);
		}
		output.flush();
		System.out.println("SENDING ROOM "+currentRoom);
	}

	private void sendPlayer() throws IOException {
		output.write(SET_PLAYER);
		output.writeObject(player);
		output.flush();
	}

	private void sendUpdate(Object update) throws IOException {
		output.write(APPLY_UPDATE);
		output.writeObject(update);
		output.flush();
		//System.out.println("SENDING UPDATE "+update);
	}

	//private void setReady(boolean ready) {
		//isReady = ready;
	//}

	public void run(){
		try {

			sendWorld();
			sendRoom();
			sendPlayer();

			while(!close) {
				if(input.available() != 0) {
					int event = input.read();
					switch(event) {
						case Slave.PLAYER_ACTION:
							server.action(this, input.read());
							break;

						case Slave.PLAYER_MOVE:
							// second int is an ordinal
							Direction d = Direction.values()[input.read()];
							//System.out.println("MOVING "+d);
							server.move(this, d);
							break;

						case Slave.PLAYER_DROP:
							server.drop(this, input.read());
							break;

						case Slave.REQUEST_ROOM:
							sendRoom();
							break;

						case Slave.REQUEST_WORLD:
							sendWorld();
							break;

						case Slave.REQUEST_PLAYER:
							sendPlayer();
							break;

						case Slave.STATUS_READY:
							//setReady(true);
							break;

						case Slave.STATUS_STOP:
							//setReady(false);
							break;

					}
					output.flush();
				}
			}
			socket.close(); // release socket ... v.important!
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
