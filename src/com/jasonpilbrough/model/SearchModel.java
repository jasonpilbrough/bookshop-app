package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.tablemodel.AccessControlledTableModel;
import com.jasonpilbrough.tablemodel.CachedTableModel;
import com.jasonpilbrough.tablemodel.IncidentalsTableModel;
import com.jasonpilbrough.tablemodel.LibraryItemsTableModel;
import com.jasonpilbrough.tablemodel.LoansTableModel;
import com.jasonpilbrough.tablemodel.MembersTableModel;
import com.jasonpilbrough.tablemodel.PurchasesTableModel;
import com.jasonpilbrough.tablemodel.SalesTableModel;
import com.jasonpilbrough.tablemodel.ShopItemsTableModel;
import com.jasonpilbrough.tablemodel.ValidatedTableModel;

public class SearchModel implements TableModelListener{

	private Database db;
	private AccessManager am;
	private PropertyChangeSupport changefirer;
	private final static String[] labels = new String[]{"Members","Library Items","Loans","Shop Items","Sales","Other Income","Purchases"};
	private final static String[] table_names = new String[]{"members","library_items","loans","shop_items","sales","incidentals","purchases"};
	private String selectedLabel;

	private TableModel tableModel;
	private ComboBoxModel<String> comboModel;
	
	
	public SearchModel(Database db, String initalSelection, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		selectedLabel = initalSelection;
		changefirer = new PropertyChangeSupport(this);
		//TODO dont like this no error checking if invalid selection passed
		tableModel = makeTableModel(initalSelection);
		tableModel.addTableModelListener(this);
		comboModel = makeComboModel();
	}

	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	//Fires a change on all values to update all views
	public void setAllValues(){
		tableModel = makeTableModel(selectedLabel);
		tableModel.addTableModelListener(this);
		comboModel = makeComboModel();
		
		// the order of this commands must stay the same, weird i know
		changefirer.firePropertyChange("combo_model", null, comboModel);
		changefirer.firePropertyChange("table_model", null, tableModel);
	}
	
	public void setFilter(String text){
		tableModel = makeTableModel(selectedLabel,text);
		tableModel.addTableModelListener(this);
		changefirer.firePropertyChange("table_model", null, tableModel);
	}
	
	public void setTable(String selection){
		selectedLabel = selection;
		tableModel = makeTableModel(selection);
		tableModel.addTableModelListener(this);
		changefirer.firePropertyChange("table_model", null, tableModel);
	}
	
	public String getTableCode(){
		if(selectedLabel.equalsIgnoreCase("members")||selectedLabel.equalsIgnoreCase("library items") || 
				selectedLabel.equalsIgnoreCase("loans")){
			return "ELOP";
		}
		if(selectedLabel.equalsIgnoreCase("shop items")||selectedLabel.equalsIgnoreCase("sales")){
			return "ESOP";
		}
		if(selectedLabel.equalsIgnoreCase("incidentals")){
			return "EOOP";
		}
		if(selectedLabel.equalsIgnoreCase("purchases")){
			return "POP";
		}
		return "";
	}
	
	public void delete(int[] ids){
		ArrayList<String> list = new ArrayList<>(Arrays.asList(labels));
		for (int i = 0; i < ids.length; i++) {
			db.sql("DELETE FROM ? WHERE id = ? LIMIT 1")
			.set(table_names[list.indexOf(selectedLabel)])
			.set(ids[i])
			.update();
		}
		setTable(selectedLabel);
	}
	
	private ComboBoxModel<String> makeComboModel(){
		ComboBoxModel<String> model = new ComboBoxModel<String>() {

			@Override
			public int getSize() {
				return labels.length;
			}

			@Override
			public String getElementAt(int index) {
				return labels[index];
			}

			@Override
			public void addListDataListener(ListDataListener l) {}

			@Override
			public void removeListDataListener(ListDataListener l) {}

			@Override
			public void setSelectedItem(Object anItem) {
				selectedLabel = anItem.toString();
				
			}

			@Override
			public Object getSelectedItem() {
				return selectedLabel;
			}
		};
		model.setSelectedItem(selectedLabel);
		return model;
	}
	
	private TableModel makeTableModel(String tablename){
		return makeTableModel(tablename,"");
	}
	
	private TableModel makeTableModel(String tablename,String filter){
		//TODO hate passing in code
		if(tablename.equalsIgnoreCase("members")){
			return new AccessControlledTableModel(new ValidatedTableModel(new MembersTableModel(db,filter)), am, "ELOP");
		}else if(tablename.equalsIgnoreCase("library items")){
			return new AccessControlledTableModel(new ValidatedTableModel(new LibraryItemsTableModel(db,filter)), am, "ELOP");
		}else if(tablename.equalsIgnoreCase("loans")){
			return new AccessControlledTableModel(new ValidatedTableModel(new LoansTableModel(db,filter)), am, "ELOP");
		}else if(tablename.equalsIgnoreCase("shop items")){
			return new AccessControlledTableModel(new ValidatedTableModel(new ShopItemsTableModel(db,filter)), am, "ESOP");
		}else if(tablename.equalsIgnoreCase("sales")){
			return new AccessControlledTableModel(new ValidatedTableModel(new SalesTableModel(db,filter)), am, "ESOP");
		}else if(tablename.equalsIgnoreCase("other income")){
			return new AccessControlledTableModel(new ValidatedTableModel(new IncidentalsTableModel(db,filter)), am, "EOOP");
		}else if(tablename.equalsIgnoreCase("purchases")){
			return new AccessControlledTableModel(new ValidatedTableModel(new PurchasesTableModel(db,filter)), am, "POP");
		}
		
		return null;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		//setAllValues();
		if(e.getType()>0){
			changefirer.firePropertyChange("progress", null, e.getType());
		}
		changefirer.firePropertyChange("table_model", null, tableModel);
	}
	
}
