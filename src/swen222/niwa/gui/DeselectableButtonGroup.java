package swen222.niwa.gui;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * creates a button group where clicking on the selected button will clear the selection
 * @author Zoe
 *
 */
public class DeselectableButtonGroup extends ButtonGroup{
	
	@Override
	public void setSelected(ButtonModel model, boolean selected){
		if(selected){
			super.setSelected(model, selected);
		}else{
			clearSelection();
		}
	}
}
