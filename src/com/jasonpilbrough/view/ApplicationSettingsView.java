package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SpinnerNumberModel;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJSpinner;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;
import net.java.dev.designgridlayout.Tag;

public class ApplicationSettingsView extends JFrame implements Drawable {

	private SmartJSpinner loanDuration, loanExtension, membershipDuration, loanCap, membershipFee, fine;
	private SmartJSpinner floatAmount;
	private SmartJTextField password, appLogs, bugReports, cashUps, salesReport;
	private SmartJButton confirm, cancel;
	private boolean devAccess = false;
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "dev_access":
			devAccess = (boolean)evt.getNewValue();
			break;
		case "loan_duration":
			loanDuration.setValue(Integer.parseInt(evt.getNewValue().toString()));
			break;
		case "loan_extension":
			loanExtension.setValue(Integer.parseInt(evt.getNewValue().toString()));
			break;
		case "membership_duration":
			membershipDuration.setValue(Integer.parseInt(evt.getNewValue().toString()));
			break;
		case "loan_cap":
			loanCap.setValue(Integer.parseInt(evt.getNewValue().toString()));
			break;
		case "membership_fee":
			membershipFee.setValue(Integer.parseInt(evt.getNewValue().toString()));
			break;
		case "fine":
			fine.setValue(Integer.parseInt(evt.getNewValue().toString()));
			break;
		case "float":
			floatAmount.setValue(Integer.parseInt(evt.getNewValue().toString()));
			break;
		case "password":
			password.setText(evt.getNewValue().toString());
			break;
		case "app_logs_path":
			appLogs.setText(evt.getNewValue().toString());
			break;
		case "bug_reports_path":
			bugReports.setText(evt.getNewValue().toString());
			break;
		case "cash_ups_path":
			cashUps.setText(evt.getNewValue().toString());
			break;
		case "sales_reports_path":
			salesReport.setText(evt.getNewValue().toString());
			draw();
			break;
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
		setVisible(true);
	    setBounds(0, 0, 400, 350);
	    setTitle("Application Settings");
	    
	    loanDuration = new SmartJSpinner(new  SpinnerNumberModel(0, 0, 1000, 1));
	    loanExtension = new SmartJSpinner(new  SpinnerNumberModel(0, 0, 1000, 1));
	    membershipDuration = new SmartJSpinner(new  SpinnerNumberModel(0, 0, 1000, 1));
	    loanCap = new SmartJSpinner(new  SpinnerNumberModel(0, 0, 1000, 1));
	    membershipFee = new SmartJSpinner(new  SpinnerNumberModel(0, 0, 1000, 10));
	    fine = new SmartJSpinner(new  SpinnerNumberModel(0, 0, 1000, 5));
	    floatAmount = new SmartJSpinner(new  SpinnerNumberModel(0, 0, 10000, 100));
	    password = new SmartJTextField();
	    appLogs = new SmartJTextField();
	    bugReports = new SmartJTextField();
	    cashUps = new SmartJTextField();
	    salesReport = new SmartJTextField();
	    
	    confirm = new SmartJButton("Apply","confirm").withRegisteredController(controller);
	    cancel = new SmartJButton("Cancel","cancel").withRegisteredController(controller);

	    controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        
		loanDuration = new SmartJSpinner().withSomeState(loanDuration);
	    loanExtension = new SmartJSpinner().withSomeState(loanExtension);
	    membershipDuration = new SmartJSpinner().withSomeState(membershipDuration);
	    loanCap = new SmartJSpinner().withSomeState(loanCap);
	    membershipFee = new SmartJSpinner().withSomeState(membershipFee);
	    fine = new SmartJSpinner().withSomeState(fine);
	    floatAmount = new SmartJSpinner().withSomeState(floatAmount);
	    password = new SmartJTextField().withSomeState(password);
	    
