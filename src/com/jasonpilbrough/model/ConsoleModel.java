package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.helper.Database;

public class ConsoleModel {
	
	
	private Database db;
	private PropertyChangeSupport changefirer;
	
	private String text;
	private int count = 0;

	
	public ConsoleModel(Database db) {
		super();
		this.db = db;
		text = "";
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	
	
	public void appendThrowable(Throwable t){
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM");
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("HH:mm:ss ");
		String oldText = text;
		text ="\n"+"ERROR: "+String.format("%04d - ", ++count)+fmt2.print(new DateTime());;
		text +="\n";
		text +=format(t.toString()+"\n");
		for (int i = 0; i < t.getStackTrace().length; i++) {
			text +=format(t.getStackTrace()[i]+"\n");
		}
		
		text +="\n=======================================================================\n";
		text += oldText;
		changefirer.firePropertyChange("text", "", text);
	
	}
	
	private String format(String val){
		int threshold = 71;
		String ans = "";
		int tempLen = 0;
		
		for (int i = 0; i < val.length(); i++) {
			if(tempLen<threshold){
				tempLen++;
				ans+= val.charAt(i);
			}else{
				tempLen = 0;
				ans+= "\n"+val.charAt(i);
				tempLen++;
			}
		}
		return ans;
	}
	
}
