package com.jasonpilbrough.helper;

public class AccessException extends Exception {

	public AccessException() {
		this("You do not have permission to complete this opperation");
	}

	public AccessException(String message) {
		super(message);
		
	}
}
