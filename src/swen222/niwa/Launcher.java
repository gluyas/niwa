package swen222.niwa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import swen222.niwa.net.ClockThread;
import swen222.niwa.net.Slave;

/**
 * The Launcher class is used to set up and run an instance of the game. It reads
 * command line arguments and then does the appropriate action e.g. setting up the
 * server and accepting client connections. Once the game is initialized it begins
 * updating the game state until the game has been won or all clients have
 * disconnected.
 *
 * @author Hamish Meikle
 *
 */
public class Launcher {

	public static void main(String args[]){

		boolean server = false;
		String host = null;
		int port = 32768;
		int clockPeriod = 20;

		// Parse the command line arguments
		for(int i = 0 ; i < args.length ; i++){
			if(args[i].startsWith("-")){
				String argument = args[i];
				if(argument.equals("-server")){
					server = true;
				}else if(argument.equals("-connect")){
					host = args[++i];
				}
			}
		}

		// Check what to do
		if(server){
			// Running in server mode
			runServer(port, clockPeriod);
		}else if(host != null){
			// Running in client mode
			runClient(host, port);
		}else{
			// Unrecognised arguments
			// TODO: Deal with these more elegantly
			System.out.println("Please use the command '-server'");
		}



	}

	/**
	 * Creates a server socket and listens for connections from client sockets,
	 * once all clients have connected it starts a game.
	 */
	private static void runServer(int port, int gameClock){
		// Setup a clock thread
		ClockThread clock = new ClockThread(gameClock);

		// Start listening for connections
		System.out.println("SERVER LISTENING ON PORT: " +port);

		try{
			// Create the server socket
			ServerSocket server = new ServerSocket(port);
			while(true){
				// Listen for a socket
				Socket client = server.accept();
				System.out.println(client.getInetAddress() + " HAS CONNECTED.");
			}

		}catch(IOException e){

		}
		startGame(clock);
	}

	/**
	 * Creates a client socket and then starts running the client on a slave
	 * connection.
	 */
	private static void runClient(String host, int port){
		try {
			// Create the client's socket
			Socket socket = new Socket(host, port);
			System.out.println("CLIENT HAS BEEN CONNECTED TO " + host + " : " + port);
			new Slave(socket).run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private static void startGame(ClockThread clock){
		clock.start();
	}

}
