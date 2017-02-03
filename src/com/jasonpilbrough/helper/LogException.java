package com.jasonpilbrough.helper;

public class LogException extends RuntimeException {

	public LogException(String message) {
		super(message);	
	}
	
	public LogException(Throwable throwable) {
		super(throwable);
		
	}
	
	public LogException(String message, Throwable throwable) {
		super(message, throwable);
		
	}
	
}
