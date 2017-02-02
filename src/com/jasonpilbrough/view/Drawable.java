package com.jasonpilbrough.view;
import java.beans.PropertyChangeListener;
import java.util.Map;

import com.jasonpilbrough.vcontroller.Controller;

public interface Drawable extends PropertyChangeListener {

	public void initialise(Controller controller);
	public void draw();
	public Map<String,Object> getFields();
}
