package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

public class ChangeLogView extends JFrame implements Drawable {
	
	private String[] details;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
			case "versions":
				details = (String[])evt.getNewValue();
				draw();
				break;
			default:
				throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}
	}

	@Override
	public void initialise(Controller controller) {
		 setVisible(true);
	     setBounds(0, 0, 400, 400);
	     setTitle("Change Log");
	     
	     
	     controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
		//parent.setBorder(new BevelBorder(1));
		//parent.setPreferredSize(new Dimension(400, 300));	
		
		
 
		
		
		DesignGridLayout layout = new DesignGridLayout(parent);
        //layout.row().grid().add(scrollPane);
		//this loop starts at 1 to remove the leading white space
		for (int i = 1; i < details.length; i++) {
	        String[] lines = details[i].split(":");
			for (String line : lines) {
				JLabel label = new JLabel(line);
				label.setFont (new Font("Menlo",Font.PLAIN,12));
				layout.row().grid().add(label);
			}
			RowGroup group = new RowGroup();
	        addGroup(layout, "");
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(290,265));
		scrollPane.setViewportView(parent);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		getContentPane().removeAll();
		getContentPane().add(scrollPane);   
        revalidate();
        repaint();

	}

	@Override
	public Map<String, Object> getFields() {
		// TODO Auto-generated method stub
		return null;
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
