package com.jasonpilbrough.helper;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.jasonpilbrough.vcontroller.Controller;

/* Helper class which simplifies the adding of action listeners to buttons
 * 
 * 
 */
public class SmartJButton extends JButton{
    
    private String action;

    public SmartJButton(String text,String action) {
        super(text);
        this.action = action;
    }
    
    public SmartJButton(String text) {
        this(text, text.toLowerCase());
    }
    
    public SmartJButton() {
        this("");
    }
   
      
    public SmartJButton withRegisteredController(final Controller c) {
        addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				c.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, action));
				
			}
		});
        return this;
    }
    
    /* The need for this method arrives from the way the layout manager cannot add components
     * that are already added to another container. It becomes necessary to create a duplicate component
     * and add that to the new container. This method passes on the state of the old component to the
     * new component 
     */
    public SmartJButton withSomeState(SmartJButton button){
    	action = button.action;
    	this.setText(button.getText());
    	this.setEnabled(button.isEnabled());
        this.setVisible(button.isVisible());
        this.setPreferredSize(button.getPreferredSize());
        this.setHorizontalTextPosition(button.getHorizontalTextPosition());
        this.setIcon(button.getIcon());
        this.setFont(button.getFont());
        
        
        for(ActionListener al:button.getActionListeners()){
        	this.addActionListener(al);
        }
    	return this;
    }
    
    
    
    
    
}
