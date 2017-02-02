package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.tablemodel.CachedTableModel;
import com.jasonpilbrough.tablemodel.MembersTableModel;
import com.jasonpilbrough.tablemodel.NonEditableTableModel;

/* Models all members in the application
 * Any changes to model (ie. calling set methods) cause the PropertyChangeSupport to fire event
 * containing details of the change. Any view that has been added as a listener, using the 'addListener'
 * method with be notified of this change. This model also stores the state of the view, (eg. if it is
 * editable or not)
 */

public class MembersModel implements TableModelListener{

	private Database db;
	private PropertyChangeSupport changefirer;
	private TableModel tableModel;
	
	
	
	public MembersModel(Database db) {
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	//Fires a change on all values to update all views
	public void setAllValues(){
		tableModel = new NonEditableTableModel(new CachedTableModel(new MembersTableModel(db)));
		tableModel.addTableModelListener(this);
		changefirer.firePropertyChange("table_model", null, tableModel);
	}
	
	public void setFilter(String text){
		tableModel = new CachedTableModel(new MembersTableModel(db,text));
		tableModel.addTableModelListener(this);
		changefirer.firePropertyChange("table_model", null, tableModel);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		setAllValues();
		
	}


}
