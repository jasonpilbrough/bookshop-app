package com.jasonpilbrough.model;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.table.TableModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.Money;
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
				
		changefirer.firePropertyChange("cash_payments", null, new Money(0));
		changefirer.firePropertyChange("card_payments", null,new Money(0));
		changefirer.firePropertyChange("eft_payments", null, new Money(0));
		changefirer.firePropertyChange("total_income", null, new Money(0));
		changefirer.firePropertyChange("cost_sales", null, new Money(0));
		changefirer.firePropertyChange("refunds", null, new Money(0));
		changefirer.firePropertyChange("purchases", null, new Money(0));
		changefirer.firePropertyChange("total_expense", null, new Money(0));
		changefirer.firePropertyChange("profit", null, new Money(0));
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
	
		String directory = db.sql("SELECT value FROM settings WHERE name = 'sales_reports_path' LIMIT 1")
				.retrieve().get("value").toString();
		
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		
		SmartFile dir = new SmartFile(directory, "");
		dir.makeDirs();
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(directory));
	    chooser.setSelectedFile(new File("salesreport__"+fmt1.print(new DateTime())+".txt"));
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
			changefirer.firePropertyChange("close", null, null);
	    	return actualSave(chooser.getCurrentDirectory().getPath()+"/",chooser.getName(chooser.getSelectedFile()), date1, date2);
	       
	    }else{
	    	  return false;
	    }
	  
		
	}
	
	public boolean print(final String date1, final String date2) throws IOException, PrinterException{
		String directory = db.sql("SELECT value FROM settings WHERE name = 'sales_reports_path' LIMIT 1")
				.retrieve().get("value").toString();
		
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		
		SmartFile dir = new SmartFile(directory, "");
		dir.makeDirs();
	    actualSave(directory,"salesreport__"+fmt1.print(new DateTime())+".txt", date1, date2);
	    PrinterJob pj = PrinterJob.getPrinterJob();
	    
	    pj.setPrintable(new Printable() {
			
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				 // We have only one page, and 'page'
			    // is zero-based
			    if (pageIndex > 0) {
			         return NO_SUCH_PAGE;
			    }

			    // User (0,0) is typically outside the
			    // imageable area, so we must translate
			    // by the X and Y values in the PageFormat
			    // to avoid clipping.
			    Graphics2D g2d = (Graphics2D)graphics;
			    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

			    // Now we perform our rendering
			   
			    
				DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd");
			    String text = "";
		    	
			    text+="\nSALES REPORT\n";
				text+=String.format("%-20s %s -> %s", "DATE" ,fmt2.print(new DateTime(date1)),fmt2.print(new DateTime(date2)));
				text+="\n===============================================================\n\n";
				text+="TRANSACTIONS";
				text+="\n===============================================================\n\n";

				String[] columnName = new String[]{"TITLE", "QTY","COST","TOTAL"};
				text+=String.format("%-32s %-8s %-12s %-12s", columnName[0], columnName[1], columnName[2], columnName[3]);
				text+="\n---------------------------------------------------------------";
				
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					text+=String.format("\n%-32s %-5s %-12s %-12s", trim(tableModel.getValueAt(i, 0).toString()), 
							tableModel.getValueAt(i, 1) , 
							(tableModel.getValueAt(i, 2).toString().length()==0?"":new Money(tableModel.getValueAt(i, 2)).toStringWithoutCurrency()),
							(tableModel.getValueAt(i, 3).toString().length()==0?"":new Money(tableModel.getValueAt(i, 3)).toStringWithoutCurrency()));
				}
				
				text+="\n\n\nCASHFLOW";
				text+="\n===============================================================\n";
				text+=String.format("\n%-15s %-17s %-15s %s", "CASH PAYMENTS" ,getCashPayments(date1, date2), "COST OF SALES" ,getCostOfSales(date1, date2));
				text+=String.format("\n%-15s %-17s %-15s %s", "CARD PAYMENTS" ,getCardPayments(date1, date2), "REFUNDS" ,getRefunds(date1, date2));
				text+=String.format("\n%-15s %-17s %-15s %s", "EFT PAYMENTS" ,getEftPayments(date1, date2), "PURCHASES " ,getPurchases(date1, date2));
				text+="\n-----------------------------     -----------------------------";
				text+=String.format("\n%-15s %-17s %-15s %s", "TOTAL INCOME" ,getTotalIncome(date1, date2), "TOTAL EXPENSES" ,getTotalExpense(date1, date2));
				
				text+="\n\n------------------------------\n";
				text+=String.format("%-15s %s", "PROFIT" ,getProfit(date1, date2));
				text+="\n------------------------------\n";
				
	    		String[] lines = text.split("\n");
	    		graphics.setFont(new Font("monospaced", Font.PLAIN, 10)); 
	    		for (int i = 0; i < lines.length; i++) {
	    			 graphics.drawString(lines[i], 35, 15*(i+3));
				}

			    // tell the caller that this page is part
			    // of the printed document
	    		//throw new PrinterException();
			    return PAGE_EXISTS;
			}
		});
	   
        if (pj.printDialog()) {
        	pj.print();
        	changefirer.firePropertyChange("close", null, null);
        	return true;
        }else{
        	return false;
        }
	    
	}
	
	private boolean actualSave(String dir, String filename,String date1, String date2) throws IOException{

		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd");
		SmartFile file = new SmartFile(dir,filename);
    	String text = "";
    	
    	text+="\nSALES REPORT\n";
		text+=String.format("%-20s %s -> %s", "DATE" ,fmt2.print(new DateTime(date1)),fmt2.print(new DateTime(date2)));
		text+="\n===============================================================\n\n";
		text+="TRANSACTIONS";
		text+="\n===============================================================\n\n";

		String[] columnName = new String[]{"TITLE", "QTY","COST","TOTAL"};
		text+=String.format("%-32s %-8s %-12s %-12s", columnName[0], columnName[1], columnName[2], columnName[3]);
		text+="\n---------------------------------------------------------------";
		
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			text+=String.format("\n%-32s %-5s %-12s %-12s", trim(tableModel.getValueAt(i, 0).toString()), 
					tableModel.getValueAt(i, 1) , 
					(tableModel.getValueAt(i, 2).toString().length()==0?"":new Money(tableModel.getValueAt(i, 2)).toStringWithoutCurrency()),
					(tableModel.getValueAt(i, 3).toString().length()==0?"":new Money(tableModel.getValueAt(i, 3)).toStringWithoutCurrency()));
		}
		
		text+="\n\n\nCASHFLOW";
		text+="\n===============================================================\n";
		text+=String.format("\n%-15s %-17s %-15s %s", "CASH PAYMENTS" ,getCashPayments(date1, date2), "COST OF SALES" ,getCostOfSales(date1, date2));
		text+=String.format("\n%-15s %-17s %-15s %s", "CARD PAYMENTS" ,getCardPayments(date1, date2), "REFUNDS" ,getRefunds(date1, date2));
		text+=String.format("\n%-15s %-17s %-15s %s", "EFT PAYMENTS" ,getEftPayments(date1, date2), "PURCHASES " ,getPurchases(date1, date2));
		text+="\n-----------------------------     -----------------------------";
		text+=String.format("\n%-15s %-17s %-15s %s", "TOTAL INCOME" ,getTotalIncome(date1, date2), "TOTAL EXPENSES" ,getTotalExpense(date1, date2));
		
		text+="\n\n------------------------------\n";
		text+=String.format("%-15s %s", "PROFIT" ,getProfit(date1, date2));
		text+="\n------------------------------\n";
		
		file.write(text);
		return true;
	}
	
	private String trim(String val){
		int thresh = 27;
		
		if(val.length()>thresh){
			return val.substring(0, thresh-3)+ "...";
		}else{
			return val;
		}
		
	}
	
	private Money getCashPayments(String lowerBound, String upperBound){
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
			return new Money(0);
		} else if(map.get("sum")==null){
			return new Money((double)map2.get("sum"));
		} else if(map2.get("sum")==null){
			return new Money((double)map.get("sum"));
		}
		return new Money((double) map.get("sum") + (double) map2.get("sum"));
	}
	
	private Money getCardPayments(String lowerBound, String upperBound){
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
			return new Money(0);
		} else if(map.get("sum")==null){
			return new Money((double)map2.get("sum"));
		} else if(map2.get("sum")==null){
			return new Money((double)map.get("sum"));
		}
		return new Money((double) map.get("sum") + (double) map2.get("sum"));
	}
	
	private Money getEftPayments(String lowerBound, String upperBound){
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
			return new Money(0);
		} else if(map.get("sum")==null){
			return new Money((double)map2.get("sum"));
		} else if(map2.get("sum")==null){
			return new Money((double)map.get("sum"));
		}
		return new Money((double) map.get("sum") + (double) map2.get("sum"));
	}
	
	private Money getTotalIncome(String lowerBound, String upperBound){
		return getCashPayments(lowerBound,upperBound).add(getCardPayments(lowerBound,upperBound).add(getEftPayments(lowerBound,upperBound)));
	}
	
	private Money getCostOfSales(String lowerBound, String upperBound){
		Map map = db.sql(" SELECT SUM(cost_price *SIGN(price_per_unit_sold) * quantity_sold) AS sum FROM sales "
				+ "INNER JOIN shop_items ON sales.shop_item_id = shop_items.id WHERE sale_date >= '?' AND sale_date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null){
			return new Money(0);
		}
		return new Money((double) map.get("sum"));
	}
	
	private Money getPurchases(String lowerBound, String upperBound){
		Map map =  db.sql("SELECT SUM(amount) AS sum FROM purchases WHERE date >= '?' AND date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null){
			return new Money(0);
		}
		return new Money((double) map.get("sum"));
	}
	
	private Money getTotalExpense(String lowerBound, String upperBound){
		return getCostOfSales(lowerBound,upperBound).add(getPurchases(lowerBound,upperBound).add(getRefunds(lowerBound, upperBound)));
	}
	
	private Money getProfit(String lowerBound, String upperBound){
		return getTotalIncome(lowerBound,upperBound).subtract(getTotalExpense(lowerBound,upperBound));
	}
	
	private Money getRefunds(String lowerBound, String upperBound){
		Map map = db.sql("SELECT SUM( price_per_unit_sold * quantity_sold ) AS sum FROM sales WHERE price_per_unit_sold < 0 AND "
				+ "sale_date >= '?' AND sale_date <= '?'")
				.set(lowerBound)
				.set(upperBound)
				.retrieve();
		
		if(map.get("sum")==null){
			return new Money(0);
		} 
		return new Money(Math.abs((double) map.get("sum"))) ;
	}
	
	
	
}