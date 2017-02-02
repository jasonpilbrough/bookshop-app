package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.FailedValidationException;

public class AddIncidentalModel {

	private Database db;
	private PropertyChangeSupport changefirer;
	
	//TODO get this from db
	private static final String[] labels = new String[]{"MEMBERSHIP FEE", "FINE", "HIRE FEE", "DONATION", "OTHER"};
	private String selectedLabel;
	private double defaultPrice;

	private ComboBoxModel<String> comboModel;
	
	
	
	public AddIncidentalModel(Database db, String type, double amount) {
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		selectedLabel = type;
		defaultPrice = amount;
		comboModel = makeComboModel();
	}
	
	public AddIncidentalModel(Database db) {
		this(db,labels[0],0);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void setAllValues(){
		changefirer.firePropertyChange("combo_model", null, comboModel);
		changefirer.firePropertyChange("price", null, defaultPrice);
	}
	
	//TODO dont like this
	public void close(){
		changefirer.firePropertyChange("close", null, null);
	}
	
	
	public void add(String type, double price, String payment) throws  FailedValidationException{
		if(price<0){
			throw new FailedValidationException("Price cannot be negetive");
		}
		db.sql("INSERT INTO incidentals(type, amount, date, payment) "
				+ "VALUES('?', ?, '?', '?')")
					.set(type.toUpperCase())
					.set(price)
					.set(new DateInTime().toString())
					.set(payment.toUpperCase())
					.update();
		
		changefirer.firePropertyChange("close", null, null);
	}
	
	private ComboBoxModel<String> makeComboModel(){
		ComboBoxModel<String> model = new ComboBoxModel<String>() {

			@Override
			public int getSize() {
				return labels.length;
			}

			@Override
			public String getElementAt(int index) {
				return labels[index];
			}

			@Override
			public void addListDataListener(ListDataListener l) {}

			@Override
			public void removeListDataListener(ListDataListener l) {}

			@Override
			public void setSelectedItem(Object anItem) {
				selectedLabel = anItem.toString();
				
			}

			@Override
			public Object getSelectedItem() {
				return selectedLabel;
			}
		};
		model.setSelectedItem(selectedLabel);
		return model;
	}
}
