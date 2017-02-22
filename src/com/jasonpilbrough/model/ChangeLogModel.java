package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.SmartFile;

public class ChangeLogModel {
	private Database db;
	private PropertyChangeSupport changefirer;
	
	
	
	public ChangeLogModel(Database db) {
		super();
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void setAllValues() throws FileNotFoundException{
		getText();
	}
	
	private void getText() throws FileNotFoundException{
		String directory = db.sql("SELECT value FROM settings WHERE name = 'resources_path' LIMIT 1")
				.retrieve().get("value").toString();
		
		
		String filename = "change_log.txt";
		SmartFile file = new SmartFile(directory, filename);
		String text = file.readWithFilter(":");
		//System.out.println(text);
		String[] versions = text.split("#");


		changefirer.firePropertyChange("versions", "", versions);	
		
	}
	
}
