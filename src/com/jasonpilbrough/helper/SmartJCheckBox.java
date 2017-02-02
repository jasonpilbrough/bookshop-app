package com.jasonpilbrough.helper;

import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class SmartJCheckBox extends JCheckBox {

	public SmartJCheckBox(String text) {
		super(text);
	}
	public SmartJCheckBox() {
		this("");
	}
	
	
	/* The need for this method arrives from the way the layout manager cannot add components
     * that are already added to another container. It becomes necessary to create a duplicate component
     * and add that to the new container. This method passes on the state of the old component to the
     * new component 
     */
    public SmartJCheckBox withSomeState(SmartJCheckBox box){
    	this.setSelected(box.isSelected());
    	this.setEnabled(box.isEnabled());
        this.setVisible(box.isVisible());
        this.setText(box.getText());
        for(ActionListener al:box.getActionListeners()){
        	this.addActionListener(al);
        }
    	return this;
    }
}
