package com.jasonpilbrough.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.joda.time.DateTime;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJDatePicker;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class SalesReportView extends SmartJFrame implements Drawable {

	
	private JLabel cashPayments, cardPayments, eftPayments, totalIncome, costSales, refunds, purchases,totalExpense, profit;
    private SmartJButton save, generate;
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
			setBounds(0, 0, 550, 450);
			setTitle("Sales Report");
			
			table = new JTable();
			table.setGridColor(new java.awt.Color(218, 218, 218));
			table.setShowVerticalLines(false);
			//table.setShowHorizontalLines(false);
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
	        
	      //init command from view causes the model to push all values view
	        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        
		cashPayments = new JLabel(cashPayments.getText());
		cashPayments.setFont (new Font("Menlo",Font.PLAIN,12));
		cardPayments = new JLabel(cardPayments.getText());
		cardPayments.setFont (new Font("Menlo",Font.PLAIN,12));
		eftPayments = new JLabel(eftPayments.getText());
		eftPayments.setFont (new Font("Menlo",Font.PLAIN,12));
		totalIncome = new JLabel(totalIncome.getText());
		totalIncome.setFont (new Font("Menlo",Font.PLAIN,12));
		costSales = new JLabel(costSales.getText());
		costSales.setFont (new Font("Menlo",Font.PLAIN,12));
		refunds = new JLabel(refunds.getText());
		refunds.setFont (new Font("Menlo",Font.PLAIN,12));
		purchases = new JLabel(purchases.getText());
		purchases.setFont (new Font("Menlo",Font.PLAIN,12));
		totalExpense = new JLabel(totalExpense.getText());
		totalExpense.setFont (new Font("Menlo",Font.PLAIN,12));
		profit = new JLabel(profit.getText());
		profit.setFont (new Font("Menlo",Font.PLAIN,12));
		
		formatJLabels(new JLabel[]{cashPayments, cardPayments, eftPayments, totalIncome, costSales, refunds, purchases, totalExpense, profit});
		
        save = new SmartJButton().withSomeState(save);
        generate = new SmartJButton().withSomeState(generate);
        
        resizeColumns();
        setRightAlignment();
        
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

        layout.row().grid().add(p1).add(p2);
        layout.row().grid().empty().empty().add(generate);
        layout.emptyRow();
        layout.row().grid(new JLabel("Transactions:")).add(scrollPane);
        layout.row().grid().add(new JLabel("-- INCOME --")).grid().add(new JLabel("-- EXPENSES --"));
        layout.row().grid(new JLabel("Cash Payments:")).add(cashPayments).grid(new JLabel("Cost of Sales:")).add(costSales);
        layout.row().grid(new JLabel("Card Payments:")).add(cardPayments).grid(new JLabel("Refunds:")).add(refunds);
        layout.row().grid(new JLabel("EFT Payments:")).add(eftPayments).grid(new JLabel("Purchases:")).add(purchases);
        layout.row().grid(new JLabel("Total Income:")).add(totalIncome).grid(new JLabel("Total Expense:")).add(totalExpense);
        layout.emptyRow();
        layout.row().grid(new JLabel("Profit:")).add(profit);
        
        layout.row().grid().empty().empty().add(save);
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
	
	private void setRightAlignment(){
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(JLabel.RIGHT);
		for (int i = 0; i < table.getColumnCount(); i++) {
			if(i>0){
				table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
			}
			
		}
		
	}
	//not opperational
	private void formatJLabels(JLabel[] labels){
		return;
		/*int maxLength = 0;
		for (JLabel label : labels){ 
			
			if(label.getText().length()>maxLength){
				maxLength = label.getText().length();
			}
			
			
		}
		for (JLabel label : labels) {
			int len = label.getText().length();
			for (int i = 0; i < maxLength - len; i++) {
				label.setText("_"+label.getText());
			}
		}*/
		
	}
	
	//SUMS 100%
		float[] columnWidthPercentage = {73.0f, 7.0f, 20.0f};
		
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
}
