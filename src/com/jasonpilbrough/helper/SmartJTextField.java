package com.jasonpilbrough.helper;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import com.jasonpilbrough.vcontroller.Controller;

public class SmartJTextField extends JTextField {

	public SmartJTextField(String text) {
		super(text);
	}
	public SmartJTextField() {
		this("");
	}
	
	
	//TODO not working
	public SmartJTextField withFocusListener(final String prompt){
		this.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				//if(getText().equals(""))
					//setText(prompt);
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(getText().equalsIgnoreCase(prompt)){
					System.out.println("HERE sjtf");
					setText("");
					revalidate();
					repaint();
				}
				
					
				
			}
		});
		return this;
	}
	
	@Override
	public void setText(String t) {
		//System.out.println(t);
		super.setText(t);
	}
	
	
	
	@Override
	public void setCaretPosition(int position) {
		//TODO very very very very dirty fix PLEASE HELP!!!!
		if(position==0){
			super.setCaretPosition(this.getText().length());
		}else{
			super.setCaretPosition(position);
		}
		
	}
	
		//TODO not working
		public SmartJTextField withKeyListener(final Controller controller){
			addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {}
				
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						controller.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "filter search"));
					}else{
						//controller.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "key pressed"));
					}
					
					
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					
				}
			});
			return this;
		}
	
	
	
	/* The need for this method arrives from the way the layout manager cannot add components
     * that are already added to another container. It becomes necessary to create a duplicate component
     * and add that to the new container. This method passes on the state of the old component to the
     * new component 
     */
    public SmartJTextField withSomeState(SmartJTextField field){
    	String fieldText = field.getText();
    	String thisText = getText();
    	//System.out.println(fieldText);
    	this.setText(field.getText());
     	this.setEnabled(field.isEnabled());
        this.setVisible(field.isVisible());
        this.setDisabledTextColor(field.getDisabledTextColor());
        if(field.hasFocus())
        	this.requestFocusInWindow();
        
        for(FocusListener fl:field.getFocusListeners()){
        	this.addFocusListener(fl);
        }
        for(KeyListener kl:field.getKeyListeners()){
        	this.addKeyListener(kl);
        }
    	return this;
    }

	
}
