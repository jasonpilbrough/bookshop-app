package com.jasonpilbrough.tablemodel;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.LogException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.view.Drawable;

public class ValidatedTableModel implements TableModel {

	private TableModel origin;
	
	private static final int maxTextLength = 50;
	private static final int minTextLength = 1;
	
	public ValidatedTableModel(TableModel origin){
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
		return origin.isCellEditable(rowIndex, columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return origin.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		try {
			if(getColumnClass(columnIndex)==String.class){
				if(aValue.toString().length()>maxTextLength){
					throw new FailedValidationException(getColumnName(columnIndex)+" too long");
				}
				if(aValue.toString().length()<minTextLength){
					throw new FailedValidationException(getColumnName(columnIndex)+" too short");
				}
			}
			if(getColumnClass(columnIndex)==DateInTime.class){
				new DateInTime(aValue.toString()).validate();
			}
			//TODO phone number validation?
			
			origin.setValueAt(aValue, rowIndex, columnIndex);
		} catch (FailedValidationException e) {
			//TODO dont like this
			ViewHandler vh = new ViewHandler(null,null);
			Drawable d = vh.makeMessageDialog(e.getMessage(), null);
			d.draw();
		}

	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		origin.addTableModelListener(l);

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		origin.removeTableModelListener(l);

	}
	

}
