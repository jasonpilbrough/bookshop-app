package com.jasonpilbrough.tablemodel;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;

public class CachedTableModel implements TableModel {
	
	private TableModel origin;
	private Object[][] data;
	private int rowCount = -1;
	
	public CachedTableModel(TableModel origin){
		this.origin = origin;
		data = new Object[getRowCount()][getColumnCount()];
		/*for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = getValueAt(i, j);
			}
		}*/
		
	}

	@Override
	public int getRowCount() {
		if(rowCount<0){
			rowCount =  origin.getRowCount();
		}
		
		return rowCount;
	}

	@Override
	public int getColumnCount() {
		return origin.getColumnCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(data[rowIndex][columnIndex]==null){
			data[rowIndex][columnIndex] = origin.getValueAt(rowIndex, columnIndex);
		}
		return data[rowIndex][columnIndex];
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
		return origin.isCellEditable(rowIndex, columnIndex);
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
