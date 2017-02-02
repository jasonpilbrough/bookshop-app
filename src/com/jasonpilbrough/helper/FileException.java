package com.jasonpilbrough.helper;

public class FileException extends RuntimeException {

	public FileException(String message) {
		super(message);	
	}
	
	public FileException(Throwable throwable) {
		super(throwable);
		
	}
	
	public FileException(String message, Throwable throwable) {
		super(message, throwable);
		
	}
	
}
