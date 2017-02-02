package com.jasonpilbrough.helper;

import java.io.FileWriter;
import java.io.IOException;

public class SmartFile {
	
	private final String filePath;
	private static final String settingsFilePath = "res/settings.txt";
	
	public SmartFile(String filePath) {
		this.filePath = filePath;
	}
	
	public String read(){
		return "";
	}

	
	public void write(String text) throws IOException{
		try(FileWriter fw = new FileWriter(filePath)) {
    		fw.write(text);
    		
    	}
	}
	
	//TODO not ideal
	public String readSettingsFile(){
		return "";
	}
}
