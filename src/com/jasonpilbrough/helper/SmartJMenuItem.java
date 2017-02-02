package com.jasonpilbrough.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.jasonpilbrough.vcontroller.Controller;

public class SmartJMenuItem extends JMenuItem {
	
	private String action;

	public SmartJMenuItem(String text) {
		this(text,text);
	}
	
	public SmartJMenuItem(String text, String action) {
		super(text);
		this.action = action.toLowerCase();
	}
	
    
    public SmartJMenuItem withRegisteredController(Controller c) {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	c.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, action));
            }
        });
        return this;
    }
}
