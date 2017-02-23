package com.jasonpilbrough.helper;

public class Money {

	private double amount;
	
	public Money(double amount){
		this.amount = amount;
	}
	
	public Money(Object amount){
		this.amount = Double.valueOf(amount.toString()).doubleValue();
		
	}
	
	public String toStringWithoutCurrency(){
		String[] arr=String.valueOf(amount).split("\\.");
	    int[] intArr=new int[2];
	    intArr[0]=Integer.parseInt(arr[0]);
	    intArr[1]=Integer.parseInt(arr[1]);
	    if(amount>=0){
	    	return String.format("%s.%02d", intArr[0],intArr[1]);
	    }else{
	    	return String.format("(%s.%02d)", Math.abs(intArr[0]),intArr[1]);
	    }
		
	}
	
	public String toString(){
		String[] arr=String.valueOf(amount).split("\\.");
	    int[] intArr=new int[2];
	    intArr[0]=Integer.parseInt(arr[0]);
	    intArr[1]=Integer.parseInt(arr[1]);
	    if(amount>=0){
	    	return String.format("R %6s.%02d", intArr[0],intArr[1]);
	    }else{
	    	return String.format("(R %5s.%02d)", Math.abs(intArr[0]),intArr[1]);
	    }
		
	}
}
