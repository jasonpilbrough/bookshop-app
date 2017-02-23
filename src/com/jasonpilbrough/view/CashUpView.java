package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

public class CashUpView extends SmartJFrame implements Drawable {

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
	       setBounds(0, 0, 420, 400);
	       setTitle("Cash Up");
	       
	        cashInBox = new SmartJTextField("");
	        cashFloat = new JLabel();
	        cashSales = new JLabel();
	        recordedCashSales = new JLabel();
	        varience = new JLabel();
	        explaination = new JTextArea();
	        explaination.setLineWrap(true);
	        explaination.setWrapStyleWord(true);
	        
	        apply = new SmartJButton("Apply").withRegisteredController(controller);
	        save = new SmartJButton("Save").withRegisteredController(controller);
	        
	      //init command from view causes the model to push all values view
	        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        
        
        cashInBox = new SmartJTextField().withSomeState(cashInBox);
        cashInBox.setFont (new Font("monospaced",Font.PLAIN,12));
        cashFloat = new JLabel(cashFloat.getText());
        cashFloat.setFont (new Font("monospaced",Font.PLAIN,12));
        cashSales = new JLabel(cashSales.getText());
        cashSales.setFont (new Font("monospaced",Font.PLAIN,12));
        recordedCashSales = new JLabel(recordedCashSales.getText());
        recordedCashSales.setFont (new Font("monospaced",Font.PLAIN,12));
        varience = new JLabel(varience.getText());
        varience.setFont (new Font("monospaced",Font.PLAIN,12));
        save = new SmartJButton().withSomeState(save);
        apply = new SmartJButton().withSomeState(apply);
        //TODO component state doesnt persist
        explaination = new JTextArea();
        explaination.setLineWrap(true);
        explaination.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400,100));
		scrollPane.setViewportView(explaination);
     
		
        DesignGridLayout layout = new DesignGridLayout(parent);
        RowGroup infoGroup = new RowGroup();
        addGroup(layout, "Cash Up details");
        layout.row().grid(new JLabel("Date:")).add(new JLabel(new DateInTime().toString())); 
        layout.row().grid(new JLabel("Cash In Box:")).add(cashInBox,2).add(apply); 
        addGroup(layout, "Generated data");
        layout.row().grid(new JLabel("Float:")).add(cashFloat);
        layout.row().grid(new JLabel("Cash Sales:")).add(cashSales);
        layout.row().grid(new JLabel("Recorded Cash Sales:")).add(recordedCashSales);
        layout.row().grid(new JLabel("Varience:")).add(varience);
        layout.emptyRow();
        addGroup(layout, "Reason for varience");
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
