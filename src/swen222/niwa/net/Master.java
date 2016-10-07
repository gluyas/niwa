package swen222.niwa.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import swen222.niwa.demo.DemoPlayer;
import swen222.niwa.model.world.Location;
import swen222.niwa.model.world.Room;

/**
 * A master connection receives events from a slave connection via a socket.
 * These events are registered within the game. The master connection is also
 * responsible for transmitting information to the slave about the current game
 * state.
 *
 * @author Hamish M
 *
 */
public class Master extends Thread{

	// TODO: Need a reference to the game, for testing purposes will use a room
	// private Server server;
	private static Room room;
	private final int broadcastClock;
	private final int uid; // a unique id
	private final Socket socket;

	public Master(int broadcastClock, int uid, Socket socket, Room room) {
		this.broadcastClock = broadcastClock;
		this.uid = uid;
		this.socket = socket;
		this.room = room;
	}

	public void run(){
		// TODO: While the game is active, this method should be continuously running. It should
		// be receiving information from clients and then broadcasting it back to the client.

		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			Room r = Room.newFromFile(new File("resource/rooms/testRoom.xml"));
			DemoPlayer p = new DemoPlayer(Location.at(r, 0, 0));
			ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
			objOut.writeByte('r');
			objOut.writeObject(r);
			//objOut.flush();
			objOut.writeByte('p');
			objOut.writeObject(p);
			// second room
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			objOut.writeByte('r');
			r = Room.newFromFile(new File("resource/rooms/testRoom2.xml"));
			objOut.writeObject(r);


			boolean exit=false;

			while(!exit) {

				try {

					if(input.available() != 0) {
						// Player is attempting to move
						int dir = input.readInt();
						switch(dir){
						case 1:
							// get the reference to the player, then move them north
							// e.g. room.getPlayer(uid).move(Direction.North)
							break;
						case 2:
							// get the reference to the player, then move them south
							break;
						case 3:
							// get the reference to the player, then move them west
							break;
						case 4:
							// get the reference to the player, then move them east
							break;
						}
					}
					// Now broadcast updated room state to slave
					output.flush();
					Thread.sleep(broadcastClock);

				} catch(InterruptedException e) {
				}
			}
			socket.close(); // release socket ... v.important!
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
