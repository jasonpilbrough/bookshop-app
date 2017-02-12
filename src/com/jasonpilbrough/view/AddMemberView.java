package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJCheckBox;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;
import net.java.dev.designgridlayout.Tag;

public class AddMemberView extends SmartJFrame implements Drawable {
	
	private SmartJTextField name, phone;
    private SmartJButton cancel, confirm;
    private SmartJCheckBox checkBox;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		switch(evt.getPropertyName()){
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
	       setBounds(0, 0, 300, 175);
	       setTitle("Add Member");
	       
	       name = new SmartJTextField("");
	       phone = new SmartJTextField("");
	       checkBox = new SmartJCheckBox("Defer Fee Payment");
	       
	       cancel = new SmartJButton("Cancel").withRegisteredController(controller);
	       confirm = new SmartJButton("Confirm").withRegisteredController(controller);

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        
        
        name = new SmartJTextField().withSomeState(name);
        phone = new SmartJTextField().withSomeState(phone);
        checkBox = new SmartJCheckBox().withSomeState(checkBox);
        
        cancel = new SmartJButton().withSomeState(cancel);
        confirm = new SmartJButton().withSomeState(confirm);
        
        DesignGridLayout layout = new DesignGridLayout(parent);
        
        RowGroup group = new RowGroup();
        addGroup(layout, "Member Details");
        
        layout.row().group(group).grid(new JLabel("Name:")).add(name);
        layout.row().group(group).grid(new JLabel("Phone Number:")).add(phone);
        layout.row().group(group).grid(new JLabel("Membership:")).add(checkBox);
        //layout.row().bar().add(cancel,Tag.CANCEL).add(confirm, Tag.APPLY);
        layout.row().bar().add(confirm, Tag.APPLY);
        getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();

	}
	
	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("name", name.getText());
		map.put("phone_number", phone.getText());
		map.put("defered", ""+checkBox.isSelected());
		return map;
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
