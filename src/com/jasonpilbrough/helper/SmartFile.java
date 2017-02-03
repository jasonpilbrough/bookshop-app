package com.jasonpilbrough.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SmartFile {
	
	private final String filePath;
	
	public SmartFile(String filePath) {
		this.filePath = filePath;
	}
	
	public String read() throws FileNotFoundException{
		String ans = "";
		try(Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream(filePath))) {
			while(sc.hasNext()){
				ans += sc.nextLine()+"\n";
			}
			//TODO remove
		} catch (NullPointerException e) {
			throw new FileNotFoundException("File '"+filePath+"' not found");
		} 
		return ans;
	}

	
	public void write(String text) throws IOException{
		try(FileWriter fw = new FileWriter(filePath)) {
    		fw.write(text);
    		
    	}
	}
	
	public void append(String text) throws IOException{
		try(FileWriter fw = new FileWriter(filePath,true)) {
    		fw.write(text);
    		
    	}
	}
	
	
}
