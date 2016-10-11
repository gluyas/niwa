package swen222.niwa;


import java.util.Observable;
import java.util.Observer;

import swen222.niwa.gui.NiwaFrame;
/**
 * Server for the game. Responsible for handling interactions
 * between model and view. 
 * @author Jack U
 *
 */
public class Controller implements Observer{

	private final NiwaFrame view;
	
	public Controller() {
		this.view = new NiwaFrame(this);
		
	}
	
	public static void main(String[] args) {
		new Controller();
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
