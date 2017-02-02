package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFileChooser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.FailedValidationException;

public class AddBugReportModel {

	private Database db;
	private AccessManager am;
	private PropertyChangeSupport changefirer;
	
	
	
	public AddBugReportModel(Database db, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public boolean save(String message) throws IOException, FailedValidationException{
		
		String filepath = db.sql("SELECT value FROM settings WHERE name = 'bug_reports_path' LIMIT 1")
				.retrieve().get("value").toString();
		
		if(message.length()==0){
			throw new FailedValidationException("Please include a message in your report");
		}
		
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss aa");
		String user = am.formatUsername(am.getLoggedInUser());
		
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(filepath));
	    chooser.setSelectedFile(new File("bugreport__"+fmt1.print(new DateTime())));
	    int retrival = chooser.showSaveDialog(null);
	    
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	    	try(FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt")) {
	    		fw.write("\nBUG REPORT");
	    		fw.write("\n-----------------------------------------------------------");
	    		fw.write("\n");
	    		fw.write(String.format("\n%-20s %s", "TIMESTAMP" ,fmt2.print(new DateTime())));
	    		fw.write(String.format("\n%-20s %s","REPORTED BY",user));
	    		fw.write(String.format("\n%-20s %s","STATUS","001"));
	    		fw.write("\n");
	    		fw.write("\n-----------------------------------------------------------\n");
	    		fw.write("\n");
	    		fw.write("MESSAGE");
	    		fw.write("\n");
	    		fw.write("\n");
	    		fw.write(formatString(message));
	    		//fw.write(String.format("\n%-20s %s","EXPLANATION ",formatString(explaination)));
	    		fw.write("\n");
	    		fw.write("\n-----------------------------------------------------------");
	    		changefirer.firePropertyChange("close", null, null);
	    		return true;
	    	}
	       
	    }
		
		return false;
	}
	
	
	private String formatString(String exp){
		int threshold = 45;
		
		String[] lines = exp.split("\n");
		String finalAns = "";
		for (int k = 0; k < lines.length; k++) {
			StringBuilder sb = new StringBuilder(lines[k]);

			int i = 0;
			while ((i = sb.indexOf(" ", i + threshold)) != -1) {
			    sb.replace(i, i + 1, "\n");
			}
			
			String[] parts = sb.toString().split("\n");
			for (int j = (k==0?1:0); j < parts.length; j++) {
				parts[j] = parts[j];
			}
			String ans = "";
			for (int j = 0; j < parts.length; j++) {
				ans+=(j==0?"":"\n")+parts[j];
			}
			finalAns+=(k==0?"":"\n")+ans;
		}

			
			
		
		return finalAns;
	}
	
}


