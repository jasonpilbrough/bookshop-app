package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.FailedValidationException;

public class AddMemberModel {
	
	private Database db;
	private PropertyChangeSupport changefirer;
	

	private static final int maxNameLength = 50;
	private static final int minNameLength = 1;
	private static final int reqPhoneLength = 10;
	
	public AddMemberModel(Database db) {
		super();
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	
	public void addMember(String name, String phone, boolean paid) throws FailedValidationException{
		
		if(name.length()>maxNameLength){
			throw new FailedValidationException("Name too long");
		}
		if(name.length()<minNameLength){
			throw new FailedValidationException("Name too short");
		}
		
		if(phone.length()>reqPhoneLength){
			throw new FailedValidationException("Phone number too long");
		}
		if(phone.length()<reqPhoneLength){
			throw new FailedValidationException("Phone number too short");
		}
		
		for (int i = 0; i < phone.length(); i++) {
			if(phone.charAt(i) < 48 ||phone.charAt(i) > 57){
				throw new FailedValidationException("Phone number wrong format");
			}
		}
		
		db.sql("INSERT INTO members(name, phone_number, join_date, expire_date) "
				+ "VALUES('?', '?', '?','?')")
					.set(name)
					.set(phone)
					.set(new DateInTime().toString())
					.set(paid?new DateInTime().addYears(1).toString(): new DateInTime().toString())
					.update();
		changefirer.firePropertyChange("close", null, null);
	}
	
	public double getMembershipFee(){
		String val = db.sql("SELECT value FROM settings WHERE name='membership_fee' LIMIT 1")
				.retrieve().get("value").toString();
		return Double.parseDouble(val);
	}
	
	/*//method for when membership fee is payed
	public void addMember(String name, String phone, double fee) throws FailedValidationException{
		
		addMember(name, phone);
		
		DateInTime expireDate = new DateInTime().addYears(1);
		db.sql("INSERT INTO incidentals(type, amount, date) "
				+ "VALUES('MEMBERSHIPFEE', ?, '?')")
					.set(fee)
					.set(new DateInTime().toString())
					.update();
		changefirer.firePropertyChange("close", null, null);
	}
	*/
}
