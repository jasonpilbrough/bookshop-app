package com.jasonpilbrough.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;
import net.java.dev.designgridlayout.Tag;


/* Defines the view accosiated with a particular member. Requires the 'initialise' method to be invoked
 * prior to any interaction with the view. 'draw' method called to show view on screen. 'propertyChange' 
 * called by model whenever it is updated and passes data to method as PropertyChangeEvent parameter.
 * Value of all fields accessible though 'getFields' method as a map. With each property name mapping to 
 * a value
 */

public class MemberView extends JFrame implements Drawable{

	
	private JTable table;
	private SmartJTextField name, phone,expireDate ;
    private SmartJButton edit, delete, cancel, confirm, renew, loan, extend, returnLoan;
	
	public MemberView(){
		super();
	}

	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
			case "close":
				setVisible(false);
				draw();
				break;
			case "name":
				name.setText(evt.getNewValue().toString());
				draw();
				break;
			case "phone_number":
				phone.setText(evt.getNewValue().toString());
				draw();
				break;
			case "expire_date":
				expireDate.setText(evt.getNewValue().toString());
				draw();
				break;
			case "editable":
				setEditable(Boolean.parseBoolean(evt.getNewValue().toString()));
				draw();
				break;
			case "loans_table_model":
				table.setModel((TableModel)evt.getNewValue());
				draw();
				break;
				
			default:
				throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}
		
		
	}

	@Override
	public void initialise(Controller controller) {
	       
	       
	       setVisible(true);
	       setBounds(0, 0, 350, 400);
	       setTitle("Member View");
	       
	       table = new JTable();
	       table.setGridColor(new java.awt.Color(218, 218, 218));
	       table.setShowVerticalLines(false);
	       
	       
	        name = new SmartJTextField("");
	        phone = new SmartJTextField("");
	        expireDate = new SmartJTextField("");
	        
	        name.setDisabledTextColor(Color.black);
	        phone.setDisabledTextColor(Color.black);
	        expireDate.setDisabledTextColor(Color.black);
	                
	        edit = new SmartJButton("Edit").withRegisteredController(controller);
	        delete = new SmartJButton("Delete").withRegisteredController(controller);
	        renew = new SmartJButton("Renew").withRegisteredController(controller);
	        cancel = new SmartJButton("Cancel").withRegisteredController(controller);
	        confirm = new SmartJButton("Confirm").withRegisteredController(controller);
	        loan = new SmartJButton("New Loan").withRegisteredController(controller);
	        extend = new SmartJButton("Extend").withRegisteredController(controller);
	        returnLoan = new SmartJButton("Return").withRegisteredController(controller);
	        
	        //edit.setIcon(new ImageIcon("res/edit.png"));
	        
	      //init command from view causes the model to push all values view
	        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
	        
		
	}
	
	private void setEditable(boolean val){

		name.setEnabled(val);
        phone.setEnabled(val);
        expireDate.setEnabled(false);
        cancel.setVisible(val);
        confirm.setVisible(val);
        
        table.setEnabled(!val);
        edit.setEnabled(!val);
        delete.setEnabled(!val);
        renew.setEnabled(!val);
        loan.setEnabled(!val);
        extend.setEnabled(!val);
        returnLoan.setEnabled(!val);
	}

	@Override
	public void draw() {
		
        JPanel parent = new JPanel();
        
        
        name = new SmartJTextField().withSomeState(name);
        phone = new SmartJTextField().withSomeState(phone);
        expireDate = new SmartJTextField().withSomeState(expireDate);
        
        edit = new SmartJButton().withSomeState(edit);
        delete = new SmartJButton().withSomeState(delete);
        renew = new SmartJButton().withSomeState(renew);
        cancel = new SmartJButton().withSomeState(cancel);
        confirm = new SmartJButton().withSomeState(confirm);
        loan = new SmartJButton().withSomeState(loan);
        extend = new SmartJButton().withSomeState(extend);
        returnLoan = new SmartJButton().withSomeState(returnLoan);
     
        
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(350,100));
		scrollPane.setViewportView(table);
		
		
		//remove the id column from the table
		//TODO dont like this, too tightly coupled to model with column count
		if(table.getColumnCount()==5){
			table.removeColumn(table.getColumnModel().getColumn(0));
			table.removeColumn(table.getColumnModel().getColumn(0));
		}
		
		resizeColumns();
		setLeftAlignment();
		
        DesignGridLayout layout = new DesignGridLayout(parent);
        
        RowGroup detailsGroup = new RowGroup();
        addGroup(layout, "Member Details",detailsGroup);
        
        layout.row().group(detailsGroup).grid().empty().add(edit).add(delete);
        layout.row().group(detailsGroup).grid().add(new JLabel("Name:")).add(name,2);
        layout.row().group(detailsGroup).grid().add(new JLabel("Phone number:")).add(phone,2);
        layout.row().group(detailsGroup).grid().add(new JLabel("Expire Date:")).add(expireDate).add(renew);
        layout.row().group(detailsGroup).grid().empty().add(cancel).add(confirm);
        
        RowGroup loansGroup = new RowGroup();
        addGroup(layout, "Loan Actions",loansGroup);
        
        layout.row().group(loansGroup).grid().add(scrollPane);
        layout.row().group(loansGroup).grid().add(returnLoan, extend).add(loan);;
        getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();
		
	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("name", name.getText());
		map.put("phone_number", phone.getText());
		//prevent the model looking for a non existent value if no row is selected
		if(table.getSelectedRow()>-1)
			map.put("selected_row_id", ""+table.getModel().getValueAt(table.getSelectedRow(), 0));
		
		return map;
	}
	
	private void setLeftAlignment(){
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.LEFT);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
		}
		
	}
	
	//SUMS 100%
	float[] columnWidthPercentage = {50.0f, 25.0f, 25.0f};
			
	private void resizeColumns() {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	    int tW = table.getWidth();
	    TableColumn column;
	    TableColumnModel jTableColumnModel = table.getColumnModel();
	    int cantCols = jTableColumnModel.getColumnCount();
	    for (int i = 0; i < cantCols; i++) {
	        column = jTableColumnModel.getColumn(i);
	        int pWidth = Math.round(columnWidthPercentage[i] * tW);
	        //column.setMinWidth(pWidth);
	        //column.setMaxWidth(pWidth);
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
