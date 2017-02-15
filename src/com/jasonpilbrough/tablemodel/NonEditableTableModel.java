package com.jasonpilbrough.tablemodel;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class NonEditableTableModel implements TableModel {

	private TableModel origin;
	
	
	public NonEditableTableModel(TableModel origin){
		this.origin = origin;
		
	}
	
	@Override
	public int getRowCount() {
		return origin.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return origin.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return origin.getColumnName(columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return origin.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return origin.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		origin.setValueAt(aValue, rowIndex, columnIndex);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		origin.addTableModelListener(l);

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		origin.removeTableModelListener(l);

	}
	@Override
	public String toString() {
		return origin.toString();
	}

}
