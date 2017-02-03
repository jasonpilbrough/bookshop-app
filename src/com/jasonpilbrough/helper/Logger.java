package com.jasonpilbrough.helper;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Set;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.model.ConsoleModel;

public class Logger {

	private AccessManager am;
	private static ConsoleModel cm;
	private final SettingsFile settings;
	
	
	
	public Logger(SettingsFile settings){
		this.settings = settings;
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		
		
		for (Thread thread : threadArray) {
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					try {
						log(e);
						if(cm!=null){
							cm.appendThrowable(e);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						//TODO dont like
						JOptionPane.showMessageDialog(null, "Error writing to app logs: "+e1.toString());
					} 
					
					e.printStackTrace();
					
				}
			});
			
		}
	}
	
	public static void setConsoleModel(ConsoleModel model){
		cm = model;
	}
	
	public void setAccessManager(AccessManager am){
		this.am = am;
	}
	
	public void log(Throwable t) throws IOException{
		
		
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss aa");
		String user = "";
		if(am==null){
			user = "Access Manager not initialised";
		}else{
			user = am.formatUsername(am.getLoggedInUser());
		}
		SmartFile file = new SmartFile(settings.getSettings().get("path")+fmt1.print(new DateTime())+"-log.txt");
		String text = "";
	
		text +="\nLOG ENTRY";
		text +="\n-----------------------------------------------------------------------\n";
		text +=String.format("\n%-20s %s", "TIMESTAMP" ,fmt2.print(new DateTime()));
		text +=String.format("\n%-20s %s","USER",user);
		text +="\n\n-----------------------------------------------------------------------\n\n";
		text +="STACK TRACE\n\n";
		text +=format(t.toString()+"\n");
		for (int i = 0; i < t.getStackTrace().length; i++) {
			text +=format(t.getStackTrace()[i]+"\n");
		}
		
		text +="\n\n=======================================================================\n";
		file.append(text);
	       
	    	
	    
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
