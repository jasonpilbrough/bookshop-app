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

public class LoansTableModel implements TableModel {
	
	private static final String[] labels = new String[]{"ID","Member Name","Item Title","Date Issued","Date Due"};
	private static final String[] tableNames = new String[]{"loans.id","name","title","date_issued","date_due"};
	private static final boolean[] editable = new boolean[]{false,false,false,true,true};
	private static final Class[] columnNames = new Class[]{Integer.class,String.class,String.class,DateInTime.class,DateInTime.class};
	private Object[][] data;
	private int rowCount = 0;
	
	private String filter;
	private Database db;
	private int memberId = -1;
	private List<TableModelListener> listeners;
	
	public LoansTableModel(final Database db, final String filter, final PropertyChangeListener progressListener) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
		SwingWorker<Object[][], Object> worker = new TableModelWorker(new SwingWorkerActions() {
			
			@Override
			public Object getValueAt(int limit, int offset) {
				
				List<List<Object>> data = db.sql("SELECT loans.id, name, title, date_issued, date_due FROM loans "
						+ "INNER JOIN members ON loans.member_id = members.id "
						+ "INNER JOIN library_items ON loans.library_item_id = library_items.id "
						+ "WHERE (name LIKE '%?%' OR title LIKE '%?%') AND IF(?>0, member_id = ?, member_id LIKE '%%') "
						+ "ORDER BY name, loans.id LIMIT ? OFFSET ?")
						.set(filter)
						.set(filter)
						.set(memberId)
						.set(memberId)
						.set(limit)
						.set(offset)
						.retrieve2D();
				return data;
			}
			
			@Override
			public int getRowCount() {
				return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM loans "
						+ "INNER JOIN members ON loans.member_id = members.id "
						+ "INNER JOIN library_items ON loans.library_item_id = library_items.id "
						+ "WHERE (name LIKE '%?%' OR title LIKE '%?%') AND IF(?<0,  member_id LIKE '%%', member_id = ?) ")
						.set(filter)
						.set(filter)
						.set(memberId)
						.set(memberId)
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
	
	public LoansTableModel(Database db, int memberId) {
		this(db,"", null);
		this.memberId = memberId;
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
		return columnNames[columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		db.sql("UPDATE loans SET ? = '?' WHERE id = ? LIMIT 1")
		.set(tableNames[columnIndex])
		.set(aValue)
		.set(getValueAt(rowIndex, 0))
		.update();
		
		notifyListeners(rowIndex);
		
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {}
	
	public void notifyListeners(int rowIndex){
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this, rowIndex));
		}
	}
	public void notifyListeners(){
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
}
