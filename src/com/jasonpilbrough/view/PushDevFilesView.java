package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class PushDevFilesView extends SmartJFrame implements Drawable{

	private JLabel progress;
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "complete":
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, "Upload complete");
			break;
		case "progress":
			progress.setText(evt.getNewValue().toString());
			break;
		case "failed":
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, evt.getNewValue().toString());
			break;
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}
	}
		
	@Override
	public void initialise(Controller controller) {
		setVisible(true);
	    setBounds(0, 0, 200, 50);
	    setTitle("Push Dev Files");
	    
	    progress = new JLabel();
	    
	   

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
		
		progress = new JLabel(progress.getText());

		DesignGridLayout layout = new DesignGridLayout(parent);
		
		
		layout.row().grid(new JLabel("Status:")).add(progress);
		
		getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();
        repaint();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		return map;
	}
  	
  	private void addGroup(DesignGridLayout layout, String name)
  	{
  		JLabel group = new JLabel(name);
  		group.setForeground(Color.BLUE);
  		layout.emptyRow();
  		layout.row().left().add(group, new JSeparator()).fill();
  	}
}
