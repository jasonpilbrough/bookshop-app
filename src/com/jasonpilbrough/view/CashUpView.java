package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.Tag;

public class CashUpView extends JFrame implements Drawable {

	private SmartJTextField cashInBox;
	private JLabel cashFloat, cashSales, recordedCashSales, varience;
	private JTextArea explaination;
    private SmartJButton save, apply;
    
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "close":
			setVisible(false);
			draw();
			break;
		case "cash_float":
			cashFloat.setText(evt.getNewValue().toString());
			draw();
			break;
		case "recorded_cash_sales":
			recordedCashSales.setText(evt.getNewValue().toString());
			draw();
			break;
		case "cash_sales":
			cashSales.setText(evt.getNewValue().toString());
			draw();
			break;
		case "varience":
			varience.setText(evt.getNewValue().toString());
			draw();
			break;
			
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
	       setVisible(true);
	       setBounds(0, 0, 420, 355);
	       setTitle("Cash Up");
	       
	        cashInBox = new SmartJTextField("");
	        cashFloat = new JLabel();
	        cashSales = new JLabel();
	        recordedCashSales = new JLabel();
	        varience = new JLabel();
	        explaination = new JTextArea();
	        
	        apply = new SmartJButton("Apply").withRegisteredController(controller);
	        save = new SmartJButton("Save").withRegisteredController(controller);
	        
	      //init command from view causes the model to push all values view
	        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        
        
        cashInBox = new SmartJTextField().withSomeState(cashInBox);
        cashFloat = new JLabel(cashFloat.getText());
        cashSales = new JLabel(cashSales.getText());
        recordedCashSales = new JLabel(recordedCashSales.getText());
        varience = new JLabel(varience.getText());
        save = new SmartJButton().withSomeState(save);
        apply = new SmartJButton().withSomeState(apply);
        //TODO component state doesnt persist
        explaination = new JTextArea();
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400,100));
		scrollPane.setViewportView(explaination);
     
		
        DesignGridLayout layout = new DesignGridLayout(parent);
        layout.row().grid(new JLabel("Date:")).add(new JLabel(new DateInTime().toString())); 
        layout.row().grid(new JLabel("Cash In Box:")).add(cashInBox,2).add(apply); 
        layout.row().grid(new JLabel("Float:")).add(cashFloat);
        layout.row().grid(new JLabel("Cash Sales:")).add(cashSales);
        layout.row().grid(new JLabel("Recorded Cash Sales:")).add(recordedCashSales);
        layout.row().grid(new JLabel("Varience:")).add(varience);
        layout.emptyRow();
        layout.row().grid(new JLabel("Explaination:")).add(scrollPane);
        layout.emptyRow();
        layout.row().grid().empty(2).add(save);
        getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("cash_in_box", cashInBox.getText());
		map.put("explaination", explaination.getText());
		return map;
	}

}
