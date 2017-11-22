
package swen222.niwa;

import swen222.niwa.demo.DemoFrame;
import swen222.niwa.net.Master;
import swen222.niwa.net.Slave;
import swen222.niwa.gui.graphics.SpriteSet;

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

		SpriteSet.loadSprites();

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
				} else if (argument.equals("-editor")) {
					DemoFrame.main(args);
					return;
				}
			}
		}

		// Check what to do
		if(server){
			// Running in server mode
			//Room game = Room.newFromFile(new File("resource/rooms/desertBowl.xml"), 0, 0);
			Server.run(port, numOfPlayers, 3, 3);
		}else if(host != null){
			// Running in client mode
			Client.run(host, port);
		}else{
			// Unrecognised arguments
			// TODO: Deal with these more elegantly
			System.out.println("Please use the command '-server'");
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
