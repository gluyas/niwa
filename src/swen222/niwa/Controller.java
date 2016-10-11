package swen222.niwa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import swen222.niwa.gui.NiwaFrame;
import swen222.niwa.gui.WinDialog;

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
		String selected = view.invPanel.getSelectedItem();
		switch (e.getActionCommand()) {
		case "Use Item":
			if (!selected.equals("Empty")&&!selected.equals("null")) {
				// do action stuff
				System.out.println("used " + selected);
				}
				// nothing to do
			break;
		case "Inspect":
			if (selected.equals("null")) {
				// do inspect stuff in game world if we support that, or do
				// nothing
				System.out.println("I don't know what that is.");
			} else if (!selected.equals("Empty")) {
				// do inspect stuff with this object
				// probs return a string held in that object
				System.out.println("it's " + selected);
			}
			break;
		case "Drop":
			if (!selected.equals("Empty")&&!selected.equals("null")) {
				// drop this object
				System.out.println("dropped " + selected);
				}
				// nothing to do
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
