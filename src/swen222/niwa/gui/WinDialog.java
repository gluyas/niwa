package swen222.niwa.gui;

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

		// if we want something else to happen pls advise
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(scoreTable());
		pack();
		setVisible(true);
	}

	// this is method will dependent on how the scores are delivered
	public JPanel scoreTable() {
		JPanel panel = new JPanel();
		JTextArea scores = new JTextArea();
		scores.setEditable(false);

		panel.add(scores);
		return panel;
	}

}
