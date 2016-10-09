package swen222.niwa.gui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Dialog window that displays after the game is won, shows player scores
 * 
 * @author Zoe
 *
 */
public class WinDialog extends JDialog {

	public WinDialog(JFrame frame) {
		super(frame, "game over, thank you for playing!");

		setPreferredSize(new Dimension(400, 300));
		setResizable(false);
		// if we want something else to happen pls advise
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(scoreTable());
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	// this is method will dependent on how the scores are delivered
	public JPanel scoreTable() {
		JPanel panel = new JPanel();
		JTextArea scores = new JTextArea();
		scores.setEditable(false);
		scores.setText("here \nare \nsome \nscores \ni guess");
		panel.add(scores);
		return panel;
	}

}
