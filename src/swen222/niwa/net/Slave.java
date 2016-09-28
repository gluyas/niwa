package swen222.niwa.net;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

import swen222.niwa.Controller;
import swen222.niwa.gui.NiwaFrame;

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
		try{
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			NiwaFrame gameWindow = new NiwaFrame(new Controller());
			boolean exit = false;

			while(!exit) {
				// read event
				if(input.available() != 0){
					System.out.println("Something is arriving from master");
					int amount = input.readInt();
					byte[] data = new byte[amount];
					input.readFully(data);
					// at this point I am just testing sending strings from master to slave
					// decode the string
					System.out.println(new String(data, Charset.defaultCharset()));
					//game.fromByteArray(data);
					//display.repaint();
				}else{
					String message = inputString("Type your message:");
					byte[] encodedMessage = message.getBytes();
					output.write(encodedMessage);
					output.flush();
				}
			}
			socket.close(); // release socket ... v.important!


		}catch(IOException e){

		}
	}

	private static String inputString(String msg){
		System.out.println(msg);
		while(true){ // loop indefinitely
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			try{
				return input.readLine(); // return the user's input
			}catch(IOException e){

			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
				output.writeBytes("right");
			} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
				output.writeBytes("left");
			} else if(code == KeyEvent.VK_UP) {
				output.writeBytes("up");
			} else if(code == KeyEvent.VK_DOWN) {
				output.writeBytes("down");
			}
			output.flush();
		} catch(IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server.  So, we just ignore it.
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
