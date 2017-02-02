package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;

public class WelcomeModel {

	private Database db;
	private AccessManager am;
	private PropertyChangeSupport changefirer;
	
	
	public WelcomeModel(Database db, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void setAllValues() {
		changefirer.firePropertyChange("username", null, am.formatUsername(am.getLoggedInUser()));		
		changefirer.firePropertyChange("resources_path", null, getResourcesFilepath());		
	}
	
	private String getResourcesFilepath(){
		return db.sql("SELECT value FROM settings WHERE name = 'resources_path' LIMIT 1")
				.retrieve().get("value").toString();
	}
	
}
