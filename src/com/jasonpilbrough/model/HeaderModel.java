package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HeaderModel {
	
	/* Models header in main screen of application.
	 * Any changes to model (ie. calling set methods) cause the PropertyChangeSupport to fire event
	 * containing details of the change. Any view that has been added as a listener, using the 'addListener'
	 * method with be notified of this change. This model also stores the state of the view, (eg. if it is
	 * editable or not)
	 *
	 */
	
	private PropertyChangeSupport changefirer;
	

	public HeaderModel() {
		super();
		changefirer = new PropertyChangeSupport(this);
	}
	
	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	
	public void setAsLibContext(){
		changefirer.firePropertyChange("lib_context", null, null);
	}
	
	public void setAsStoreContext(){
		changefirer.firePropertyChange("store_context", null, null);
	}
	
	

}
