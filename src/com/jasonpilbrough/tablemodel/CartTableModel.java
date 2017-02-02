package com.jasonpilbrough.tablemodel;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CartTableModel implements TableModel {

	
	private String[] labels = new String[]{"Item","Price per","Qty"};
	private List<String[]> data;
	
	public CartTableModel(List<String[]> data) {
		super();
		this.data = data;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return labels.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return labels[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex)[columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

	@Override
	public void addTableModelListener(TableModelListener l) {}

	@Override
	public void removeTableModelListener(TableModelListener l) {}

}
