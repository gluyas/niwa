package swen222.niwa.net;

/**
 * The Player class relays events e.g. key presses, clicks to a character proxy in
 * the game. This is only used in a single player game as, in a multi-player game,
 * the slave does the job of this class.
 * 
 * @author Hamish M
 *
 */

public class Player {
	
	//TODO: We might not actually need this class as it is only necessary in a single player scenario.
	// Would like some clarification for whether or not single player support is required.
	
	private final int uid; // a unique id

	public Player(int uid) {
		this.uid = uid;
	}
	

}
