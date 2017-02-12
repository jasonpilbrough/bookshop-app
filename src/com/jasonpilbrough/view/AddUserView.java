package com.jasonpilbrough.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJCheckBox;
import com.jasonpilbrough.helper.SmartJComboBox;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.Tag;

public class AddUserView extends SmartJFrame implements Drawable {


	private SmartJTextField username,password;
    private SmartJCheckBox defaultPassword;
    private SmartJButton confirm;
    private SmartJComboBox<String> combo;
    
    
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "combo_model":
			combo.setModel((ComboBoxModel<String>)evt.getNewValue());
			draw();
			break;	
		case "close":
			setVisible(false);
			draw();
			break;
			default:
				throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}

	}

	@Override
	public void initialise(Controller controller) {
		setVisible(true);
	    setBounds(0, 0, 300, 185);
	    setTitle("Add User");
	    
	    username = new SmartJTextField("");
	    password = new SmartJTextField("");
	    defaultPassword = new SmartJCheckBox("Use default password");
	    defaultPassword.setSelected(true);
	    defaultPassword.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				draw();
			}
		});
	    
	    combo = new SmartJComboBox<>();
	    confirm = new SmartJButton("Confirm").withRegisteredController(controller);
	    
	    controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
	    
	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        

        username = new SmartJTextField().withSomeState(username);
        password = new SmartJTextField().withSomeState(password);
        defaultPassword = new SmartJCheckBox().withSomeState(defaultPassword);
        confirm = new SmartJButton().withSomeState(confirm);
        
        combo  = new SmartJComboBox<>().withSomeState(combo);
        
        DesignGridLayout layout = new DesignGridLayout(parent);
        layout.row().grid(new JLabel("Username:")).add(username);
        layout.row().grid().add(defaultPassword);
        if(!defaultPassword.isSelected()){
        	 layout.row().grid(new JLabel("Password:")).add(password);
        }
       
        layout.row().grid(new JLabel("Access:")).add(combo);
        //layout.row().bar().add(cancel,Tag.CANCEL).add(confirm, Tag.APPLY);
        
        layout.row().bar().add(confirm, Tag.APPLY);
        getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("username", username.getText());
		map.put("access", combo.getSelectedItem().toString());
		map.put("password",password.getText());
		map.put("default_password", defaultPassword.isSelected());
		return map;
	}

}
