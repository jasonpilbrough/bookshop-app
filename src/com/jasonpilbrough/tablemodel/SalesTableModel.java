package com.jasonpilbrough.tablemodel;

import java.awt.event.ActionEvent;
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
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.vcontroller.Controller;
import com.jasonpilbrough.view.Drawable;

public class SalesTableModel implements TableModel {
	
	private static final String[] labels = new String[]{"ID","Sale Date","Item Title", "Payment","Price per unit","Quantity"};
	private static final String[] tableNames = new String[]{"sales.id","sale_date","title", "payment","price_per_unit_sold","quantity_sold"};
	private static final Class[] columnClasses = new Class[]{Integer.class,DateInTime.class,String.class,String.class,Double.class,Integer.class};
	private static final boolean[] editable = new boolean[]{false,true,false,true,true,true};
	private Object[][] data;
	private int rowCount = 0;
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;

	public SalesTableModel(final Database db, final String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
		SwingWorker<Object[][], Object> worker = new TableModelWorker(new SwingWorkerActions() {
			
			@Override
			public Object getValueAt(int limit, int offset) {
				List<List<Object>> data = db.sql("SELECT sales.id,sale_date, title,payment, price_per_unit_sold, quantity_sold "
						+ "FROM sales INNER JOIN shop_items ON sales.shop_item_id = shop_items.id "
						+ "WHERE title LIKE '%?%' ORDER BY sale_date DESC,id DESC LIMIT ? OFFSET ?")
						.set(filter)
						.set(limit)
						.set(offset)
						.retrieve2D();
				return data;
			}
			
			@Override
			public int getRowCount() {
				return (int)(long)(db.sql("SELECT COUNT(*) AS num FROM sales "
						+ "INNER JOIN shop_items ON sales.shop_item_id = shop_items.id "
						+ "WHERE title LIKE '%?%'")
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
				}
				
			}
		});
	}
	
	public SalesTableModel(Database db) {
		this(db,"");
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
		//return String.class;
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
		return data[rowIndex][columnIndex];	
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		if(columnIndex==3){
			System.out.println((double)getValueAt(rowIndex, columnIndex)+" : "+(double)aValue);
			if((double)getValueAt(rowIndex, columnIndex)>-1&&(double)aValue<0){
				//TODO absolutly hate this. NO VIEW HANDLER, and the rest should be in validated table model
				Drawable d = new ViewHandler(null,null).makeMessageDialog("Changing transaction to a refunded sale directly\nwill NOT "
						+ "change the quantity of stock avaliable.\nRather use Add->Refund","Warning");
				d.draw();
			} else if((double)getValueAt(rowIndex, columnIndex)<0&&(double)aValue>-1){
				//TODO absolutly hate this. NO VIEW HANDLER, and the rest should be in validated table model
				Drawable d = new ViewHandler(null,null).makeMessageDialog("Changing transaction to a sale directly\n"
						+ "will NOT change quantity of stock avaliable. \nRather use Go-to-store->Make-a-sale","Warning");
				d.draw();
			}
					
		}
		db.sql("UPDATE sales SET ? = '?' WHERE id = ? LIMIT 1")
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
