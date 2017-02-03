package com.jasonpilbrough.tablemodel;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.AccessException;
import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.LogException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.view.Drawable;

public class AccessControlledTableModel implements TableModel {
	
	private TableModel origin;
	private AccessManager am;
	private String code;
	
	public AccessControlledTableModel(TableModel origin, AccessManager am, String code){
		this.origin = origin;
		this.am = am;
		//TOOD hate hate this
		this.code = code;
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
			if(!am.allowedAccess(code)){
				throw new AccessException();
			}
			
			origin.setValueAt(aValue, rowIndex, columnIndex);
		} catch (AccessException e) {
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