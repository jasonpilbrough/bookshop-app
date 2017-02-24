package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.joda.time.DateTime;

import com.jasonpilbrough.helper.SmartCellEditor;
import com.jasonpilbrough.helper.SmartCellRenderer;
import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJDatePicker;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

public class SalesReportView extends SmartJFrame implements Drawable {

	
	private JLabel cashPayments, cardPayments, eftPayments, totalIncome, costSales, refunds, purchases,totalExpense, profit;
    private SmartJButton save,print, generate;
    private JTable table;
    private SmartJDatePicker datePicker1, datePicker2;
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "close":
			setVisible(false);
			break;
		case "table_model":
			table.setModel((TableModel)evt.getNewValue());
			//draw();
			break;
		case "cash_payments":
			cashPayments.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "card_payments":
			cardPayments.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "eft_payments":
			eftPayments.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "total_income":
			totalIncome.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "cost_sales":
			costSales.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "refunds":
			refunds.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "purchases":
			purchases.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "total_expense":
			totalExpense.setText(evt.getNewValue().toString());
			//draw();
			break;
		case "profit":
			profit.setText(evt.getNewValue().toString());
			draw();
			break;
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
			setVisible(true);
			setBounds(0, 0, 460, 500);
			setTitle("Sales Report");
			
			table = new JTable();
			table.setGridColor(new java.awt.Color(218, 218, 218));
			table.setShowVerticalLines(false);
			table.setShowHorizontalLines(false);
			//table.setAutoCreateRowSorter(true);
			
			AbstractFormatter format = new AbstractFormatter() {
				
				@Override
				public String valueToString(Object value) throws ParseException {
					if(value==null){
						return "";
					}
					return new DateTime(value).toString("yyyy-MM-dd");
				}
				
				@Override
				public Object stringToValue(String text) throws ParseException {
					return new DateTime(text).toString("yyyy-MM-dd");
				}
			};

			datePicker1 = new SmartJDatePicker(new JDatePanelImpl(new UtilDateModel(),new Properties()),format);
		    datePicker2 = new SmartJDatePicker(new JDatePanelImpl(new UtilDateModel(),new Properties()),format);
		    datePicker1.getJFormattedTextField().setText(new DateTime().toString("yyyy-MM-dd"));
		    datePicker2.getJFormattedTextField().setText(new DateTime().toString("yyyy-MM-dd"));
		   
	       
	        cashPayments = new JLabel();
	        cardPayments = new JLabel();
	        eftPayments = new JLabel();
	        totalIncome = new JLabel();
	        costSales = new JLabel();
	        refunds = new JLabel();
	        purchases = new JLabel();
	        totalExpense = new JLabel();
	        profit = new JLabel();
	        
	        generate = new SmartJButton("Generate").withRegisteredController(controller);
	        save = new SmartJButton("Save").withRegisteredController(controller);
	        print = new SmartJButton("Print").withRegisteredController(controller);
	        
	      //init command from view causes the model to push all values view
	        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        
		cashPayments = new JLabel(cashPayments.getText());
		cashPayments.setFont (new Font("monospaced",Font.PLAIN,12));
		cardPayments = new JLabel(cardPayments.getText());
		cardPayments.setFont (new Font("monospaced",Font.PLAIN,12));
		eftPayments = new JLabel(eftPayments.getText());
		eftPayments.setFont (new Font("monospaced",Font.PLAIN,12));
		totalIncome = new JLabel(totalIncome.getText());
		totalIncome.setFont (new Font("monospaced",Font.BOLD,12));
		costSales = new JLabel(costSales.getText());
		costSales.setFont (new Font("monospaced",Font.PLAIN,12));
		refunds = new JLabel(refunds.getText());
		refunds.setFont (new Font("monospaced",Font.PLAIN,12));
		purchases = new JLabel(purchases.getText());
		purchases.setFont (new Font("monospaced",Font.PLAIN,12));
		totalExpense = new JLabel(totalExpense.getText());
		totalExpense.setFont (new Font("monospaced",Font.BOLD,12));
		profit = new JLabel(profit.getText());
		profit.setFont (new Font("monospaced",Font.BOLD,12));
		
		
        save = new SmartJButton().withSomeState(save);
        print = new SmartJButton().withSomeState(print);
        generate = new SmartJButton().withSomeState(generate);
        
