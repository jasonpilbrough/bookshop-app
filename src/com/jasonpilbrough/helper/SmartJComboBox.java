package com.jasonpilbrough.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.jasonpilbrough.vcontroller.Controller;

public class SmartJComboBox<E> extends JComboBox<E> {
	
	private String action;

	public SmartJComboBox(ComboBoxModel<E> model, String action) {
		super(model);
		this.action = action;
	}
	public SmartJComboBox(String action) {
		super();
		this.action = action;
	}
	
	public SmartJComboBox() {
		super();
	}
	
	public SmartJComboBox withRegisteredController(final Controller controller){
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, action));
				
			}
		});
		return this;
	}
	
	
	/* The need for this method arrives from the way the layout manager cannot add components
     * that are already added to another container. It becomes necessary to create a duplicate component
     * and add that to the new container. This method passes on the state of the old component to the
     * new component 
     */
    public SmartJComboBox withSomeState(SmartJComboBox box){
    	this.action = box.action;
    	this.setEnabled(box.isEnabled());
        this.setVisible(box.isVisible());
        this.setModel(box.getModel());
        
        for(ActionListener al:box.getActionListeners()){
        	this.addActionListener(al);
        }
        
    	return this;
    }
}
