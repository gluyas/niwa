
package swen222.niwa.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import swen222.niwa.Controller;

/**
 * Panel for displaying the inventory. Creates and manages the inventory buttons
 * @author Zoe
 *
 */
public class InventoryPanel extends JPanel{

	private static final int HEIGHT = 550;
	private static final int WIDTH = HEIGHT/2;
	// the below will change depending on how many items we let players hold
	private static final int INV_WIDTH = 3;
	private static final int INV_HEIGHT = 6;
	private Controller control;

	private InventoryBtn[] buttons;
	private DeselectableButtonGroup group;

	public InventoryPanel(Controller control){
		this.control = control;
		buttons = new InventoryBtn[INV_WIDTH*INV_HEIGHT];
		group = new DeselectableButtonGroup();

		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new InventoryBtn();
			group.add(buttons[i]);
			add(buttons[i]);
		}

		setLayout(new GridLayout(INV_HEIGHT, INV_WIDTH, 2, 2));
		setBackground(Color.DARK_GRAY);// for testing, don't worry about it


		updateInventory(getTestSet());
	}

	private void updateInventory(Map<String,Integer> items){
		int i = 0;
		for(String s : items.keySet()){
			buttons[i].updateButton(s, items.get(s));
			i++;
		}
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(WIDTH, HEIGHT);
	}

	//methods for testing concept

	private Map<String,Integer> getTestSet(){
		Map<String,Integer> items = new HashMap<>();
		items.put("red", 1);
		items.put("blue", 2);
		items.put("green", 3);
		items.put("HAH, you thought it was gonna be a colour again", 5);
		return items;
	}

}

