package swen222.niwa;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import swen222.niwa.gui.NiwaFrame;
import swen222.niwa.gui.WinDialog;
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
		new WinDialog(view);
	}
	
	public static void main(String[] args) {
		new Controller();
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ((get someone 2 help w/ observer pattern and what it means for this))
		if(e.getActionCommand().equals("Action")){
			//do action stuff
		}else if(e.getActionCommand().equals("Inspect")){
			String selected = view.invPanel.getSelectedItem();
			if(selected.equals("null")){
				//do inspect stuff in game world if we support that, or do nothing
				System.out.println("I don't know what that is.");
			}else{
				//do inspect stuff with this object
				//probs return a string held in that object
				System.out.println("it's "+selected);
			}
		}else if(e.getActionCommand().equals("Rotate CW (Q)")){
			//turn clockwise
		}else if(e.getActionCommand().equals("Rotate CCW (E)")){
			//turn counterclockwise
		}
	}

}
