package com.jasonpilbrough.helper;

import java.util.Map;

public class AccessManager {

	private String loggedInUser;
	private Database db;
	
	public AccessManager(Database db) {
		this.db = db;
	}
	public String getLoggedInUser(){
		return loggedInUser;
	}
	
	public void loginUser(String username){
		loggedInUser = username;
	}
	
	public void logoutUser(){
		loggedInUser = null;
	}
	
	public boolean allowedAccess(String code){
		if(loggedInUser==null){
			throw new RuntimeException("User must be logged in");
		}
		Map map1 = db.sql("SELECT access_level FROM users WHERE username = '?' LIMIT 1")
				.set(loggedInUser)
				.retrieve();
		
		if(map1.get("access_level")==null){
			throw new RuntimeException("User must be logged in");
		}
		
		Map map2 = db.sql("SELECT access_level FROM privlages WHERE code = '?' LIMIT 1")
			.set(code)
			.retrieve();
		
		if(map2.get("access_level")==null){
			throw new RuntimeException("Access code doesnt not exist");
		}
		return (int)(long)map1.get("access_level") >= (int)(long)map2.get("access_level");
		
	}
	
	public String formatUsername(String username){
		if(username==null){
			return "No logged in user";
		}
		if(username.length()==0){
			return username;
		} else if(username.length()==1){
			return username.toUpperCase();
		} 
		return username.substring(0,1).toUpperCase()+username.substring(1).toLowerCase();
	}
}
