package swen222.niwa.net;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * A slave connection receives information about the current state of the board
 * and relays that into the local copy of the board. The slave connection also
 * notifies the master connection of key presses by the player.
 *
 * @author Hamish M
 */

public class Slave extends Thread implements KeyListener{

	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;

	//TODO: Very much just a place holder at the moment, will obviously need further implementation

	public Slave(Socket socket){
		this.socket = socket;
	}

	public void run(){
		System.out.println("client is doing stuff");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
