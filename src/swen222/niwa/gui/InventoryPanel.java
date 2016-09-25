package swen222.niwa.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * Panel for displaying the inventory. Creates and manages the inventory buttons
 * @author Zoe
 *
 */
public class InventoryPanel extends JPanel{
	
	private static final int HEIGHT = 550;
	private static final int WIDTH = HEIGHT/2;
	private static final int INV_WIDTH = 5;
	private static final int INV_HEIGHT = 5;
	
	public InventoryPanel(){
		setLayout(new GridLayout(INV_HEIGHT, INV_WIDTH));
	}

}
