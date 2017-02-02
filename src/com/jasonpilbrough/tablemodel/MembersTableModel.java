package com.jasonpilbrough.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;
	
	public MembersTableModel(Database db, String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
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
		
		return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM members WHERE name LIKE '%?%'")
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

