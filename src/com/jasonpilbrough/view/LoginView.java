package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.BevelBorder;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class LoginView extends JPanel implements Drawable {

	private SmartJButton login;
	private SmartJTextField username;
	private JPasswordField password;

	private JLabel name,title;
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		

	}

	@Override
	public void initialise(final Controller controller) {
		
		setVisible(true);
	       setBounds(0, 0, 600, 600);
			
	        username = new SmartJTextField().withKeyListener(controller);
	        password = new JPasswordField();
	        password.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						controller.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "enter pressed"));
					}
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			login = new SmartJButton("Login","login").withRegisteredController(controller);
			title = new JLabel("Highway Christian Community");
	        name = new JLabel("Login");

	}

	@Override
	public void draw() {
		username = new SmartJTextField().withSomeState(username);
		KeyListener[] listeners = password.getKeyListeners();
		password = new JPasswordField();
		for(KeyListener kl:listeners){
        	password.addKeyListener(kl);
        }

		name = new JLabel(name.getText());
		name.setFont (name.getFont().deriveFont (36.0f));

		title = new JLabel(title.getText());
		title.setFont (title.getFont().deriveFont (14.0f));
		
		
		
		JPanel parent = new JPanel();
		//parent.setBorder(new BevelBorder(1));
		parent.setPreferredSize(new Dimension(300, 400));
		
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		
		for (int i = 0; i < 20; i++) {
			layout.row().grid().add(new JLabel(""));
		}
		layout.row().center().add(name);
		layout.row().center().add(title);
		
		for (int i = 0; i < 6; i++) {
			layout.row().grid().add(new JLabel(""));
		}
		layout.row().grid(new JLabel("Username:")).add(username);
		layout.row().grid(new JLabel("Password:")).add(password);
		layout.row().grid().empty().add(login);
		for (int i = 0; i < 5; i++) {
			layout.row().grid().add(new JLabel(""));
		}
		
		JLabel footer1 = new JLabel("Designed & developed by Jason Pilbrough");
		footer1.setForeground(new Color(150, 150, 150));
		footer1.setFont (footer1.getFont().deriveFont (10.0f));
		JLabel footer2 = new JLabel("Copyright Jason Pilbrough 2017");
		footer2.setForeground(new Color(150, 150, 150));
		footer2.setFont (footer1.getFont().deriveFont (10.0f));
		JLabel footer3 = new JLabel("All rights reserved");
		footer3.setForeground(new Color(150, 150, 150));
		footer3.setFont (footer1.getFont().deriveFont (10.0f));
		
		//layout.row().center().add(footer1);
		//layout.row().center().add(footer2);
		//layout.row().center().add(footer3);
		removeAll();
        add(parent);   
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

}
