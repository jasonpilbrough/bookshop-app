package com.jasonpilbrough.helper;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;

public class SmartJDatePicker extends JDatePickerImpl {

	public SmartJDatePicker(JDatePanelImpl datePanel, AbstractFormatter formatter) {
		super(datePanel, formatter);
	}
	
	public SmartJDatePicker withSomeState(SmartJDatePicker picker){
		this.getJFormattedTextField().setText(picker.getJFormattedTextField().getText());
		this.getModel().setDate(picker.getModel().getYear(), picker.getModel().getMonth(), picker.getModel().getDay());
		return this;
	}

}
