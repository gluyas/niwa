package swen222.niwa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import swen222.niwa.gui.NiwaFrame;
import swen222.niwa.gui.WinDialog;
import swen222.niwa.model.entity.ObjectEntity;

/**
 * Controller for the game. Responsible for handling interactions between model
 * and view.
 *
 * @author Jack U, Zoe
 *
 */
public class Controller implements Observer, ActionListener {

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

	public void gameWon(String scores){
		new WinDialog(view, scores);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ((get someone 2 help w/ observer pattern and what it means for this))
		ObjectEntity selected = view.invPanel.getSelectedItem();
		switch (e.getActionCommand()) {
		case "Use Item":
			if (selected != null) {
				// use this item
			}
			break;
		case "Inspect":
			if (selected == null) {
				// do inspect stuff in game world if we support that
				// if nothing to inspect there return empty string
			} else {
				// set text to selected.getDescription();
			}
			break;
		case "Drop":
			if (selected != null) {
				// drop this item
			}
			break;
		case "Rotate CW(Q)":
			// rotate clockwise
			break;
		case "Rotate CCW(E)":
			// rotate counterclockwise
			break;
		case "New Game":
			// start a new game
			break;
		case "Save":
			// save the game
			break;
		case "Load":
			// load a saved game
			break;
		default:
			// unknown command
		}
	}

}
