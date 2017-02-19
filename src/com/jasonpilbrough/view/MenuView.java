package com.jasonpilbrough.view;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.jasonpilbrough.helper.SmartJMenuItem;
import com.jasonpilbrough.vcontroller.Controller;

public class MenuView extends JMenuBar implements Drawable {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		

	}

	@Override
	public void initialise(Controller controller) {
		//Where the GUI is created:
    	JMenu fileMenu,searchMenu,addMenu,toolsMenu, settingsMenu, betaMenu, gotoMenu;


    	//Build the first menu.
    	fileMenu = new JMenu("File");
    	fileMenu.add(new SmartJMenuItem("About","about").withRegisteredController(controller));
    	fileMenu.add(new SmartJMenuItem("Change Log","change log").withRegisteredController(controller));
    	fileMenu.add(new SmartJMenuItem("Auto Update...","update").withRegisteredController(controller));
    	fileMenu.addSeparator();
    	fileMenu.add(new SmartJMenuItem("Report a bug","report bug").withRegisteredController(controller));
    	fileMenu.add(new SmartJMenuItem("Logout","logout").withRegisteredController(controller));
    	fileMenu.add(new SmartJMenuItem("Exit Application","exit").withRegisteredController(controller));
    	this.add(fileMenu);
    	

    	toolsMenu = new JMenu("Tools");
    	toolsMenu.add(new JMenuItem("Analytics"));
    	toolsMenu.add(new SmartJMenuItem("Cash Up","cash up").withRegisteredController(controller));
    	toolsMenu.add(new SmartJMenuItem("Sales Report","sales report").withRegisteredController(controller));
    	toolsMenu.add("Stock Take");
    	toolsMenu.add(new SmartJMenuItem("User Management","manage users").withRegisteredController(controller));
    	toolsMenu.add(new SmartJMenuItem("Console","stack trace").withRegisteredController(controller));
    	this.add(toolsMenu);
    	
    	gotoMenu = new JMenu("Go to");
    	gotoMenu.add(new SmartJMenuItem("Library","go to library").withRegisteredController(controller));
    	gotoMenu.add(new SmartJMenuItem("Store","go to store").withRegisteredController(controller));
    	this.add(gotoMenu);

    	searchMenu = new JMenu("Search");
    	searchMenu.add(new SmartJMenuItem("Members","search members").withRegisteredController(controller));
    	searchMenu.add(new SmartJMenuItem("Library Items","search library items").withRegisteredController(controller));
    	searchMenu.add(new SmartJMenuItem("Loans","search loans").withRegisteredController(controller));
    	searchMenu.addSeparator();
    	searchMenu.add(new SmartJMenuItem("Shop Items","search shop items").withRegisteredController(controller));
    	searchMenu.add(new SmartJMenuItem("Sales","search sales").withRegisteredController(controller));
    	searchMenu.addSeparator();
    	searchMenu.add(new SmartJMenuItem("Other Income","search incidentals").withRegisteredController(controller));
    	searchMenu.add(new SmartJMenuItem("Purchases","search purchases").withRegisteredController(controller));
    	this.add(searchMenu);
    	
    	addMenu = new JMenu("Add");
    	addMenu.add(new SmartJMenuItem("Member","add member").withRegisteredController(controller));
    	addMenu.add(new SmartJMenuItem("Library Item","add library item").withRegisteredController(controller));
    	addMenu.add(new SmartJMenuItem("Loan","add loan").withRegisteredController(controller));
    	addMenu.addSeparator();
    	addMenu.add(new SmartJMenuItem("Shop Item","add shop item").withRegisteredController(controller));
    	addMenu.add(new SmartJMenuItem("Sale","add sale").withRegisteredController(controller));
    	addMenu.add(new SmartJMenuItem("Refund","add refund").withRegisteredController(controller));
    	addMenu.addSeparator();
    	addMenu.add(new SmartJMenuItem("Other Income","add incidental").withRegisteredController(controller));
    	addMenu.add(new SmartJMenuItem("Purchase","add purchase").withRegisteredController(controller));
    	this.add(addMenu);
    	
    	settingsMenu = new JMenu("Settings");
    	settingsMenu.add(new SmartJMenuItem("Application Settings","application settings").withRegisteredController(controller));
    	settingsMenu.add(new SmartJMenuItem("User Settings","user settings").withRegisteredController(controller));
    	this.add(settingsMenu);
    	
    	

	}

	@Override
	public void draw() { }

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		return map;
	}

}
