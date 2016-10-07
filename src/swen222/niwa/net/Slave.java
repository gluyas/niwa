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
import swen222.niwa.demo.DemoPlayer;
import swen222.niwa.gui.NiwaFrame;
import swen222.niwa.gui.RoomRenderer;
import swen222.niwa.model.world.Room;
import swen222.niwa.model.entity.Entity;
import swen222.niwa.model.util.EntityTable;
import swen222.niwa.model.util.HashEntityTable;
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

	private static EntityTable<Entity> et = new HashEntityTable<>();
	private Room r;
	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private ObjectInputStream objIn;
	private DemoFrame gameWindow;

	//TODO: Very much just a place holder at the moment, will obviously need further implementation

	public Slave(Socket socket){
		this.socket = socket;
	}

	public void run(){

		try{
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			objIn = new ObjectInputStream(socket.getInputStream());

			boolean exit = false;

			while(!exit) {
				// read event
				if(objIn.available() != 0){
					System.out.println("Something is arriving from master");
					byte action = objIn.readByte();
					switch (action) {
						case 'r': // rooms
							System.out.println("master sent me a room");
							// TODO: initial room state, should only be sent once
							// and then the remaining mutable objects will be sent
							// over e.g. entities
							r = (Room)objIn.readObject();
							gameWindow = new DemoFrame(r, this);
							break;
						case 'p':
							// TODO: updated player state
							System.out.println("received a player");
							DemoPlayer p = (DemoPlayer) objIn.readObject();;
							System.out.println(p);
							et.add(p);
							for (Entity e : et) System.out.println(e.toString());
							gameWindow.repaint();
							break;
						case 's':
							// TODO: updated seed state
					}
				}
			}
			socket.close(); // release socket ... v.important!


		}catch(Exception e) {
			throw new Error(e);
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

	//TODO: fix this horrible non-encapsulation
	public static EntityTable getEntityTable() {
		return et;
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
			case VK_W: // north
				output.writeInt(1);
				break;

			case VK_A: // west
				output.writeInt(3);
				break;

			case VK_S: // south
				output.writeInt(2);
				break;

			case VK_D: // east
				output.writeInt(4);
				break;

			case VK_Q: // rotate cw
				break;

			case VK_E: // rotate ccw
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
