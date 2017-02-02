package com.jasonpilbrough.tablemodel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	private String filter;
	private Database db;
	private List<TableModelListener> listeners;

	public SalesTableModel(Database db, String filter) {
		this.db = db;
		this.filter = filter;
		listeners = new ArrayList<>();
	}
	
	public SalesTableModel(Database db) {
		this(db,"");
	}
	
	@Override
	public int getRowCount() {
		//work around because super class calling this method before constructor can init db
		if(db==null || filter==null)
			return 0;
		
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
		
		//TODO sql statement may cause problems with sqlite db - OFFSET
		Map<String,Object> map = db.sql("SELECT sales.id, title, price_per_unit_sold, quantity_sold, sale_date, payment "
				+ "FROM sales INNER JOIN shop_items ON sales.shop_item_id = shop_items.id "
				+ "WHERE title LIKE '%?%' ORDER BY sale_date DESC,id DESC LIMIT 1 OFFSET ?")
				.set(filter)
				.set(rowIndex)
				.retrieve();
		if(columnIndex==0)
			return map.get("id");
		else
			return map.get(tableNames[columnIndex]);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		if(columnIndex==3){
			System.out.println((double)getValueAt(rowIndex, columnIndex)+" : "+(double)aValue);
			if((double)getValueAt(rowIndex, columnIndex)>-1&&(double)aValue<0){
				//TODO absolutly hate this. NO VIEW HANDLER, and the rest should be in validated table model
				Drawable d = new ViewHandler(null,null).makeMessageDialog("Warning. Changing transaction to a refunded sale directly\nwill NOT "
						+ "change the quantity of stock avaliable.\nRather use Add->Refund");
				d.draw();
			} else if((double)getValueAt(rowIndex, columnIndex)<0&&(double)aValue>-1){
				//TODO absolutly hate this. NO VIEW HANDLER, and the rest should be in validated table model
				Drawable d = new ViewHandler(null,null).makeMessageDialog("Warning. Changing transaction to a sale directly\n"
						+ "will NOT change quantity of stock avaliable. \nRather use Go-to-store->Make-a-sale");
				d.draw();
			}
					
		}
		db.sql("UPDATE sales SET ? = '?' WHERE id = ? LIMIT 1")
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
