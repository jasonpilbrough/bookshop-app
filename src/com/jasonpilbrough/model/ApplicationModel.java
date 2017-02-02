package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ApplicationModel {

	private PropertyChangeSupport changefirer;
	
	
	public ApplicationModel() {
		changefirer = new PropertyChangeSupport(this);
		
	}
	
	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	//Fires a change on all values to update all views
	public void setAllValues(){
		changefirer.firePropertyChange("context", null, "login");
	}
	
	public void setStoreContext(boolean saleTransaction){
		//TODO this is so bad :(
		changefirer.firePropertyChange("context", null, "store"+(saleTransaction?"":"_r"));
	}
	
	public void setLoginContext(){
		changefirer.firePropertyChange("context", null, "login");
	}
	
	public void setLibraryContext(){
		changefirer.firePropertyChange("context", null, "library");
	}
	
	public void setWelcomeContext(){
		changefirer.firePropertyChange("context", null, "welcome");
	}
		
		
}
