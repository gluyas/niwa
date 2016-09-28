package swen222.niwa.net;

/**
 * A ClockThread is used for producing pulses. A "pulse" is respomsible for
 * updating the state of the game, as well as refreshing each client's display
 * window. Note that it is important to monitor the delay between pulses. This
 * is because there will be issues if the time for work after a pulse exceeds
 * the delay.
 *
 * @author Hamish M
 *
 */
public class ClockThread extends Thread{

	// NOTE: At the moment I'm basing the server/client implementation off of DJP's Pacman.
	// I'm still sussing out how everything interacts with each other, but it should work fine
	// once it's been fine tuned.

	private final int delay; // the delay between each pulse
	// Here I need some sort of reference to the instance of the current Game, not sure we have this
	// class implemented yet
	// e.g. private final Game game;

	// DJP's class has a reference to the display so that it can be repainted from the run() method in this class,
	// however I'm not sure if that is valid for this game as each client has the potential to have a different display,
	// unlike in Pacman.
	// TODO: Figure out how each client's display is repainted


	public ClockThread(int delay) {
		this.delay = delay;
	}

	public void run(){
		// with each clock tick, we need to perform updates
		// loop indefinitely
		int tickCount = 0;
		while(true){
			try{
				Thread.sleep(delay);
				//System.out.println(tickCount + " tick");
				tickCount++;
			}catch(InterruptedException e){
				// Should never get here.
			}
		}
	}


}
