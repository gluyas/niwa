package swen222.niwa.net;

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
public class Master {
	
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
	}

}
