package swen222.niwa.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import swen222.niwa.Controller;

/**
 * The panel that creates and manages all the non-inventory components of the
 * application window
 * 
 * @author Zoe
 *
 */
public class MainAppPanel extends JPanel {

	private static final int HEIGHT = 550;
	private static final int WIDTH = HEIGHT / 2;
	private Controller control;

	private JTextArea textArea;
	private JLabel score;

	public MainAppPanel(Controller control) {
		this.control = control;

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(HEIGHT - (HEIGHT / 4), WIDTH / 3));
		score = new JLabel("0");

		add(textArea);
		add(rotateButtons());
		add(actionButtons());
	}

	/**
	 * creates the game action buttons and the panel that contains them
	 * 
	 * @return
	 */
	private JPanel actionButtons() {
		JPanel panel = new JPanel(new GridLayout(3, 1, 2, 2));
		NiwaBtn drop = new NiwaBtn("Drop");
		panel.add(drop);
		NiwaBtn action = new NiwaBtn("Action");
		panel.add(action);
		NiwaBtn inspect = new NiwaBtn("Inspect");
		panel.add(inspect);

		return panel;
	}

	/**
	 * creates the rotation buttons and the panel
	 * 
	 * @return
	 */
	private JPanel rotateButtons() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 2, 2));
		NiwaBtn rotLeft = new NiwaBtn("Rotate Left(Q)");
		panel.add(rotLeft);
		NiwaBtn rotRight = new NiwaBtn("Rotate Right(E)");
		panel.add(rotRight);
		return panel;
	}

	/**
	 * Updates the text area to display the given text
	 * @param s - String to be displayed in the text area
	 */
	public void updateText(String s) {
		textArea.setText(s);
	}

	/**
	 * Updates the score label
	 * @param s - String representation of the player's score
	 */
	public void updateScore(String s) {
		score.setText(s);
	}

}
