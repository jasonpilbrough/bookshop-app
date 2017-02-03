package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jasonpilbrough.helper.FileException;
import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class WelcomeView extends JPanel implements Drawable {

	private SmartJButton goLib, goStore;
	private JLabel name,title;
	private String username;
	
	private String resourcesPath = "";
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "username":
			username = evt.getNewValue().toString();
			draw();
			break;
		case "resources_path":
			resourcesPath = evt.getNewValue().toString();
			draw();
			break;
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
		   setVisible(true);
	       setBounds(0, 0, 600, 600);
			
	        title = new JLabel("Highway Christian Community");
	        name = new JLabel();
			goLib = new SmartJButton("Library","go to library").withRegisteredController(controller);
			goStore = new SmartJButton("Bookstore","go to store").withRegisteredController(controller);
			
			goLib.setPreferredSize(new Dimension(100,100));
			goStore.setPreferredSize(new Dimension(100,100));

			
			controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
	}

	@Override
	public void draw() {
		name = new JLabel("Welcome, "+username);
		name.setFont (name.getFont().deriveFont (36.0f));

		title = new JLabel(title.getText());
		title.setFont (title.getFont().deriveFont (14.0f));
		
		
		
		JPanel parent = new JPanel();
		//parent.setBorder(new BevelBorder(1));
		//parent.setPreferredSize(new Dimension(400, 300));
		
		goLib = new SmartJButton("Go to Library").withSomeState(goLib);
		goStore = new SmartJButton("Go to Store").withSomeState(goStore);
		
		goLib.setHorizontalTextPosition(SwingConstants.CENTER);
		goLib.setFont (goLib.getFont().deriveFont (12.0f));
		goLib.setVerticalTextPosition(SwingConstants.BOTTOM);
		
		goStore.setHorizontalTextPosition(SwingConstants.CENTER);
		goStore.setFont (goLib.getFont().deriveFont (12.0f));
		goStore.setVerticalTextPosition(SwingConstants.BOTTOM);
		
		try {
			InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcesPath+"book_shelf.png");
			if(stream!=null){
				ImageIcon book= new ImageIcon(ImageIO.read(stream));
				goLib.setIcon(book);
			}
			
		}catch (IOException e) {
			e.printStackTrace();
			throw new FileException("File '"+resourcesPath+"book_shelf.png"+"' not found");
		}
		
		try {
			InputStream stream2 = getClass().getClassLoader().getResourceAsStream(resourcesPath+"coins.png");
			if(stream2!=null){
				ImageIcon coin= new ImageIcon(ImageIO.read(stream2));
				goStore.setIcon(coin);
			}
			
		}catch (IOException e) {
			e.printStackTrace();
			throw new FileException("File '"+resourcesPath+"book.png"+"' not found");
		}
			
		
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		
		for (int i = 0; i < 12; i++) {
			layout.row().grid().add(new JLabel(""));
		}
		layout.row().center().add(name);
		layout.row().center().add(title);
		
		
		for (int i = 0; i < 6; i++) {
			layout.row().grid().add(new JLabel(""));
		}
		layout.row().grid().add(goLib).add(goStore);
		for (int i = 0; i < 5; i++) {
			layout.row().grid().add(new JLabel(""));
		}
		
		JLabel footer1 = new JLabel("Designed & developed by Jason Pilbrough");
		footer1.setForeground(new Color(150, 150, 150));
		footer1.setFont (footer1.getFont().deriveFont (10.0f));
		JLabel footer2 = new JLabel("Copyright © 2017 Jason Pilbrough ");
		footer2.setForeground(new Color(150, 150, 150));
		footer2.setFont (footer1.getFont().deriveFont (10.0f));
		JLabel footer3 = new JLabel("All rights reserved");
		footer3.setForeground(new Color(150, 150, 150));
		footer3.setFont (footer1.getFont().deriveFont (10.0f));
		
		layout.row().grid().add(footer1);
		layout.row().grid().add(footer2);
		layout.row().grid().add(footer3);
		removeAll();
        add(parent);   
        revalidate();
        repaint();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		return map;
	}

}
