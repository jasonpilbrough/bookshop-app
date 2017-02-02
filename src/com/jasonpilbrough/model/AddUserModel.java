package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.FailedValidationException;

public class AddUserModel {

	
	private Database db;
	private PropertyChangeSupport changefirer;
	
	private final String[] labels;
	private String selectedLabel;
	private ComboBoxModel<String> comboModel;
	
	private static final int maxTextLength = 50;
	private static final int minTextLength = 1;
	
	
	public AddUserModel(Database db) {
		super();
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		labels = makeLabels();
		selectedLabel = labels[labels.length-1];
		comboModel = makeComboModel();
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void setAllValues(){
		changefirer.firePropertyChange("combo_model", null, comboModel);
	}
	
	public void addUser(String username, String password, String access) 
			throws FailedValidationException{
		
		if(!checkUsernameAvaliable(username)){
			throw new FailedValidationException("Username already taken");
		}
		if(username.length()>maxTextLength){
			throw new FailedValidationException("Username too long");
		}
		if(username.length()<minTextLength){
			throw new FailedValidationException("Username too short");
		}
		
		if(password.length()>maxTextLength){
			throw new FailedValidationException("Password too long");
		}
		if(password.length()<minTextLength){
			throw new FailedValidationException("Password too short");
		}
		
		db.sql("INSERT INTO users(username, password, access_level) "
				+ "VALUES('?', '?', ?)")
					.set(username)
					.set(password)
					.set(access.substring(6, 7))
					.update();
		changefirer.firePropertyChange("close", null, null);
	}
	
	private boolean checkUsernameAvaliable(String username){
		Map map = db.sql("SELECT id FROM users WHERE username = '?' LIMIT 1")
		.set(username)
		.retrieve();
		
		return map.get("id")==null;
	}
	
	public void addUser(String username, String access) throws FailedValidationException{
		
		if(!checkUsernameAvaliable(username)){
			throw new FailedValidationException("Username already taken");
		}
		
		if(username.length()>maxTextLength){
			throw new FailedValidationException("Username too long");
		}
		if(username.length()<minTextLength){
			throw new FailedValidationException("Username too short");
		}
		
		db.sql("INSERT INTO users(username, password, access_level) "
				+ "VALUES('?', (SELECT value FROM settings WHERE name = 'default_password' LIMIT 1), ?)")
					.set(username)
					.set(access.substring(6, 7))
					.update();
		changefirer.firePropertyChange("close", null, null);
	}
	
	private String[] makeLabels(){
		List<Object> list = (ArrayList<Object>)db.sql("SELECT level FROM access_levels ORDER BY level DESC")
		.retrieve().get("array");
		
		List<String> list2 = (ArrayList<String>)db.sql("SELECT title FROM access_levels ORDER BY level DESC")
				.retrieve().get("array");
		
		String[] ans = new String[list.size()];
		
		for (int i = 0; i < ans.length; i++) {
			ans[i] = "Level "+list.get(i)+" ("+list2.get(i)+")";
		}
		
		return ans;
		
	}
	
	private ComboBoxModel<String> makeComboModel(){
		ComboBoxModel<String> model = new ComboBoxModel<String>() {

			@Override
			public int getSize() {
				return labels.length;
			}

			@Override
			public String getElementAt(int index) {
				return labels[index];
			}

			@Override
			public void addListDataListener(ListDataListener l) {}

			@Override
			public void removeListDataListener(ListDataListener l) {}

			@Override
			public void setSelectedItem(Object anItem) {
				selectedLabel = anItem.toString();
				
			}

			@Override
			public Object getSelectedItem() {
				return selectedLabel;
			}
		};
		model.setSelectedItem(selectedLabel);
		return model;
	}
	
}
