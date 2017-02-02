package com.jasonpilbrough.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.view.Drawable;

public class SalesReportTableModel implements TableModel {
	private static final String[] labels = new String[]{"Title", "Qty", "Total "};
	private static final String[] tableNames = new String[]{"title","qty", "price"};
	private static final Class[] columnClasses = new Class[]{String.class,String.class,String.class};
	private static final boolean[] editable = new boolean[]{false,false,false};
	
	private DateInTime lowerLimit;
	private DateInTime upperLimit;
	private Database db;
	private List<TableModelListener> listeners;

	public SalesReportTableModel(Database db, DateInTime lowerLimit, DateInTime upperLimit) {
		this.db = db;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		listeners = new ArrayList<>();
	}
	
	
	@Override
	public int getRowCount() {
		//work around because super class calling this method before constructor can init db
		if(db==null || lowerLimit==null || upperLimit==null)
			return 0;
		
		return getSalesRowCount() + getIncidentalsRowCount() + getPurchasesRowCount() + getRefundsRowCount();
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
		
		if(rowIndex==getRefundsRowCount() + getSalesRowCount() ){
			return new String[]{"","","",""}[columnIndex];
		} 
		
		
		else if(rowIndex<getSalesRowCount()){
			return getValueAtFromSale(rowIndex, columnIndex);
		} else if(rowIndex < getRefundsRowCount() + getSalesRowCount()){
			return getValueAtFromRefunds(rowIndex, columnIndex);
		}else if(rowIndex < getIncidentalsRowCount() + getSalesRowCount() + getRefundsRowCount()){
			return getValueAtFromIncidental(rowIndex, columnIndex);
		}else if(rowIndex < getIncidentalsRowCount() + getSalesRowCount() + getPurchasesRowCount() +getRefundsRowCount()){
			return getValueAtFromPurchases(rowIndex, columnIndex);
		}
		
		return "";
		
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }

	@Override
	public void addTableModelListener(TableModelListener l) {}

	@Override
	public void removeTableModelListener(TableModelListener l) {}
	
	private int getSalesRowCount(){
		return  (int)(long)(db.sql("SELECT COUNT(DISTINCT title) AS num FROM sales "
				+ "INNER JOIN shop_items ON sales.shop_item_id = shop_items.id "
				+ "WHERE price_per_unit_sold > -1 AND sale_date >= '?' AND sale_date <= '?'")
				.set(lowerLimit)
				.set(upperLimit)
		.retrieve().get("num"));
	}
	
	private int getIncidentalsRowCount(){
		return (int)(long)(db.sql("SELECT COUNT(DISTINCT type) AS num FROM incidentals "
				+ "WHERE amount > -1 AND date >= '?' AND date <= '?'")
				.set(lowerLimit)
				.set(upperLimit)
		.retrieve().get("num"))+1;
	}
	
	private int getPurchasesRowCount(){
		return  (int)(long)(db.sql("SELECT COUNT(DISTINCT type) AS num FROM purchases "
				+ "WHERE amount > -1 AND date >= '?' AND date <= '?'")
				.set(lowerLimit)
				.set(upperLimit)
		.retrieve().get("num"));
	}
	
	private int getRefundsRowCount(){
		return (int)(long)(db.sql("SELECT COUNT(DISTINCT title) AS num FROM sales "
				+ "INNER JOIN shop_items ON sales.shop_item_id = shop_items.id "
				+ "WHERE price_per_unit_sold < 0 AND sale_date >= '?' AND sale_date <= '?'")
				.set(lowerLimit)
				.set(upperLimit)
		.retrieve().get("num"));
	}

	
	private Object getValueAtFromSale(int rowIndex, int columnIndex){
		//TODO sql statement may cause problems with sqlite db - OFFSET
			Map<String,Object> map = db.sql("SELECT title, SUM(price_per_unit_sold*quantity_sold) AS 'price', "
					+ "SUM(quantity_sold) AS 'qty' FROM sales "
					+ "INNER JOIN shop_items ON sales.shop_item_id = shop_items.id "
					+ "WHERE price_per_unit_sold > -1 AND sale_date >= '?' AND sale_date <= '?' GROUP BY title  "
					+ "ORDER BY title LIMIT 1 OFFSET ?")
					.set(lowerLimit)
					.set(upperLimit)
					.set(rowIndex)
					.retrieve();
			return map.get(tableNames[columnIndex]).toString().toUpperCase();
	}
	
	private Object getValueAtFromRefunds(int rowIndex, int columnIndex){
		//TODO sql statement may cause problems with sqlite db - OFFSET
			Map<String,Object> map = db.sql("SELECT title, SUM(price_per_unit_sold*quantity_sold) AS 'price', "
					+ "SUM(quantity_sold) AS 'qty' FROM sales "
					+ "INNER JOIN shop_items ON sales.shop_item_id = shop_items.id "
					+ "WHERE price_per_unit_sold < 0 AND sale_date >= '?' AND sale_date <= '?' GROUP BY title "
					+ "ORDER BY title LIMIT 1 OFFSET ?")
					.set(lowerLimit)
					.set(upperLimit)
					.set(rowIndex-getSalesRowCount())
					.retrieve();
			return map.get(tableNames[columnIndex]).toString().toUpperCase();
	}
	
	private Object getValueAtFromIncidental(int rowIndex, int columnIndex){
		//TODO sql statement may cause problems with sqlite db - OFFSET
			Map<String,Object> map = db.sql("SELECT type AS title, SUM(amount) AS price, COUNT(*) AS qty FROM incidentals "
					+ "WHERE amount> -1 AND date >= '?' AND date <= '?' GROUP BY type ORDER BY title LIMIT 1 OFFSET ?")
					.set(lowerLimit)
					.set(upperLimit)
					.set(rowIndex-getSalesRowCount()-getRefundsRowCount() -1)
					.retrieve();
			return map.get(tableNames[columnIndex]).toString().toUpperCase();
	}
	
	private Object getValueAtFromPurchases(int rowIndex, int columnIndex){
		//TODO sql statement may cause problems with sqlite db - OFFSET
			Map<String,Object> map = db.sql("SELECT type AS title, (0-SUM(amount)) AS price, COUNT(*) AS qty FROM purchases "
					+ "WHERE amount> -1 AND date >= '?' AND date <= '?' GROUP BY type ORDER BY title LIMIT 1 OFFSET ?")
					.set(lowerLimit)
					.set(upperLimit)
					.set(rowIndex-getSalesRowCount()-getIncidentalsRowCount()-getRefundsRowCount())
					.retrieve();
			return map.get(tableNames[columnIndex]).toString().toUpperCase();
	}
	
	

}
