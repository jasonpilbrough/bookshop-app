package com.jasonpilbrough.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class MembersView extends JPanel implements Drawable{

	private JTable table;
	private SmartJButton add, select,filterBtn;
	private SmartJTextField filter;
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "table_model":
			table.setModel((TableModel)evt.getNewValue());

			draw();
			
			filter.requestFocusInWindow();
			filter.getCaret().setDot(filter.getText().length());
			
			
			break;
			
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}
		
	}

	@Override
	public void initialise(Controller controller) {
	   setVisible(true);
       setBounds(0, 0, 450, 400);
       table = new JTable();
       table.setGridColor(new java.awt.Color(218, 218, 218));
       table.setShowVerticalLines(false);
	   table.setAutoCreateRowSorter(true);
	   table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	   table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = table.rowAtPoint(evt.getPoint());
		        int col = table.columnAtPoint(evt.getPoint());
		        if (row >= 0 && col >= 0) {
		        	if(evt.getClickCount()==2){
		        		controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "select"));
		        	}
		            

		        }
		    }
		});
       
       add = new SmartJButton("Add").withRegisteredController(controller);
       select = new SmartJButton("Select").withRegisteredController(controller);
       filterBtn = new SmartJButton("Filter Search").withRegisteredController(controller);
       
       filter = new SmartJTextField()
    		   .withFocusListener("Filter search")
    		   .withKeyListener(controller);
       

       
       //init command from view causes the model to push all values view
       controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
		
	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
		
		filter = new SmartJTextField().withSomeState(filter);
		filterBtn = new SmartJButton().withSomeState(filterBtn);

		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(450,300));
		scrollPane.setViewportView(table);
		

		resizeColumns();
		setLeftAlignment();
		
		scrollPane.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Members",
                TitledBorder.LEFT,
                TitledBorder.TOP));
		
		//remove the id column from the table
		//TODO dont like this, too tightly coupled to model with column count
		//if(table.getColumnName(0).equalsIgnoreCase("id"))
			//table.removeColumn(table.getColumnModel().getColumn(0));
			
		
		
        add = new SmartJButton().withSomeState(add);
        select = new SmartJButton().withSomeState(select);

		DesignGridLayout layout = new DesignGridLayout(parent);
		layout.row().grid().add(filter,3).add(filterBtn);
		layout.row().grid().add(scrollPane);
		layout.row().grid().empty().empty().empty().add(select);
        removeAll();
        add(parent);
        revalidate();
		
	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("filter", filter.getText());

		if(table.getSelectedRow()>-1){
			map.put("selected_row_id", table.getValueAt(table.getSelectedRow(), 0));
			//System.out.println(table.getValueAt(table.getSelectedRow(), 0));
		}
		
		return map;
	}
	
	//SUMS 100%
		float[] columnWidthPercentage = {7.0f, 44.0f, 26.0f, 23.0f};
		
		
	private void setLeftAlignment(){
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.LEFT);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
		}
		
	}
	private void resizeColumns() {
	    int tW = (int)table.getPreferredSize().getWidth();
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

}
