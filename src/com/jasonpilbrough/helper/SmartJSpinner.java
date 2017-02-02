package com.jasonpilbrough.helper;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class SmartJSpinner extends JSpinner {

	public SmartJSpinner(SpinnerModel model){
		super(model);
	}
	
	public SmartJSpinner(){
		super();
	}
	
	public SmartJSpinner withSomeState(SmartJSpinner spinner){
		this.setModel(spinner.getModel());
		return this;
	}
}
