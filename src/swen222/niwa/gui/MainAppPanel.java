package swen222.niwa.gui;

import java.awt.BorderLayout;
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

	private static final int HEIGHT = 50;
	private static final int WIDTH = 360;
	private Controller control;

	private JTextArea textArea;
	private JLabel score;

	public MainAppPanel(Controller control) {
		this.control = control;

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(WIDTH/2, HEIGHT - (HEIGHT / 10)));
		score = new JLabel("0");

		JPanel textThings = new JPanel(new BorderLayout());
		textThings.add(textArea, BorderLayout.SOUTH);
		textThings.add(score, BorderLayout.NORTH);
		add(textThings, BorderLayout.WEST);
		add(rotateButtons(), BorderLayout.CENTER);
		add(actionButtons(), BorderLayout.EAST);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(WIDTH, HEIGHT);
	}

	/**
	 * creates the game action buttons and the panel that contains them
	 * 
	 * @return
	 */
	private JPanel actionButtons() {
		JPanel panel = new JPanel(new GridLayout(1, 3, 2, 2));
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
		NiwaBtn rotLeft = new NiwaBtn("Rotate CW(Q)");
		panel.add(rotLeft);
		NiwaBtn rotRight = new NiwaBtn("Rotate CCW(E)");
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
