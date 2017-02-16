package com.jasonpilbrough.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SmartFile {
	
	private final String directory;
	private final String filename;
	
	public SmartFile(String directory, String filename) {
		this.directory = directory;
		this.filename = filename;
	}
	
	//this method should only be used to read files that are stored inside the .jar
	public String read() throws FileNotFoundException{
		String ans = "";
		try(Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream(directory+filename))) {
			while(sc.hasNext()){
				ans += sc.nextLine()+"\n";
			}
			//TODO remove
		} catch (NullPointerException e) {
			throw new FileNotFoundException("File '"+directory+filename+"' not found");
		} 
		return ans;
	}
	
	//this method should only be used to read files that are stored inside the .jar
		public String readWithFilter(String filter) throws FileNotFoundException{
			String ans = "";
			try(Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream(directory+filename))) {
				while(sc.hasNext()){
					String line = sc.nextLine();
					if(line.length()>0){
						if(line.substring(0, 1).equals(filter)||line.substring(0, 1).equals("#")){
							ans += line+"\n";
						}
					}
					
					
				}
			} catch (NullPointerException e) {
				throw new FileNotFoundException("File '"+directory+filename+"' not found");
			} 
			return ans;
		}

	
	public void write(String text) throws IOException{
		makeDirs();
		try(FileWriter fw = new FileWriter(directory+filename)) {
    		fw.write(text);
    		
    	}
		
	}
	
	public void append(String text) throws IOException{
		makeDirs();
		try(FileWriter fw = new FileWriter(directory+filename,true)) {
    		fw.write(text);
    		
    	}
	}
	
	public void makeDirs(){
		File directoryFile = new File(String.valueOf(directory));
	    if (! directoryFile.exists()){
	    	directoryFile.mkdirs();
	    }
	}
	
	
}
