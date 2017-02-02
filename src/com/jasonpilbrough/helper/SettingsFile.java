package com.jasonpilbrough.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SettingsFile {

	private final String settingsFilePath;
	
	public SettingsFile(String settingsFilePath) {
		super();
		this.settingsFilePath = settingsFilePath;
	}

	public Map<String,String> getSettings() throws FileNotFoundException{
		Map<String,String> map = new HashMap<>();
		try(Scanner sc = new Scanner(new File(settingsFilePath))) {
			while(sc.hasNext()){
				String line = sc.nextLine();
				if(line.length()==0){
					
				}else if(line.substring(0, 1).equals("#")){
					map.put(line.substring(1).split("--")[0], line.split("--")[1]);
				}
			}
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File '"+settingsFilePath+"' not found");
		} 
		return map;
	}
}
