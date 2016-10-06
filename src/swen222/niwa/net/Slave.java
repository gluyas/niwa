package swen222.niwa.net;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

import swen222.niwa.Controller;
import swen222.niwa.demo.DemoFrame;
import swen222.niwa.gui.NiwaFrame;
import swen222.niwa.gui.RoomRenderer;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.world.Direction;
import swen222.niwa.model.world.Room;

/**
 * A slave connection receives information about the current state of the board
 * and relays that into the local copy of the board. The slave connection also
 * notifies the master connection of key presses by the player.
 *
 * @author Hamish M
 */

public class Slave extends Thread implements KeyListener{

	//TODO: fix this horrible non-encapsulation
	public static EntityTable getEntityTable() {
		return et;
	}

	private static EntityTable et;
	private Room r;

	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private ObjectInputStream objin;
	private DemoFrame gameWindow;

	//TODO: Very much just a place holder at the moment, will obviously need further implementation

	public Slave(Socket socket){
		this.socket = socket;
	}

	public void run(){

		try{
			output = new DataOutputStream(socket.getOutputStream());

			input = new DataInputStream(socket.getInputStream());
			objin = new ObjectInputStream(socket.getInputStream());

			// Open up the demo frame, will be the actual client window eventually
			
			boolean exit = false;

			while(!exit) {
				// read event
				if(input.available() != 0){
					System.out.println("Something is arriving from master");
					byte action = input.readByte();
					switch (action) {
						case 'r': // room
							// TODO: initial room state, should only be sent once
							// and then the remaining mutable objects will be sent
							// over e.g. entities
							break;
						case 'p':
							// TODO: updated player state
							break;
						case 's':
							// TODO: updated seed state
					}
				}
			}
			socket.close(); // release socket ... v.important!


		}catch(IOException e){

		//} catch (ClassNotFoundException e) {

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
		try{
			int code = e.getKeyCode();
			switch (code) {
			case VK_W:
				output.writeInt(1);
				gameWindow.getPlayer().move(Direction.NORTH);
				break;

			case VK_A:
				output.writeInt(3);
				gameWindow.getPlayer().move(Direction.WEST);
				break;

			case VK_S:
				output.writeInt(2);
				gameWindow.getPlayer().move(Direction.SOUTH);
				break;

			case VK_D:
				output.writeInt(4);
				gameWindow.getPlayer().move(Direction.EAST);
				break;

			case VK_Q:
				gameWindow.getRR().rotateCW();
				gameWindow.repaint();
				break;

			case VK_E:
				gameWindow.getRR().rotateCCW();
				gameWindow.repaint();
				break;
			}
		}catch(IOException ee){

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
