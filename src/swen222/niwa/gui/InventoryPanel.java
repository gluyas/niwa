
package swen222.niwa.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonModel;
import javax.swing.JPanel;

import swen222.niwa.Controller;
import swen222.niwa.model.entity.ObjectEntity;

/**
 * Panel for displaying the inventory. Creates and manages the inventory buttons
 *
 * @author Zoe
 *
 */
public class InventoryPanel extends JPanel implements Observer {

	// the below will change depending on how many items we let players hold
	private static final int INV_SIZE = 9;
	private Controller control;

	private InventoryBtn[] buttons;
	private DeselectableButtonGroup btnGroup;

	public InventoryPanel(Controller control) {
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
	 * Takes in an ArrayList of ObjectEntity, iterates through each item
	 * updating inventory buttons accordingly
	 *
	 * @param items
	 *            - ArrayList of ObjectEntity in inventory
	 */
	public void updateInventory(ArrayList<ObjectEntity> items) {
		int i = 0;
		for (ObjectEntity item : items) {
			buttons[i].updateButton(item);
			i++;
		}
	}

	/**
	 * Returns the ObjectEntity stored in the selected inventory button, null if
	 * none selected
	 *
	 * @return
	 */
	public ObjectEntity getSelectedItem() {
		InventoryBtn selected = null;
		for (int i = 0; i < INV_SIZE; i++) {
			if (buttons[i].isSelected()) {
				return buttons[i].getItem();
			}
		}
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		// updateInventory();
	}

}
