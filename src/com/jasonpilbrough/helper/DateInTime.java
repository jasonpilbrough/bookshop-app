package com.jasonpilbrough.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Jason
 */
public class DateInTime {
    
    private String date;

    public DateInTime(String date) {
        this.date = date;
    }
    
    //creates date object using current time
    public DateInTime() {
        this(new DateTime().now().toString("yyyy-MM-dd"));
    }
    
    public DateInTime addWeeks(int numWeeks){
    	DateTime d = new DateTime(date);
    	return new DateInTime(d.plusWeeks(numWeeks).toString("yyyy-MM-dd"));
    }
    
    public DateInTime addYears(int numYears){
    	DateTime d = new DateTime(date);
    	return new DateInTime(d.plusYears(numYears).toString("yyyy-MM-dd"));
    }
    
    public int diffInWeeks(DateInTime date2){
    	DateTime d = new DateTime(date);
    	return Weeks.weeksBetween(d, new DateTime(date2.toString())).getWeeks();
    }
    
    public int compareTo(DateInTime date2){
    	DateTime d1 = new DateTime(date);
    	DateTime d2 = new DateTime(date2.toString());
    	
    	return d1.compareTo(d2);
    }

    @Override
    public String toString() {
        return date;
    }
    
    public void validate() throws FailedValidationException{
    	try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(date);
            for (int i = 0; i < date.length(); i++) {
            	if(date.charAt(i) < 45 || date.charAt(i) > 57 ){
					throw new IllegalStateException();
				}
			}
        } catch (ParseException e) {
           throw new FailedValidationException("Date in incorrect format, expected YYYY-MM-DD, or values out of range");
        } catch (IllegalStateException e) {
            throw new FailedValidationException("Date cannot contain letters, expected format YYYY-MM-DD");
         }
    }
    
    
    
}
