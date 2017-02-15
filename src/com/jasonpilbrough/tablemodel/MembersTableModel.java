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
	
	public MembersTableModel(Database db, String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
		SwingWorker<Object[][], Object> worker = new MySwingWorker(db, filter, labels.length);
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

	@Override
	public void removeTableModelListener(TableModelListener l) {}
	
	

}

class MySwingWorker extends SwingWorker <Object[][], Object>{
	
	private Database db;
	private String filter;
	private int columnCount;
	private int currentRowCount =0;
	
	public MySwingWorker(Database db, String filter, int columnnCount) {
		this.db = db;
		this.filter = filter;
		this.columnCount = columnnCount;
	}

	@Override
	protected Object[][] doInBackground() throws Exception {
		Object[][] ans = new Object[getRowCount()][getColumnCount()];
		for (int i = 0; i < getRowCount(); i++) {
			Object[] temp = new Object[getColumnCount()];
			for (int j = 0; j < getColumnCount(); j++) {
				temp[j] = getValueFromDb(i, j);
			}
			currentRowCount++;
			if(currentRowCount%10==0){
				publish(ans);
			}
			ans[i] = temp;
		}
		
		firePropertyChange("data", null, ans);
		firePropertyChange("row_count", null, getRowCount());
		return ans;
	}
	
	@Override
	protected void process(List<Object> chunks) {
		firePropertyChange("data", null, chunks.get(chunks.size()-1));
		firePropertyChange("row_count", null, currentRowCount);
	}
	
	private Object getValueFromDb(int rowIndex, int columnIndex){
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
	
	private int getRowCount() {
		return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM members WHERE name LIKE '%?%'")
				.set(filter)
				.retrieve().get("num"));
	}
	
	private int getColumnCount() {
		return columnCount;
	}



	
	
}

