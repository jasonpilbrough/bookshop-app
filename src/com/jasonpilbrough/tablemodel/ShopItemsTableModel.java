package com.jasonpilbrough.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;

public class ShopItemsTableModel implements TableModel {

	private String[] labels = new String[]{"ID","Barcode","Title","Author","Type","Cost Price","Selling Price","Qty"};
	private String[] tableNames = new String[]{"id","barcode","title","author","media_type","cost_price","selling_price","quantity",null};
	private boolean[] editable = new boolean[]{false,true,true,true,true,true,true,true};
	private static final Class[] columnClasses = new Class[]{Integer.class,String.class,String.class,String.class,
			String.class,Double.class,Double.class,Integer.class};
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	
	public ShopItemsTableModel(Database db, String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
	}
	
	public ShopItemsTableModel(Database db) {
		this(db,"");
	}
	
	
	@Override
	public int getRowCount() {
		//work around because super class calling this method before constructor can init db
				if(db==null || filter==null)
					return 0;
				
				return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM shop_items WHERE title LIKE '%?%' OR author LIKE '%?%' "
								+ "OR barcode LIKE '%?%'")
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
	public String getColumnName(int columnIndex) {
		return labels[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return editable[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//dont know why the index is ever -1, but sometimes it is
		if(rowIndex<0){
			return "";
		}
		String property = tableNames[columnIndex];
		//TODO sql statement may cause problems with sqlite db - OFFSET
		Map<String,Object> map = db.sql("SELECT ? FROM shop_items WHERE title LIKE '%?%' OR author LIKE '%?%' OR barcode LIKE '%?%' "
				+ "ORDER BY barcode,id LIMIT 1 OFFSET ?")
				.set(property)
				.set(filter)
				.set(filter)
				.set(filter)
				.set(rowIndex)
				.retrieve();
		return map.get(property);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		db.sql("UPDATE shop_items SET ? = '?' WHERE id = ? LIMIT 1")
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