        resizeColumns();
        setCustomCells();
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(300,150));
        scrollPane.setViewportView(table);
		
        AbstractFormatter format = new AbstractFormatter() {
			
			@Override
			public String valueToString(Object value) throws ParseException {
				if(value==null){
					return "";
				}
				return new DateTime(value).toString("yyyy-MM-dd");
			}
			
			@Override
			public Object stringToValue(String text) throws ParseException {
				return new DateTime(text).toString("yyyy-MM-dd");
			}
		};
		
		JPanel p1 = new JPanel();
		p1.add(datePicker1);
		JPanel p2 = new JPanel();
		p2.add(datePicker2);
        //datePicker1 = new SmartJDatePicker(new JDatePanelImpl(new UtilDateModel(),new Properties()),format).withSomeState(datePicker1);
        //datePicker2 = new SmartJDatePicker(new JDatePanelImpl(new UtilDateModel(),new Properties()),format).withSomeState(datePicker2);
      
        
        DesignGridLayout layout = new DesignGridLayout(parent);
        addGroup(layout, "Date From -> To");
        layout.row().grid().add(p1).add(p2);
        layout.row().grid().empty().empty().empty().add(generate);
        layout.emptyRow();
        addGroup(layout, "Transactions");
        layout.row().grid(new JLabel()).add(scrollPane);
        addGroup(layout, "Cashflow");
        layout.emptyRow();
        layout.emptyRow();
        layout.row().grid().add(new JLabel("Cash Payments:")).add(cashPayments).grid().add(new JLabel("Cost of Sales:")).add(costSales);
        layout.row().grid().add(new JLabel("Card Payments:")).add(cardPayments).grid().add(new JLabel("Refunds:")).add(refunds);
        layout.row().grid().add(new JLabel("EFT Payments:")).add(eftPayments).grid().add(new JLabel("Purchases:")).add(purchases);
        JLabel ti = new JLabel("Total Income:");
        ti.setFont (new Font(ti.getFont().getFontName(),Font.BOLD,13));
        JLabel te = new JLabel("Total Expenses:");
        te.setFont (new Font(te.getFont().getFontName(),Font.BOLD,13));
        layout.row().grid().add(ti).add(totalIncome).grid().add(te).add(totalExpense);
        layout.emptyRow();
        JLabel p = new JLabel("Profit:");
        p.setFont (new Font(p.getFont().getFontName(),Font.BOLD,13));
        layout.row().grid().add(p).add(profit).grid().empty();
        
        layout.row().grid().empty().empty().add(save).add(print);
        getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("date1", new DateTime(datePicker1.getModel().getValue()).toString("yyyy-MM-dd"));
		map.put("date2", new DateTime(datePicker2.getModel().getValue()).toString("yyyy-MM-dd"));
		return map;
	}
	
	private void setCustomCells(){
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new SmartCellRenderer());
			table.getColumnModel().getColumn(i).setCellEditor(new SmartCellEditor(new JTextField(), table.getModel().getColumnClass(i)));
		}
		
	}
	
	
	//SUMS 100%
		float[] columnWidthPercentage = {53.0f, 7.0f, 20.0f, 20.0f};
		
	private void resizeColumns() {
	    int tW = table.getWidth();
	    TableColumn column;
	    TableColumnModel jTableColumnModel = table.getColumnModel();
	    int cantCols = jTableColumnModel.getColumnCount();
	    for (int i = 0; i < cantCols; i++) {
	        column = jTableColumnModel.getColumn(i);
	        int pWidth = Math.round(columnWidthPercentage[i] * tW);
	        column.setPreferredWidth(pWidth);
	    }

	}
	
	private void addGroup(DesignGridLayout layout, String name, RowGroup group)
  	{
  		JCheckBox groupBox = new JCheckBox(name);
  		groupBox.setName(name);
  		groupBox.setForeground(Color.BLUE);
  		groupBox.setSelected(true);
  		groupBox.addItemListener(new ShowHideAction(group));
  		layout.emptyRow();
  		layout.row().left().add(groupBox, new JSeparator()).fill();
  	}
  	
  	private void addGroup(DesignGridLayout layout, String name)
  	{
  		JLabel group = new JLabel(name);
  		group.setForeground(Color.BLUE);
  		layout.emptyRow();
  		layout.row().left().add(group, new JSeparator()).fill();
  	}
  	

	private class ShowHideAction implements ItemListener
	  	{
	  		public ShowHideAction(RowGroup group)
	  		{
	  			_group = group;
	  		}
	  		
	  		@Override public void itemStateChanged(ItemEvent event)
	  		{
	  			if (event.getStateChange() == ItemEvent.SELECTED)
	  			{
	  				_group.show();
	 			}
	  			else
	  			{
	  				_group.hide();
	  			}
	  			pack();
	  		}
	  		
	  		final private RowGroup _group;
	 	}
}
