package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;
import net.java.dev.designgridlayout.Tag;

public class UserSettingsView extends SmartJFrame implements Drawable {

	private SmartJTextField username;
	private JPasswordField password;
	private SmartJButton confirm, cancel;
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "username":
			username.setText(evt.getNewValue().toString());
			draw();
			break;
		case "password":
			password.setText(evt.getNewValue().toString());
			draw();
			break;
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
		setVisible(true);
	    setBounds(0, 0, 250, 150);
	    setTitle("User Settings");
	    
	    username = new SmartJTextField();
	    password = new JPasswordField();
	    confirm = new SmartJButton("Apply","confirm").withRegisteredController(controller);
	    cancel = new SmartJButton("Cancel","cancel").withRegisteredController(controller);

	    controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
		
		username = new SmartJTextField().withSomeState(username);
		password = new JPasswordField(new String(password.getPassword()));
		confirm = new SmartJButton().withSomeState(confirm);
		cancel = new SmartJButton().withSomeState(cancel);
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		
		RowGroup group = new RowGroup();
        addGroup(layout, "Login Details");
        
		layout.row().group(group).grid(new JLabel("Username:")).add(username);
		layout.row().group(group).grid(new JLabel("Password:")).add(password);
        layout.row().bar().add(confirm,Tag.APPLY).add(cancel, Tag.CANCEL);
		
		getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();
        repaint();
	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("username", username.getText());
		map.put("password", password.getPassword());
		return map;
	}
	
	private void addGroup(DesignGridLayout layout, String name, RowGroup group)
  	{
  		JCheckBox groupBox = new JCheckBox(name);
  		groupBox.setName(name);
  		groupBox.setForeground(Color.BLUE);
  		groupBox.setSelected(true);
  		groupBox.addItemListener(new ShowHideAction(group));
  		layout.emptyRow();
  		layout.row().left().add(groupBox, new JSeparator()).fill();
  	}
  	
  	private void addGroup(DesignGridLayout layout, String name)
  	{
  		JLabel group = new JLabel(name);
  		group.setForeground(Color.BLUE);
  		layout.emptyRow();
  		layout.row().left().add(group, new JSeparator()).fill();
  	}
  	

private class ShowHideAction implements ItemListener
  	{
  		public ShowHideAction(RowGroup group)
  		{
  			_group = group;
  		}
  		
  		@Override public void itemStateChanged(ItemEvent event)
  		{
  			if (event.getStateChange() == ItemEvent.SELECTED)
  			{
  				_group.show();
 			}
  			else
  			{
  				_group.hide();
  			}
  			pack();
  		}
  		
  		final private RowGroup _group;
 	}

}
