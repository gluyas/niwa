
package swen222.niwa.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonModel;
import javax.swing.JPanel;

import swen222.niwa.Client;
import swen222.niwa.model.entity.ObjectEntity;
import swen222.niwa.model.entity.PlayerEntity;

/**
 * Panel for displaying the inventory. Creates and manages the inventory buttons
 *
 * @author Zoe
 *
 */
public class InventoryPanel extends JPanel implements Observer {

	// the below will change depending on how many items we let players hold
	private static final int INV_SIZE = 9;
	private Client control;

	private InventoryBtn[] buttons;
	private DeselectableButtonGroup btnGroup;

	public InventoryPanel(Client control) {
		control.addObserver(this);
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
	 * @param items
	 *            - map of item name to number in inventory
	 * @param images
	 *            - map of item name to image
	 */
	public void updateInventory(List<ObjectEntity> items) {
		// int i = 0;
		for (int i = 0; i < INV_SIZE; i++) {
			if (i < items.size()) {
				buttons[i].updateButton(items.get(i));
			} else {
				buttons[i].resetButton();
			}
		}
	}

	public String getItemDescription() {
		for(int i=0; i < INV_SIZE; i++){
			if(buttons[i].isSelected() && buttons[i].getItem() != null){
				return buttons[i].getItem().getDescription();
			}
		}
		return "";
	}

	public int getSelectedSlot() {
		for (int i = 0; i < INV_SIZE; i++) {
			// protect against player selecting empty slot below
			if (buttons[i].isSelected() && buttons[i].getItem() != null) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void update(Observable o, Object arg) {
		PlayerEntity p = control.getPlayer();
		if (p != null) updateInventory(p.getInventory());
	}

}
