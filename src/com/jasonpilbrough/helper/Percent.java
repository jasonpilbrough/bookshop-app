package com.jasonpilbrough.helper;

public class Percent {

	private double val;

	public Percent(double val) {
		super();
		this.val = val;
	}
	
	public String toString(){
		return String.format("%3d %%", (int)Math.round(val*100));
	}
}
