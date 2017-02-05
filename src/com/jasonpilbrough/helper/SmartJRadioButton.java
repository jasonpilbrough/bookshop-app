package com.jasonpilbrough.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import com.jasonpilbrough.vcontroller.Controller;

public class SmartJRadioButton extends JRadioButton {

	public SmartJRadioButton(String text, ButtonGroup group) {
        this(text);
        group.add(this);
    }
	
	public SmartJRadioButton(String text) {
        super(text);
    }
    
    public SmartJRadioButton() {
        this("");
    }
    
    public SmartJRadioButton withRegisteredController(final Controller controller){
    	this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "access level changed"));
				
			}
		});
    	return this;
    }
	
	/* The need for this method arrives from the way the layout manager cannot add components
     * that are already added to another container. It becomes necessary to create a duplicate component
     * and add that to the new container. This method passes on the state of the old component to the
     * new component 
     */
    public SmartJRadioButton withSomeState(SmartJRadioButton button, ButtonGroup group){
    	group.add(this);
    	this.setText(button.getText());
    	this.setEnabled(button.isEnabled());
        this.setVisible(button.isVisible());
        this.setSelected(button.isSelected());
        this.setFont(button.getFont());
        
        for(ActionListener al:button.getActionListeners()){
        	this.addActionListener(al);
        }
        
    	return this;
    }
}