	    appLogs = new SmartJTextField().withSomeState(appLogs);
	    bugReports = new SmartJTextField().withSomeState(bugReports);
	    cashUps = new SmartJTextField().withSomeState(cashUps);
	    salesReport = new SmartJTextField().withSomeState(salesReport);
	    
	    confirm = new SmartJButton().withSomeState(confirm);
	    cancel = new SmartJButton().withSomeState(cancel);
		
	    JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400,250));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(parent);
        
        DesignGridLayout layout = new DesignGridLayout(parent);
        RowGroup loansGroup = new RowGroup();
        addGroup(layout, "Loans",loansGroup);
        layout.row().group(loansGroup).grid(new JLabel("Loan duration:")).add(loanDuration).add(new JLabel("weeks"));
        layout.row().group(loansGroup).grid(new JLabel("Loan extension:")).add(loanExtension).add(new JLabel("weeks"));
        layout.row().group(loansGroup).grid(new JLabel("Outstanding loan cap:")).add(loanCap).add(new JLabel("items"));
        layout.row().group(loansGroup).grid(new JLabel("Fine:")).add(fine).add(new JLabel("R per week"));
        
        RowGroup memebrsGroup = new RowGroup();
        addGroup(layout, "Members",memebrsGroup);
        layout.row().group(memebrsGroup).grid(new JLabel("Membership duration:")).add(membershipDuration).add(new JLabel("years"));
        layout.row().group(memebrsGroup).grid(new JLabel("Membership fee:")).add(membershipFee).add(new JLabel("R per year"));
        layout.row().group(memebrsGroup).grid(new JLabel());
        
        RowGroup bookstoreGroup = new RowGroup();
        addGroup(layout, "Bookstore", bookstoreGroup);
        layout.row().group(bookstoreGroup).grid(new JLabel("Float:")).add(floatAmount).empty();
        
        RowGroup usersGroup = new RowGroup();
        addGroup(layout, "Users", usersGroup);
        layout.row().group(usersGroup).grid(new JLabel("Default password:")).add(password).empty();
        
        RowGroup fileGroup = new RowGroup();
        addGroup(layout, "User files", fileGroup);
        layout.row().group(fileGroup).grid(new JLabel("Cash ups:")).add(cashUps);
        layout.row().group(fileGroup).grid(new JLabel("Sales reports:")).add(salesReport);
        
        if(devAccess){
        	RowGroup devGroup = new RowGroup();
            addGroup(layout, "Dev files", devGroup);
            layout.row().group(devGroup).grid(new JLabel("App logs:")).add(appLogs);
            layout.row().group(devGroup).grid(new JLabel("Bug reports:")).add(bugReports);
        }
        
        
        JPanel parent2 = new JPanel();
        DesignGridLayout layout2 = new DesignGridLayout(parent2);
        layout2.row().grid().add(scrollPane);
        layout2.row().bar().add(confirm,Tag.APPLY).add(cancel, Tag.CANCEL);
        
        
        
        
        getContentPane().removeAll();
        getContentPane().add(parent2);
        revalidate();
        repaint();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		List<Object[]> settings = new ArrayList<>();
		settings.add(new Object[]{"loan_duration", loanDuration.getValue()});
		settings.add(new Object[]{"loan_extension", loanExtension.getValue()});
		settings.add(new Object[]{"membership_duration", membershipDuration.getValue()});
		settings.add(new Object[]{"loan_cap", loanCap.getValue()});
		settings.add(new Object[]{"membership_fee", membershipFee.getValue()});
		settings.add(new Object[]{"fine", fine.getValue()});
		settings.add(new Object[]{"float", floatAmount.getValue()});
		settings.add(new Object[]{"default_password", password.getText()});
		settings.add(new Object[]{"app_logs_path", appLogs.getText()});
		settings.add(new Object[]{"bug_reports_path", bugReports.getText()});
		settings.add(new Object[]{"cash_ups_path", cashUps.getText()});
		settings.add(new Object[]{"sales_reports_path", salesReport.getText()});
		
		map.put("settings", settings);
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
