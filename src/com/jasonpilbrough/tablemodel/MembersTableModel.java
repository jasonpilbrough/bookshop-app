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

public class MembersTableModel implements TableModel{
	
	private String[] labels = new String[]{"ID","Name","Membership Expires","Loans (Overdue)"};
	private String[] tableNames = new String[]{"id","name","expire_date",null};
	private boolean[] editable = new boolean[]{false,true,true,false};
	private static final Class[] columnClasses = new Class[]{Integer.class,String.class,DateInTime.class,String.class};
	private Object[][] data;
	private int rowCount = 0;
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	private double progress = 0;
	
	public MembersTableModel(final Database db, final String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
		SwingWorker<Object[][], Object> worker = new TableModelWorker(new SwingWorkerActions() {
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if(columnIndex<3){
					String property = columnIndex==0?"id":columnIndex==1?"name":"expire_date";
					//TODO make more compact like in LoansModel
					//TODO sql statement may cause problems with sqlite db - OFFSET
					Map<String,Object> map = db.sql("SELECT ? FROM members WHERE name LIKE '%?%' ORDER BY name,id LIMIT 1 OFFSET ?")
							.set(property)
							.set(filter)
							.set(rowIndex)
							.retrieve();
					return map.get(property);
				}else if(columnIndex==3){
					//TODO sql statement may cause problems with sqlite db - OFFSET
					Map<String,Object> map = db.sql("SELECT COUNT(loans.id) AS loans , "
							+ "COUNT(IF(loans.date_due < NOW(),1,NULL)) AS overdue FROM members "
							+ "LEFT JOIN loans ON members.id = loans.member_id WHERE name LIKE '%?%' "
							+ "GROUP BY members.id "
							+ "ORDER BY name,members.id LIMIT 1 OFFSET ?")
							.set(filter)
							.set(rowIndex)
							.retrieve();
					
					
					return map.get("loans")+"  ("+map.get("overdue")+")";
				}
				return null;
			}
			
			@Override
			public int getRowCount() {
				return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM members WHERE name LIKE '%?%'")
						.set(filter)
						.retrieve().get("num"));
			}
			
			@Override
			public int getColumnCount() {
				return labels.length;
			}
		});
		
		worker.execute();
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("data")){
					data = (Object[][])evt.getNewValue();
					notifyListeners();
					
				}else if(evt.getPropertyName().equals("row_count")){
					rowCount = (int)evt.getNewValue();
					notifyListeners();
				} else if(evt.getPropertyName().equals("progress")){
					progress = (double)evt.getNewValue();
					notifyListeners(progress);
				}
				
			}
		});
	}
	
	public MembersTableModel(Database db) {
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
		db.sql("UPDATE members SET ? = '?' WHERE id = ? LIMIT 1")
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
	//using percent is a hack to pass it to the searchmodel
		public void notifyListeners(double percentComplete){
			int percent = (int) Math.round(percentComplete*100);
			for (TableModelListener l : listeners) {
				l.tableChanged(new TableModelEvent(this,0,0,0,percent));
			}
		}
	

}

