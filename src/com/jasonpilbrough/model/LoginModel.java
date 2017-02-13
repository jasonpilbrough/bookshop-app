package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;

public class LoginModel {
	private Database db;
	private AccessManager am;
	private PropertyChangeSupport changefirer;
	
	private static final int maxTextLength = 50;
	private static final int minTextLength = 1;
	
	
	public LoginModel(Database db, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public boolean login(String username, String password){
		//to test db connection
		db.sql("SELECT id FROM users LIMIT 1 ").retrieve();
		
		Map map = db.sql("SELECT id FROM users WHERE username = '?' AND password = '?' LIMIT 1")
		.set(username)
		.set(password)
		.retrieve();
		
		
		if(map.get("id")!=null){
			am.loginUser(username);
		}
		
		return map.get("id")!=null;
		
	}
	
}
