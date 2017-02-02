package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;

public class UserSettingsModel {

	private Database db;
	private AccessManager am;
	private PropertyChangeSupport changefirer;
	
	
	public UserSettingsModel(Database db, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void setAllValues() {
		changefirer.firePropertyChange("username", null, getUsername());	
		changefirer.firePropertyChange("password", null, getPassword());		
	}
	
	public void changeUserDetails(String username, String  password){
		db.sql("UPDATE users SET username = '?', password = '?' WHERE username = '?' LIMIT 1")
		.set(username)
		.set(password)
		.set(am.getLoggedInUser())
		.update();
		am.loginUser(username);
	}
	
	public boolean checkPassword(String password){
		String val = db.sql("SELECT password FROM users WHERE username = '?' LIMIT 1")
		.set(am.getLoggedInUser())
		.retrieve().get("password").toString();
		
		return password.equals(val);
	}
	
	private String getUsername(){

		return am.getLoggedInUser();
	}
	
	private String getPassword(){
		return db.sql("SELECT password FROM users WHERE username = '?' LIMIT 1")
				.set(getUsername())
				.retrieve().get("password").toString();
	}
	
}
