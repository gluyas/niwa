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

	private Controller control;

	private JTextArea textArea;
	//private JLabel score;

	public MainAppPanel(Controller control) {
		this.control = control;

		setBackground(new Color(0xffdab9));

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(500, 40));
		//score = new JLabel("0");

		add(textArea);
		add(buttons());
		//add(score);
	}

	/**
	 * creates the rotate, action, and inspect buttons and the panel that
	 * contains them
	 * 
	 * @return JPanel containing the buttons
	 */
	private JPanel buttons() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 2, 2));
		panel.setOpaque(false);

		NiwaBtn rotCW = new NiwaBtn("Rotate CW (Q)");
		NiwaBtn rotCCW = new NiwaBtn("Rotate CCW (E)");
		NiwaBtn action = new NiwaBtn("Action");
		NiwaBtn inspect = new NiwaBtn("Inspect");

		panel.add(rotCW);
		panel.add(rotCCW);
		panel.add(action);
		panel.add(inspect);
		return panel;
	}

	/**
	 * Updates the text area to display the given text
	 * 
	 * @param s
	 *            - String to be displayed in the text area
	 */
	public void updateText(String s) {
		textArea.setText(s);
	}

//	/**
//	 * Updates the score label
//	 * 
//	 * @param s
//	 *            - String representation of the player's score
//	 */
//	public void updateScore(String s) {
//		score.setText(s);
//	}

}
