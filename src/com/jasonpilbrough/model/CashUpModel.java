package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFileChooser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
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
		
		if(explaination.length()==0){
			explaination = "N/A";
		}
		
		String directory = db.sql("SELECT value FROM settings WHERE name = 'cash_ups_path' LIMIT 1")
				.retrieve().get("value").toString();
		
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss aa");
		String user = am.formatUsername(am.getLoggedInUser());
		//TODO dont like this should have a money object
		double varience = (cashInBox - getFloat()) - getRecordedCashSales();
		String varStr = varience<0 ? "(R "+Math.abs(varience)+")" : "R "+varience ;
		double cashSales = cashInBox-getFloat();
		String cashStr = cashSales<0 ? "(R "+Math.abs(cashSales)+")" : "R "+cashSales ;
		
		SmartFile dir = new SmartFile(directory, "");
		dir.makeDirs();
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(directory));
	    chooser.setSelectedFile(new File("cashup__"+fmt1.print(new DateTime())+".txt"));
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	    	SmartFile file = new SmartFile(chooser.getCurrentDirectory().getPath()+"/",chooser.getName(chooser.getSelectedFile()));
	    	String text = "";
	    	
    		text+="\nCASH UP REPORT";
    		text+="\n-----------------------------------------------------------\n";
    		text+=String.format("\n%-20s %s", "TIMESTAMP" ,fmt2.print(new DateTime()));
    		text+=String.format("\n%-20s %s","PERFORMED BY",user);
    		text+="\n\n-----------------------------------------------------------\n";
    		text+=String.format("\n%-20s %s","CASH IN BOX" ,"R "+cashInBox);
    		text+=String.format("\n%-20s %s","FLOAT", "R "+getFloat());
    		text+=String.format("\n%-20s %s","CASH SALES ",cashStr);
    		text+=String.format("\n%-20s %s","RECORDED SALES","R "+getRecordedCashSales());
    		text+=String.format("\n%-20s %s","VARIENCE", varStr);
    		text+=String.format("\n\n%-20s %s","EXPLANATION ",formatString(explaination));
    		text+="\n\n-----------------------------------------------------------";
    		
    		file.write(text);
    		changefirer.firePropertyChange("close", null, null);
    		
    		return true;
	    	
	       
	    }
	    return false;
		
	}
	
	public void setAllValues(){
		changefirer.firePropertyChange("cash_float", null, 0);
		changefirer.firePropertyChange("cash_sales", null, 0);
		changefirer.firePropertyChange("recorded_cash_sales", null, 0);
		changefirer.firePropertyChange("varience", null, 0);
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
		changefirer.firePropertyChange("cash_float", null, getFloat());
	}
	
	public void setCashSales(double cashInBox){
		double ans = cashInBox - getFloat();
		String cashStr = ans<0 ? "("+Math.abs(ans)+")" : ""+ans ;
		changefirer.firePropertyChange("cash_sales", null, cashStr);
	}
	
	public void setRecordedCashSales(){
		changefirer.firePropertyChange("recorded_cash_sales", null, getRecordedCashSales());
	}
	
	public void setVarience(double cashInBox){
		double ans =  (cashInBox - getFloat()) - getRecordedCashSales();
		String cashStr = ans<0 ? "("+Math.abs(ans)+")" : ""+ans ;
		changefirer.firePropertyChange("varience", null,  cashStr);
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
