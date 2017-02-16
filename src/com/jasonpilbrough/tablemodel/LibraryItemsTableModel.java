package com.jasonpilbrough.tablemodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;

public class LibraryItemsTableModel implements TableModel{

	private static final String[] labels = new String[]{"ID","Barcode","Title","Author","Type","Hire Fee", "Qty"};
	private static final String[] tableNames = new String[]{"id","barcode","title","author","media_type","hire_price","quantity"};
	private static final boolean[] editable = new boolean[]{false,true,true,true,true,true,true};
	private static final Class[] columnClasses = new Class[]{Integer.class,String.class,String.class,String.class,String.class,Double.class,Integer.class};
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	private Object[][] data;
	private int rowCount = 0;
	SwingWorker<Object[][], Object> worker;
	
	public LibraryItemsTableModel(final Database db, final String filter, final PropertyChangeListener progressListener) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
		
		worker = new TableModelWorker(new SwingWorkerActions() {
			
			@Override
			public Object getValueAt(int limit, int offset) {
				List<List<Object>> data = db.sql("SELECT * FROM library_items WHERE title LIKE '%?%' OR barcode LIKE '%?%' OR"
						+ " author LIKE '%?%' ORDER BY barcode, id LIMIT ? OFFSET ?")
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
	public boolean isCellEditable(int row, int column) {
		return editable[column];
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];		
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
