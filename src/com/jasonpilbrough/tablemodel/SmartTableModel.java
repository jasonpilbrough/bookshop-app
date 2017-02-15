package com.jasonpilbrough.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public abstract class SmartTableModel implements TableModel {

	protected List<TableModelListener> listeners;
	protected Object[][] data;
	protected int rowCount = 0;
	protected double progress = 0;
	
	public SmartTableModel() {
		listeners = new ArrayList<>();
	}
	
	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public abstract int getColumnCount();

	@Override
	public abstract String getColumnName(int columnIndex);

	@Override
	public abstract Class<?> getColumnClass(int columnIndex);

	@Override
	public abstract boolean isCellEditable(int rowIndex, int columnIndex);

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	@Override
	public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex) ;

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) { }
	
	public void notifyListeners(int rowIndex){
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this, rowIndex));
		}
	}
	public void notifyListeners(){
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
	
	//using percent is a hack to pass it to the searchmodel
	public void notifyListeners(double percentComplete){
		int percent = (int) Math.round(percentComplete*100);
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this,0,0,0,percent));
		}
	}

}
