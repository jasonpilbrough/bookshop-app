package com.jasonpilbrough.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;

public class PurchasesTableModel implements TableModel {


	private static final String[] labels = new String[]{"ID","Date","Type","Payment","Amount"};
	private static final String[] tableNames = new String[]{"id","date","type","payment","amount"};
	private static final Class[] columnClasses = new Class[]{Integer.class,DateInTime.class,String.class, String.class, Double.class};
	private static final boolean[] editable = new boolean[]{false,true,true,true,true};
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	
	public PurchasesTableModel(Database db, String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
	}
	
	public PurchasesTableModel(Database db) {
		this(db,"");
	}
	
	@Override
	public int getRowCount() {
		//work around because super class calling this method before constructor can init db
		if(db==null || filter==null)
			return 0;
		
		return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM purchases WHERE type LIKE '%?%'")
						.set(filter)
						.retrieve().get("num"));
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
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return editable[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//dont know why the index is ever -1, but sometimes it is
		if(rowIndex<0){
			return "";
		}
		String property = tableNames[columnIndex];
		//TODO sql statement may cause problems with sqlite db - OFFSET
		Map<String,Object> map = db.sql("SELECT ? FROM purchases WHERE type LIKE '%?%' ORDER BY date DESC,id DESC LIMIT 1 OFFSET ?")
				.set(property)
				.set(filter)
				.set(rowIndex)
				.retrieve();
		return map.get(property);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		db.sql("UPDATE purchases SET ? = '?' WHERE id = ? LIMIT 1")
				.set(tableNames[columnIndex])
				.set(aValue)
				.set(getValueAt(rowIndex, 0))
				.update();

		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this, rowIndex));
		}

	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {}

}
