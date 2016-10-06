package swen222.niwa.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

			ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());

			// start by sending the initial state of a room

			room = Room.newFromFile(new File("/home/meiklehami1/resource/rooms/testRoom.xml"));
			output.writeByte('c');
			objOut.writeObject(room);


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
					objOut.writeObject(room);
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
