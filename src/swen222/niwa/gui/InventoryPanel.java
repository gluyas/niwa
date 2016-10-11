
package swen222.niwa.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Map;

import javax.swing.ButtonModel;
import javax.swing.JPanel;

import swen222.niwa.Client;

/**
 * Panel for displaying the inventory. Creates and manages the inventory buttons
 *
 * @author Zoe
 *
 */
public class InventoryPanel extends JPanel {

	// the below will change depending on how many items we let players hold
	private static final int INV_SIZE = 9;
	private Client control;

	private InventoryBtn[] buttons;
	private DeselectableButtonGroup btnGroup;

	public InventoryPanel(Client control) {
		this.control = control;
		buttons = new InventoryBtn[INV_SIZE];
		btnGroup = new DeselectableButtonGroup();

		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new InventoryBtn();
			btnGroup.add(buttons[i]);
			add(buttons[i]);
		}

		setLayout(new GridLayout(1, INV_SIZE, 2, 2));
		setBackground(new Color(0xfff0f5));
	}

	/**
	 * Takes in a map of item names and counts, iterates through each item
	 * updating inventory buttons accordingly
	 *
	 * @param items - map of item name to number in inventory
	 * @param images - map of item name to image
	 */
	public void updateInventory(Map<String, Integer> items, Map<String, Image> images) {
		int i = 0;
		for (String s : items.keySet()) {
			buttons[i].updateButton(s, items.get(s), images.get(s));
			i++;
		}
	}

	public String getSelectedItem() {
		ButtonModel bMod = btnGroup.getSelection();
		if (bMod == null) {
			return "null";
		} else {
			return bMod.getActionCommand();
		}
	}

}
