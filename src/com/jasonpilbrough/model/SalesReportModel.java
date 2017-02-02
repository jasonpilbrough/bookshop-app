package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.table.TableModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.SmartFile;
import com.jasonpilbrough.tablemodel.CachedTableModel;
import com.jasonpilbrough.tablemodel.NonEditableTableModel;
import com.jasonpilbrough.tablemodel.SalesReportTableModel;

public class SalesReportModel {

	private Database db;
	private PropertyChangeSupport changefirer;
	
	private TableModel tableModel; 
	

	public SalesReportModel(Database db) {
		super();
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void save() throws IOException{
	    changefirer.firePropertyChange("close", null, null);
	}
	
	public void setAllValues(){
		//the add years is to make sure no rows are returned
		tableModel = new NonEditableTableModel(new CachedTableModel(
				new SalesReportTableModel(db,new DateInTime().addYears(1),new DateInTime())));
		
		changefirer.firePropertyChange("table_model", null, tableModel);  
				
		changefirer.firePropertyChange("cash_payments", null, 0.0);
		changefirer.firePropertyChange("card_payments", null, 0.0);
		changefirer.firePropertyChange("eft_payments", null, 0.0);
		changefirer.firePropertyChange("total_income", null, 0.0);
		changefirer.firePropertyChange("cost_sales", null, 0.0);
		changefirer.firePropertyChange("refunds", null, 0.0);
		changefirer.firePropertyChange("purchases", null, 0.0);
		changefirer.firePropertyChange("total_expense", null, 0.0);
		changefirer.firePropertyChange("profit", null, 0.0);
	}
	
	public void setAllValues(String date1, String date2){
		
		tableModel = new NonEditableTableModel(new CachedTableModel(
				new SalesReportTableModel(db,new DateInTime(date1),new DateInTime(date2))));
		
		changefirer.firePropertyChange("table_model", null, tableModel);  
		
		changefirer.firePropertyChange("cash_payments", null, getCashPayments(date1,date2));
		changefirer.firePropertyChange("card_payments", null, getCardPayments(date1,date2));
		changefirer.firePropertyChange("eft_payments", null, getEftPayments(date1,date2));
		changefirer.firePropertyChange("total_income", null, getTotalIncome(date1,date2));
		changefirer.firePropertyChange("cost_sales", null, getCostOfSales(date1,date2));
		changefirer.firePropertyChange("refunds", null, getRefunds(date1,date2));
		changefirer.firePropertyChange("purchases", null, getPurchases(date1,date2));
		changefirer.firePropertyChange("total_expense", null, getTotalExpense(date1,date2));
		changefirer.firePropertyChange("profit", null, getProfit(date1,date2));
	}
	
	public boolean save(String date1, String date2) throws IOException{
	

		String filepath = db.sql("SELECT value FROM settings WHERE name = 'sales_reports_path' LIMIT 1")
				.retrieve().get("value").toString();
		
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd");
		
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(filepath));
	    chooser.setSelectedFile(new File("salesreport__"+fmt1.print(new DateTime())));
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	    	SmartFile file = new SmartFile(chooser.getSelectedFile()+".txt");
	    	String text = "";
	    	
    		text+="\nSALES REPORT\n";
    		text+=String.format("%-20s %s -> %s", "DATE" ,fmt2.print(new DateTime(date1)),fmt2.print(new DateTime(date2)));
    		text+="\n===============================================================\n\n";
    		text+="TRANSACTIONS";
    		text+="\n===============================================================\n\n";
   
    		String[] columnName = new String[]{"TITLE", "QTY","TOTAL"};
    		text+=String.format("%-40s %-5s %-15s", columnName[0], columnName[1], columnName[2]);
    		text+="\n---------------------------------------------------------------";
    		
    		for (int i = 0; i < tableModel.getRowCount(); i++) {
    			text+=String.format("\n%-40s %-5s R %-15s", trim(tableModel.getValueAt(i, 0).toString()), 
    					tableModel.getValueAt(i, 1) , tableModel.getValueAt(i, 2));
			}
    		
    		text+="\n\n\nCASHFLOW";
    		text+="\n===============================================================\n";
    		text+=String.format("\n%-20s R %-10s %-20s R %s", "CASH PAYMENTS" ,getCashPayments(date1, date2), "COST OF SALES" ,getCostOfSales(date1, date2));
    		text+=String.format("\n%-20s R %-10s %-20s R %s", "CARD PAYMENTS" ,getCardPayments(date1, date2), "REFUNDS" ,getRefunds(date1, date2));
    		text+=String.format("\n%-20s R %-10s %-20s R %s", "EFT PAYMENTS" ,getEftPayments(date1, date2), "PURCHASES " ,getPurchases(date1, date2));
    		text+="\n-----------------------------     -----------------------------";
    		text+=String.format("\n%-20s R %-10s %-20s R %s", "TOTAL INCOME" ,getTotalIncome(date1, date2), "TOTAL EXPENSES" ,getTotalExpense(date1, date2));
    		
    		text+="\n\n------------------------------\n";
    		text+=String.format("%-20s R %s", "PROFIT" ,getProfit(date1, date2));
    		text+="\n------------------------------\n";
    		
    		file.write(text);
    		changefirer.firePropertyChange("close", null, null);
    		return true;
	    	
	       
	    }
	    return false;
		
	}
	
	private String trim(String val){
		int thresh = 35;
		
		if(val.length()>thresh){
			return val.substring(0, thresh-3)+ "...";
		}else{
			return val;
		}
		
	}
	
	private double getCashPayments(String lowerBound, String upperBound){
		Map map = db.sql("SELECT SUM( price_per_unit_sold * quantity_sold ) AS sum FROM sales WHERE price_per_unit_sold > -1 AND "
				+ "(payment = 'CASH' OR payment='UNKNOWN') AND sale_date >= '?' AND sale_date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		Map map2 = db.sql("SELECT SUM(amount) AS sum FROM incidentals WHERE (payment = 'CASH' OR payment='UNKNOWN') AND "
				+ "date >= '?' AND date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null && map2.get("sum")==null){
			return 0;
		} else if(map.get("sum")==null){
			return (double)map2.get("sum");
		} else if(map2.get("sum")==null){
			return (double)map.get("sum");
		}
		return (double) map.get("sum") + (double) map2.get("sum");
	}
	
	private double getCardPayments(String lowerBound, String upperBound){
		Map map = db.sql("SELECT SUM( price_per_unit_sold * quantity_sold ) AS sum FROM sales WHERE price_per_unit_sold > -1 AND "
				+ "payment = 'CARD' AND sale_date >= '?' AND sale_date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		Map map2 = db.sql("SELECT SUM(amount) AS sum FROM incidentals WHERE payment = 'CARD' AND "
				+ "date >= '?' AND date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null && map2.get("sum")==null){
			return 0;
		} else if(map.get("sum")==null){
			return (double)map2.get("sum");
		} else if(map2.get("sum")==null){
			return (double)map.get("sum");
		}
		return (double) map.get("sum") + (double) map2.get("sum");
	}
	
	private double getEftPayments(String lowerBound, String upperBound){
		Map map = db.sql("SELECT SUM( price_per_unit_sold * quantity_sold ) AS sum FROM sales WHERE price_per_unit_sold > -1 AND "
				+ "payment = 'EFT' AND sale_date >= '?' AND sale_date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		Map map2 = db.sql("SELECT SUM(amount) AS sum FROM incidentals WHERE payment = 'EFT'  AND "
				+ "date >= '?' AND date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null && map2.get("sum")==null){
			return 0;
		} else if(map.get("sum")==null){
			return (double)map2.get("sum");
		} else if(map2.get("sum")==null){
			return (double)map.get("sum");
		}
		return (double) map.get("sum") + (double) map2.get("sum");
	}
	
	private double getTotalIncome(String lowerBound, String upperBound){
		return getCashPayments(lowerBound,upperBound) + getCardPayments(lowerBound,upperBound) + getEftPayments(lowerBound,upperBound);
	}
	
	private double getCostOfSales(String lowerBound, String upperBound){
		Map map = db.sql(" SELECT SUM(cost_price * quantity_sold) AS sum FROM sales "
				+ "INNER JOIN shop_items ON sales.shop_item_id = shop_items.id WHERE price_per_unit_sold > -1 "
				+ "AND sale_date >= '?' AND sale_date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null){
			return 0;
		}
		return (double) map.get("sum");
	}
	
	private double getPurchases(String lowerBound, String upperBound){
		Map map =  db.sql("SELECT SUM(amount) AS sum FROM purchases WHERE date >= '?' AND date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null){
			return 0;
		}
		return (double) map.get("sum");
	}
	
	private double getTotalExpense(String lowerBound, String upperBound){
		return getCostOfSales(lowerBound,upperBound) + getPurchases(lowerBound,upperBound) + getRefunds(lowerBound, upperBound);
	}
	
	private double getProfit(String lowerBound, String upperBound){
		return getTotalIncome(lowerBound,upperBound) - getTotalExpense(lowerBound,upperBound);
	}
	
	private double getRefunds(String lowerBound, String upperBound){
		Map map = db.sql("SELECT SUM( price_per_unit_sold * quantity_sold ) AS sum FROM sales WHERE price_per_unit_sold < 0 AND "
				+ "sale_date >= '?' AND sale_date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null){
			return 0;
		} 
		return Math.abs((double) map.get("sum")) ;
	}
	
	
	
}