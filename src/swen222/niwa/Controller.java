package swen222.niwa;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import swen222.niwa.gui.NiwaFrame;
/**
 * Controller for the game. Responsible for handling interactions 
 * between model and view. 
 * @author Jack U
 *
 */
public class Controller implements Observer, ActionListener{

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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
