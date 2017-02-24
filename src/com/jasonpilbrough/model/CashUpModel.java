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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.Money;
import com.jasonpilbrough.helper.SmartFile;

public class CashUpModel {

	private Database db;
	private AccessManager am;
	private PropertyChangeSupport changefirer;
	
	
	
	public CashUpModel(Database db, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public boolean save(double cashInBox, String explaination) throws IOException{
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		String directory = db.sql("SELECT value FROM settings WHERE name = 'cash_ups_path' LIMIT 1")
				.retrieve().get("value").toString();
		SmartFile dir = new SmartFile(directory, "");
		dir.makeDirs();
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(directory));
	    chooser.setSelectedFile(new File("cashup__"+fmt1.print(new DateTime())+".txt"));
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	    	changefirer.firePropertyChange("close", null, null);
	    	return actualSave(chooser.getCurrentDirectory().getPath(),chooser.getName(chooser.getSelectedFile()), cashInBox, explaination);
	    }else{
	    	return false;
	    }
		  
		
	}
	
	public boolean sendToPrinter(final double cashInBox, final String explaination) throws IOException, PrinterException{
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		String directory = db.sql("SELECT value FROM settings WHERE name = 'cash_ups_path' LIMIT 1")
				.retrieve().get("value").toString();
		SmartFile dir = new SmartFile(directory, "");
		dir.makeDirs();
	    actualSave(directory,"cashup__"+fmt1.print(new DateTime())+".txt", cashInBox, explaination);
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
			   
			    
				DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss aa");
				String user = am.formatUsername(am.getLoggedInUser());
				//TODO dont like this should have a money object
			    String text = "";
		    	
	    		text+="\nCASH UP REPORT";
	    		text+="\n-----------------------------------------------------------\n";
	    		text+=String.format("\n%-20s %s", "TIMESTAMP" ,fmt2.print(new DateTime()));
	    		text+=String.format("\n%-20s %s","PERFORMED BY",user);
	    		text+="\n\n-----------------------------------------------------------\n";
	    		text+=String.format("\n%-20s %s","CASH IN BOX" ,new Money(cashInBox).toString());
	    		text+=String.format("\n%-20s %s","FLOAT", new Money(getFloat()).toString());
	    		text+=String.format("\n%-20s %s","CASH SALES ",new Money(cashInBox-getFloat()).toString());
	    		text+=String.format("\n%-20s %s","RECORDED SALES",new Money(getRecordedCashSales()).toString());
	    		text+=String.format("\n\n%-20s %s","VARIENCE", new Money((cashInBox - getFloat()) - getRecordedCashSales()).toString());
	    		text+=String.format("\n%-20s %s","EXPLANATION ",formatString(explaination.length()==0?"N/A":explaination));
	    		text+="\n\n-----------------------------------------------------------";
	    		String[] lines = text.split("\n");
	    		graphics.setFont(new Font("monospaced", Font.PLAIN, graphics.getFont().getSize())); 
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
	
	private boolean actualSave(String dirPath,String filename, double cashInBox, String explaination) throws IOException{
		if(explaination.length()==0){
			explaination = "N/A";
		}
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss aa");
		String user = am.formatUsername(am.getLoggedInUser());
		
		
	    
	    	SmartFile file = new SmartFile(dirPath+"/",filename);
	    	String text = "";
	    	
    		text+="\nCASH UP REPORT";
    		text+="\n-----------------------------------------------------------\n";
    		text+=String.format("\n%-20s %s", "TIMESTAMP" ,fmt2.print(new DateTime()));
    		text+=String.format("\n%-20s %s","PERFORMED BY",user);
    		text+="\n\n-----------------------------------------------------------\n";
    		text+=String.format("\n%-20s %s","CASH IN BOX" ,new Money(cashInBox).toString());
    		text+=String.format("\n%-20s %s","FLOAT", new Money(getFloat()).toString());
    		text+=String.format("\n%-20s %s","CASH SALES ",new Money(cashInBox-getFloat()).toString());
    		text+=String.format("\n%-20s %s","RECORDED SALES",new Money(getRecordedCashSales()).toString());
    		text+=String.format("\n\n%-20s %s","VARIENCE", new Money((cashInBox - getFloat()) - getRecordedCashSales()).toString());
    		text+=String.format("\n%-20s %s","EXPLANATION ",formatString(explaination));
    		text+="\n\n-----------------------------------------------------------";
    		
    		file.write(text);
    		return true;
	}
	
	public void setAllValues(){
		changefirer.firePropertyChange("cash_float", null, new Money(0));
		changefirer.firePropertyChange("cash_sales", null, new Money(0));
		changefirer.firePropertyChange("recorded_cash_sales", null, new Money(0));
		changefirer.firePropertyChange("varience", null, new Money(0));
	}
	
	private double getFloat(){
		String val = db.sql("SELECT value FROM settings WHERE name='float' LIMIT 1")
				.retrieve().get("value").toString();
		return Double.parseDouble(val);
	}
	
	private double getRecordedCashSales(){
		Map map1 = db.sql("SELECT SUM( price_per_unit_sold * quantity_sold ) AS sum FROM sales WHERE payment = 'CASH' AND "
				+ "sale_date = '?'")
				.set(new DateInTime())
				.retrieve();
		
		Map map2 = db.sql("SELECT SUM(amount) AS sum FROM incidentals WHERE payment = 'CASH' AND date = '?'")
				.set(new DateInTime())
				.retrieve();
		
		if(map1.get("sum")==null && map2.get("sum")==null)
			return 0;
		else if( map2.get("sum")==null)
			return (double)map1.get("sum");
		else if( map1.get("sum")==null)
			return (double)map2.get("sum");
		
		return (double)map1.get("sum")+ (double)map2.get("sum");
	}
	
	public void setFloat(){
		changefirer.firePropertyChange("cash_float", null, new Money(getFloat()));
	}
	
	public void setCashSales(double cashInBox){
		double ans = cashInBox - getFloat();
		changefirer.firePropertyChange("cash_sales", null, new Money(ans));
	}
	
	public void setRecordedCashSales(){
		changefirer.firePropertyChange("recorded_cash_sales", null, new Money(getRecordedCashSales()));
	}
	
	public void setVarience(double cashInBox){
		double ans =  (cashInBox - getFloat()) - getRecordedCashSales();
		changefirer.firePropertyChange("varience", null,  new Money(ans));
	}
	
	private String formatString(String exp){
		int threshold = 30;
		int margin = 20;
		
		String[] lines = exp.split("\n");
		String finalAns = "";
		for (int k = 0; k < lines.length; k++) {
			StringBuilder sb = new StringBuilder(lines[k]);

			int i = 0;
			while ((i = sb.indexOf(" ", i + threshold)) != -1) {
			    sb.replace(i, i + 1, "\n");
			}
			
			String[] parts = sb.toString().split("\n");
			for (int j = (k==0?1:0); j < parts.length; j++) {
				parts[j] = makeMargin(margin) +parts[j];
			}
			String ans = "";
			for (int j = 0; j < parts.length; j++) {
				ans+=(j==0?"":"\n")+parts[j];
			}
			finalAns+=(k==0?"":"\n")+ans;
		}

			
			
		
		return finalAns;
	}
	
	private String makeMargin(int size){
		String ans = "";
		for (int i = 0; i < size+1; i++) {
			ans+=" ";
		}
		return ans;
	}
	
	
}
