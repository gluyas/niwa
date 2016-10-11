package swen222.niwa.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Window that appears after the game is won displaying player scores
 *
 * @author Zoe
 *
 */
public class WinDialog extends JDialog {

	public WinDialog(JFrame frame, String s) {
		super(frame, "game over, thank you for playing!");

		setPreferredSize(new Dimension(400, 220));
		setResizable(false);
		// if we want something else to happen on close pls advise
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(head(), BorderLayout.NORTH);
		add(scoreTable(s), BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	public JPanel head() {
		JPanel panel = new JPanel();
		// make it some nice color idk
		panel.setBackground(new Color(0xA9EF6B));
		JLabel playerScores = new JLabel("Player Scores:");
		//make the header font bigger
		playerScores.setFont(playerScores.getFont().deriveFont(30f));
		panel.add(playerScores);
		return panel;
	}

	// this is method will depend on how the scores are delivered
	/**
	 * Creates the panel and text area that contains the scores, will take in
	 * some kind of parameter with the info to display
	 *
	 * @return
	 */
	public JPanel scoreTable(String s) {
		JPanel panel = new JPanel();
		// make it some nice color idk
		panel.setBackground(new Color(0xA9EF6B));
		JTextArea scores = new JTextArea();
		scores.setOpaque(false);
		scores.setEditable(false);
		//make the scores font a bit bigger too bc the default is tiny
		scores.setFont(scores.getFont().deriveFont(16f));
		scores.setText("here \nare \nsome \nscores \ni guess");
		panel.add(scores);
		return panel;
	}

}
