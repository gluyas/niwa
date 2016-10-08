package swen222.niwa.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import swen222.niwa.demo.DemoPlayer;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;
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
	private EntityTable<Entity> et = new HashEntityTable<>();
	private final int uid; // a unique id
	private final Socket socket;

	public Master(int uid, Socket socket, Room room) {
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
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			objOut.writeByte('r');
			r = Room.newFromFile(new File("resource/rooms/testRoom2.xml"));
			objOut.writeObject(r);


			boolean exit=false;

			while(!exit) {

				if(input.available() != 0) {
					int event = input.readInt();
					switch(event){
					case Slave.MOVE_UP:
						//
						System.out.println("Player trying to move up");
						break;
					case Slave.MOVE_DOWN:
						//
						System.out.println("Player trying to move down");
						break;
					case Slave.MOVE_LEFT:
						//
						System.out.println("Player trying to move left");
						break;
					case Slave.MOVE_RIGHT:
						//
						System.out.println("Player trying to move right");
						break;
					case Slave.PLAYER_ACTION:
						//
						System.out.println("Player trying to interact with something");
					}
				}
				// Now broadcast updated room state to slave
				output.flush();
			}
			socket.close(); // release socket ... v.important!
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
