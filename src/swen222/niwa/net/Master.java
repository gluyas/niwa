package swen222.niwa.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

	// TODO: Need a reference to the game
	private final int broadcastClock;
	private final int uid; // a unique id
	private final Socket socket;

	public Master(int broadcastClock, int uid, Socket socket) {
		this.broadcastClock = broadcastClock;
		this.uid = uid;
		this.socket = socket;
	}

	public void run(){
		// TODO: While the game is active, this method should be continuously running. It should
		// be receiving information from clients and then broadcasting it back to the client.

		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			boolean exit=false;

			while(!exit) {

				try {

					if(input.available() != 0) {
						System.out.println("received a message from a client");
						// read message from client.
						String message = "message received";
						// Now, broadcast the message back to the client
						byte[] encodedMessage = message.getBytes();
						output.write(encodedMessage);
						output.flush();
						Thread.sleep(broadcastClock);
					}

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
