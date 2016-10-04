package swen222.niwa.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import swen222.niwa.Controller;

public class MainAppPanel extends JPanel{
	
	private static final int HEIGHT = 550;
	private static final int WIDTH = HEIGHT/2;
	private Controller control;
	
	private JTextArea textArea;
	private JLabel score;
	
	public MainAppPanel(Controller control){
		this.control = control;
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		score = new JLabel("0");
		
		add(textArea);
		add(actionButtons());
		setBackground(Color.MAGENTA);// for testing, don't worry about it
	}
	
	private JPanel actionButtons(){
		JPanel panel = new JPanel(new GridLayout(3, 1, 2, 2));
		NiwaBtn drop = new NiwaBtn("Drop");
		panel.add(drop);
		NiwaBtn action = new NiwaBtn("Action");
		panel.add(action);
		NiwaBtn inspect = new NiwaBtn("Inspect");
		panel.add(inspect);
		
		return panel;
	}
	
	public void updateText(String s){
		textArea.setText(s);
	}
	
	public void updateScore(String s){
		score.setText(s);
	}

}
