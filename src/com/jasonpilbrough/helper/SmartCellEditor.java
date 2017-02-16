package com.jasonpilbrough.helper;

import java.awt.Color;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class SmartCellEditor extends DefaultCellEditor{
	private static final Border red = new LineBorder(Color.red);
    private static final Border black = new LineBorder(Color.black);
    private static final int maxTextLength = 50;
	private static final int minTextLength = 1;
	private static final long serialVersionUID = 1L;
	
	private Class thisClass;
	private JTextField text;
	
	public SmartCellEditor(JTextField textField, Class thisClass) {
		super(textField);
		this.text = textField;
		this.thisClass = thisClass;
	}
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return super.isCellEditable(anEvent);
	}
	@Override
	public boolean stopCellEditing() {
		if(this.thisClass == Money.class){
			try {
				Money money = new Money(getCellEditorValue());
			} catch (ClassCastException | NumberFormatException e) {
				 text.setBorder(red);
		         return false;
			}
		} else if(this.thisClass == Integer.class){
			try {
				Integer val = new Integer(getCellEditorValue().toString());
			} catch (ClassCastException | NumberFormatException e) {
				 text.setBorder(red);
		         return false;
			}
		} else if(this.thisClass == DateInTime.class){
			try {
				DateInTime date = new DateInTime(getCellEditorValue().toString());
				date.validate();
			} catch (FailedValidationException e) {
				 text.setBorder(red);
		         return false;
			}
		} else if(this.thisClass == String.class){
			try {
				if(getCellEditorValue().toString().length()>maxTextLength){
					throw new FailedValidationException();
				}
				if(getCellEditorValue().toString().length()<minTextLength){
					throw new FailedValidationException();
				}
			} catch (FailedValidationException e) {
				 text.setBorder(red);
		         return false;
			}
		}
		return super.stopCellEditing();
	}
}
