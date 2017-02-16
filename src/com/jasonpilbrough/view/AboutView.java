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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

public class AboutView extends SmartJFrame implements Drawable {
	
	private String title, version, copyright, developer, disclaimer;
	
	private JTextArea text;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "text":
			text.setText(evt.getNewValue().toString());
			draw();
			break;
		case "title":
			title = evt.getNewValue().toString();
			//draw();
			break;
		case "version":
			version = evt.getNewValue().toString();
			//draw();
			break;
		case "copyright":
			copyright = evt.getNewValue().toString();
			//draw();
			break;
		case "developer":
			developer = evt.getNewValue().toString();
			//draw();
			break;
		case "disclaimer":
			disclaimer = evt.getNewValue().toString();
			draw();
			break;
			
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
		 setVisible(true);
	     setBounds(0, 0, 350, 300);
	     setTitle("About");
	     
	     text = new JTextArea();
	     
	     controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}
	
	

	@Override
	public void draw() {
		JPanel parent = new JPanel();
		//parent.setBorder(new BevelBorder(1));
		//parent.setPreferredSize(new Dimension(400, 300));	
		text = new JTextArea(text.getText());
		text.setEnabled(false);
		text.setDisabledTextColor(Color.black);
		text.setBackground(new Color(238, 238, 238));
		
		
 
		
		
		DesignGridLayout layout = new DesignGridLayout(parent);
        //layout.row().grid().add(scrollPane);
		
		RowGroup loansGroup = new RowGroup();
        addGroup(layout, "");
		String[] parts1 = title.split("---");
		for (String string : parts1) {
			JLabel label = new JLabel(string);
			label.setFont (new Font("Menlo",Font.PLAIN,12));
			layout.row().grid().add(label);
		}
		
		RowGroup versionGroup = new RowGroup();
        addGroup(layout, "");
		String[] parts2 = version.split("---");
		for (String string : parts2) {
			JLabel label = new JLabel(string);
			label.setFont (new Font("Menlo",Font.PLAIN,12));
			layout.row().grid().add(label);
			
			
		}
		
		RowGroup copypyGroup = new RowGroup();
        addGroup(layout, "");
		String[] parts3 = copyright.split("---");
		for (String string : parts3) {
			JLabel label = new JLabel(string);
			label.setFont (new Font("Menlo",Font.PLAIN,12));
			layout.row().grid().add(label);
		}
		
		RowGroup devGroup = new RowGroup();
        addGroup(layout, "");
		String[] parts4 = developer.split("---");
		for (String string : parts4) {
			JLabel label = new JLabel(string);
			label.setFont (new Font("Menlo",Font.PLAIN,12));
			layout.row().grid().add(label);
		}
		
		RowGroup disclaimerGroup = new RowGroup();
        addGroup(layout, "");
		String[] parts5 = disclaimer.split("---");
		for (String string : parts5) {
			JLabel label = new JLabel(string);
			label.setFont (new Font("Menlo",Font.PLAIN,12));
			layout.row().grid().add(label);
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
