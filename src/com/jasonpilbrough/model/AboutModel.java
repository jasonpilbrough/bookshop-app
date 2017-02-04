package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.SmartFile;

public class AboutModel {
	private Database db;
	private PropertyChangeSupport changefirer;
	
	
	
	public AboutModel(Database db) {
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
	
	private String getText() throws FileNotFoundException{
		
		String directory = db.sql("SELECT value FROM settings WHERE name = 'resources_path' LIMIT 1")
				.retrieve().get("value").toString();
		
		
		String filename = "about.txt";
		SmartFile file = new SmartFile(directory, filename);
		String ans = file.read();

		changefirer.firePropertyChange("title", "", ans.split("\n\n")[0].replaceAll("\n", "---"));	
		changefirer.firePropertyChange("version", "", ans.split("\n\n")[1].replaceAll("\n", "---"));	
		changefirer.firePropertyChange("copyright", "", ans.split("\n\n")[2].replaceAll("\n", "---"));	
		changefirer.firePropertyChange("developer", "", ans.split("\n\n")[3].replaceAll("\n", "---"));	
		changefirer.firePropertyChange("disclaimer", "", ans.split("\n\n")[4].replaceAll("\n", "---"));	
		
		return ans;
	}
	
}
