package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class ConsoleView extends JFrame implements Drawable {

	private JTextArea text;
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "text":
			text.setText(evt.getNewValue().toString());
			draw();
			break;
			
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
		 setVisible(true);
	     setBounds(0, 0, 520, 500);
	     setTitle("Console");
	     
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
		//text.setBackground(new Color(238, 238, 238));
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(290,265));
		scrollPane.setViewportView(text);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		DesignGridLayout layout = new DesignGridLayout(parent);
        layout.row().grid().add(scrollPane);
        
		
		getContentPane().removeAll();
		getContentPane().add(parent);   
        revalidate();
        repaint();

	}

	@Override
	public Map<String, Object> getFields() {
		
		return null;
	}

}
