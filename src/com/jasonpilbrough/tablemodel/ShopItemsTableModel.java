package com.jasonpilbrough.tablemodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.Id;
import com.jasonpilbrough.helper.Money;

public class ShopItemsTableModel implements TableModel {

	private String[] labels = new String[]{"ID","Barcode","Title","Author","Type","Cost Price (R)","Selling Price (R)","Qty"};
	private String[] tableNames = new String[]{"id","barcode","title","author","media_type","cost_price","selling_price","quantity",null};
	private boolean[] editable = new boolean[]{false,true,true,true,true,true,true,true};
	private static final Class[] columnClasses = new Class[]{Id.class,String.class,String.class,String.class,
			String.class,Money.class,Money.class,Integer.class};
	private Object[][] data;
	private int rowCount = 0;
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	
	public ShopItemsTableModel(final Database db, final String filter, final PropertyChangeListener progressListener) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
		
		SwingWorker<Object[][], Object> worker = new TableModelWorker(new SwingWorkerActions() {
			
			@Override
			public Object getValueAt(int limit, int offset) {
				List<List<Object>> data = db.sql("SELECT * FROM shop_items WHERE title LIKE '%?%' OR author LIKE '%?%' OR barcode LIKE '%?%' "
						+ "ORDER BY barcode,id LIMIT ? OFFSET ?")
						.set(filter)
						.set(filter)
						.set(filter)
						.set(limit)
						.set(offset)
						.retrieve2D();
				return data;
			}
			
			@Override
			public int getRowCount() {
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
		});
		
		worker.execute();
		if(progressListener!=null){
			worker.addPropertyChangeListener(progressListener);
		}
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("data")){
					data = (Object[][])evt.getNewValue();
					notifyListeners();
					
				}else if(evt.getPropertyName().equals("row_count")){
					rowCount = (int)evt.getNewValue();
					notifyListeners();
				}
				
			}
		});
	}
	
	@Override
	public int getRowCount() {
		//work around because super class calling this method before constructor can init db
		if(db==null || filter==null)
			return 0;
		return rowCount;
				
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
		return data[rowIndex][columnIndex];	
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		db.sql("UPDATE shop_items SET ? = '?' WHERE id = ? LIMIT 1")
		.set(tableNames[columnIndex])
		.set(aValue)
		.set(getValueAt(rowIndex, 0))
		.update();

		data[rowIndex][columnIndex] = aValue;

	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {}

	public void notifyListeners(){
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
}
