package com.jasonpilbrough.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;

public class LibraryItemsTableModel implements TableModel {

	private static final String[] labels = new String[]{"ID","Barcode","Title","Author","Type","Hire Fee", "Qty"};
	private static final String[] tableNames = new String[]{"id","barcode","title","author","media_type","hire_price","quantity"};
	private static final boolean[] editable = new boolean[]{false,true,true,true,true,true,true};
	private static final Class[] columnClasses = new Class[]{Integer.class,String.class,String.class,String.class,String.class,Double.class,Integer.class};
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	
	public LibraryItemsTableModel(Database db, String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
	}
	
	public LibraryItemsTableModel(Database db) {
		this(db,"");
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return editable[column];
	}

	@Override
	public int getRowCount() {
		//work around because super class calling this method before constructor can init db
		if(db==null || filter==null)
			return 0;
		return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM library_items WHERE title LIKE '%?%' OR barcode LIKE '%?%' OR"
						+ " author LIKE '%?%'")
						.set(filter)
						.set(filter)
						.set(filter)
						.retrieve().get("num"));
	}



	@Override
	public int getColumnCount() {
		return labels.length;
	}



	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//dont know why the index is ever -1, but sometimes it is
		if(rowIndex<0){
			return "";
		}
		String property = tableNames[columnIndex];
		//TODO sql statement may cause problems with sqlite db - OFFSET
		Map<String,Object> map = db.sql("SELECT ? FROM library_items WHERE title LIKE '%?%' OR barcode LIKE '%?%' OR"
				+ " author LIKE '%?%' ORDER BY barcode, id LIMIT 1 OFFSET ?")
				.set(property)
				.set(filter)
				.set(filter)
				.set(filter)
				.set(rowIndex)
				.retrieve();
		return map.get(property);
		
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
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		db.sql("UPDATE library_items SET ? = '?' WHERE id = ? LIMIT 1")
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
