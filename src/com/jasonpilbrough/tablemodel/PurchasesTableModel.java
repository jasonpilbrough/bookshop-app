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
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.Id;
import com.jasonpilbrough.helper.Money;

public class PurchasesTableModel implements TableModel {


	private static final String[] labels = new String[]{"ID","Date","Type","Payment","Amount (R)"};
	private static final String[] tableNames = new String[]{"id","date","type","payment","amount"};
	private static final Class[] columnClasses = new Class[]{Id.class,DateInTime.class,String.class, String.class, Money.class};
	private static final boolean[] editable = new boolean[]{false,true,true,true,true};
	private Object[][] data;
	private int rowCount = 0;
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	
	public PurchasesTableModel(final Database db, final String filter, final PropertyChangeListener progressListener) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
		SwingWorker<Object[][], Object> worker = new TableModelWorker(new SwingWorkerActions() {
			
			@Override
			public Object getValueAt(int limit, int offset) {
				List<List<Object>> data = db.sql("SELECT id, date, type, payment, amount FROM purchases "
						+ "WHERE type LIKE '%?%' ORDER BY date DESC,id DESC LIMIT ? OFFSET ?")
						.set(filter)
						.set(limit)
						.set(offset)
						.retrieve2D();
				return data;
			}
			
			@Override
			public int getRowCount() {
				return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM purchases WHERE type LIKE '%?%'")
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
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return editable[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		db.sql("UPDATE purchases SET ? = '?' WHERE id = ? LIMIT 1")
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
