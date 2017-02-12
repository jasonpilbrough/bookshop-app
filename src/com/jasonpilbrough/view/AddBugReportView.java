package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;
import net.java.dev.designgridlayout.Tag;

public class AddBugReportView extends JFrame implements Drawable {

	private JTextArea message;
	private SmartJButton confirm, cancel;
	
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
	    setBounds(0, 0, 405, 500);
	    setTitle("Report a bug");
	    
	    message = new JTextArea();
	    message.setLineWrap(true);
	    message.setWrapStyleWord(true);
	    
	   
	    confirm = new SmartJButton("Report bug","confirm").withRegisteredController(controller);

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
		
		message = new JTextArea(message.getText());
		message.setLineWrap(true);
	    message.setWrapStyleWord(true);
		confirm = new SmartJButton().withSomeState(confirm);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400,150));
		//scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(message);
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		
		RowGroup infoGroup = new RowGroup();
        addGroup(layout, "Note");
        
		layout.row().group(infoGroup).grid().add(new JLabel("Please be as descriptive as you can. Be sure to include the"));
		layout.row().group(infoGroup).grid().add(new JLabel("the name of the screen that was open, the action that led to"));
		layout.row().group(infoGroup).grid().add(new JLabel("the error and any error messages that appeared."));
		layout.row().group(infoGroup).grid().add(new JLabel(""));
		
		RowGroup exampleGroup = new RowGroup();
        addGroup(layout, "Example");
		layout.row().group(exampleGroup).grid().add(new JLabel("In the 'View Member' screen, clicking on the 'Confirm' button"));
		layout.row().group(exampleGroup).grid().add(new JLabel("caused all the member's loans to disappear. No error message"));
		layout.row().group(exampleGroup).grid().add(new JLabel("appeared."));
		layout.row().group(infoGroup).grid().add(new JLabel(""));
		
		RowGroup messageGroup = new RowGroup();
        addGroup(layout, "Message");
		layout.row().group(messageGroup).grid().add(new JLabel(""));
		layout.row().group(messageGroup).grid().add(scrollPane);
        layout.row().bar().add(confirm,Tag.APPLY);
		
		getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();
        repaint();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("message", message.getText());
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

