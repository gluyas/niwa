
package swen222.niwa;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import swen222.niwa.file.RoomParser;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.HashEntityTable;
import swen222.niwa.model.util.ObservableEntityTable;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.world.World;
import swen222.niwa.net.Master;
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
		int numOfPlayers = 0;

		// Parse the command line arguments
		for(int i = 0 ; i < args.length ; i++){
			if(args[i].startsWith("-")){
				String argument = args[i];
				if(argument.equals("-server")){
					server = true;
					numOfPlayers = Integer.parseInt(args[++i]);
				}else if(argument.equals("-connect")){
					host = args[++i];
				}
			}
		}

		// Check what to do
		if(server){
			// Running in server mode
			//Room game = Room.newFromFile(new File("resource/rooms/desertBowl.xml"), 0, 0);
			runServer(port, numOfPlayers, 3, 3);
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
	@SuppressWarnings("unchecked")
	private static void runServer(int port, int numOfPlayers, int width, int height){

		//Server gameServer = new Server(new World(Room.newFromFile(new File("resource/rooms/mountainpass.xml"), 0, 0)));
		ObservableEntityTable<Entity>[][] tables
				= (ObservableEntityTable<Entity>[][]) new ObservableEntityTable[height][width];
		// Start listening for connections
		System.out.println("SERVER LISTENING ON PORT: " +port);

		File[] maps = new File("resource/rooms").listFiles();
		Room[][] map = new Room[width][height];
		loop : for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				int mapNum = col * height + row;
				if (mapNum >= maps.length) break loop;
				RoomParser rp = new RoomParser(maps[mapNum]);
				map[row][col] = Room.RoomBuilder.buildRoom(rp, col, row);
				tables[row][col] = new HashEntityTable<>();
				tables[row][col].addAll(rp.getEntities());
			}
		}

		Server gameServer = new Server(new World(map), tables);

		try{
			Master[] connections = new Master[numOfPlayers];
			// Create the server socket
			ServerSocket server = new ServerSocket(port);
			while(true){
				// Listen for a socket
				Socket client = server.accept();
				System.out.println(client.getInetAddress() + " HAS CONNECTED.");
				// TODO: need to create the user ID e.g. int uid = game.registerPlayer()
				// then pass it into the Master object e.g. new Master(broadcastClock, uid, client)
				connections[--numOfPlayers] = new Master(client, String.valueOf(numOfPlayers), gameServer);
				connections[numOfPlayers].start();
				// If all clients have connected
			}

		}catch(IOException e) {

		}
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
			Slave s = new Slave(socket);
			new Client(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Check whether or not there is at least one connection alive.
	 *
	 * @param connections
	 * @return
	 */
	private static boolean atleastOneConnection(Master... connections) {
		for (Master m : connections) {
			if (m.isAlive()) {
				return true;
			}
		}
		return false;
	}


}
