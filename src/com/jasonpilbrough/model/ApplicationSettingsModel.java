package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;

public class ApplicationSettingsModel {
	
	private Database db;
	private AccessManager am;
	private PropertyChangeSupport changefirer;
	
	
	public ApplicationSettingsModel(Database db, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void setAllValues() {
		changefirer.firePropertyChange("dev_access", "", am.allowedAccess("ACDS"));
		changefirer.firePropertyChange("loan_duration", "", getSettingVal("loan_duration"));	
		changefirer.firePropertyChange("loan_extension", "", getSettingVal("loan_extension"));	
		changefirer.firePropertyChange("membership_duration", "", getSettingVal("membership_duration"));	
		changefirer.firePropertyChange("loan_cap", "", getSettingVal("loan_cap"));	
		changefirer.firePropertyChange("membership_fee", "", getSettingVal("membership_fee"));	
		changefirer.firePropertyChange("fine", "", getSettingVal("fine"));	
		changefirer.firePropertyChange("float", "", getSettingVal("float"));
		changefirer.firePropertyChange("password", "", getSettingVal("default_password"));		
		changefirer.firePropertyChange("bug_reports_path", "", getSettingVal("bug_reports_path"));	
		changefirer.firePropertyChange("cash_ups_path", "", getSettingVal("cash_ups_path"));	
		changefirer.firePropertyChange("sales_reports_path", "", getSettingVal("sales_reports_path"));			
	}
	
	public void changeSetting(String setting, Object  val){
		db.sql("UPDATE settings SET value = '?' WHERE name = '?' LIMIT 1")
		.set(val)
		.set(setting)
		.update();
	}
	
	private Object getSettingVal(String setting){
		return db.sql("SELECT value FROM settings WHERE name = '?' LIMIT 1")
				.set(setting)
				.retrieve().get("value");
	}
	
	
}
