package com.jasonpilbrough.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class ManageUsersView extends SmartJFrame implements Drawable {


    private SmartJButton delete, add;
    private JTable table;
   
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "table_model":
			//to ensure the same user is selected regardless of a change in their position in the table
			if(table.getSelectedRow()>-1){
				int id = (int)(long)table.getModel().getValueAt(table.getSelectedRow(), 0);
				table.setModel((TableModel)evt.getNewValue());
				
				for (int i = 0; i < table.getModel().getRowCount(); i++) {
					if(id==(int)(long)table.getModel().getValueAt(i, 0)){
						table.setRowSelectionInterval(i, i);
					}
				}
			}else{
				table.setModel((TableModel)evt.getNewValue());
			}
			draw();
		break;
		case "cell_editor":
			table.getColumnModel().getColumn(1).setCellEditor((TableCellEditor)evt.getNewValue());
			draw();
		break;
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
	}

	}

	@Override
	public void initialise(Controller controller) {
		 	setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
	       
	       
	       setVisible(true);
	       setBounds(0, 0, 330, 270);
	       setTitle("User Management");
	       
	       table = new JTable();
		   table.setGridColor(new java.awt.Color(218, 218, 218));
		   table.setShowVerticalLines(false);
		   table.setAutoCreateRowSorter(true);
			
		   table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					//controller.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "row selection changed"));
					//draw();
					
				}
			});
			
	       delete = new SmartJButton("Delete").withRegisteredController(controller);
	       add = new SmartJButton("Add").withRegisteredController(controller);
	       
	       

	       //init command from view causes the model to push all values view
		   controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();

		
		delete = new SmartJButton().withSomeState(delete);
		add = new SmartJButton().withSomeState(add);
		
	       
		
			
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(150,150));
		scrollPane.setViewportView(table);
		
		//remove the id column from the table
		//TODO dont like this, too tightly coupled to model with column count
		if(table.getColumnName(0).equalsIgnoreCase("id"))
			table.removeColumn(table.getColumnModel().getColumn(0));
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		
		
		layout.row().grid().empty(2).add(new SmartJButton("View Privlages"));
		layout.row().grid().add(scrollPane);
		//layout.row().grid().spanRow().grid(new JLabel("Privlages:"));
		//layout.row().grid().spanRow().grid(new JLabel("Privlages:")).add(new JLabel("* NOTE - There can only be system at a time."));
		//for (int i = 0; i < privlages.size(); i++) {
		//	JLabel label = new JLabel(privlages.get(i).replaceAll("_", " "));
		//	label.setFont(label.getFont().deriveFont (10.0f));
		//	layout.row().grid().spanRow().grid().add(label);
		//}
			
		
			
		
		
		layout.row().grid().add(add).empty().add(delete);
        getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();
        
        
        

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		if(table.getSelectedRow()>-1)
			map.put("selected_row_id", ""+table.getModel().getValueAt(table.getSelectedRow(), 0));
		return map;
	}
	
	private ArrayList<String> formatStrings(String[] strings){
		int threshold = 60;
		ArrayList<String> vals = new ArrayList<>();
		for (int i = 0; i < strings.length; i++) {
			if(strings[i].length()< threshold){
				vals.add("* "+strings[i]);
			}else{
				String temp = "";
				String[] splits = strings[i].split(" ");
				for (int j = 0; j < splits.length; j++) {
					if(temp.length()+splits[j].length()<threshold){
						temp +=  splits[j]+" ";
					}else{
						if(temp.length()>0){
							vals.add("* "+temp);
						}
						
						temp = "___________"+splits[j]+" ";
					}
					
				}
				if(temp.length()>0){
					vals.add(temp);
				}
				
			}
		}
		return vals;
	}

}
