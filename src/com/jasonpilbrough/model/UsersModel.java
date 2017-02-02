package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.activation.CommandInfo;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultEditorKit.DefaultKeyTypedAction;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.tablemodel.CachedTableModel;
import com.jasonpilbrough.tablemodel.UsersTableModel;

public class UsersModel implements TableModelListener{

	private Database db;
	private PropertyChangeSupport changefirer;
	private TableModel tableModel;
	
	public UsersModel(Database db) {
		super();
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	//Fires a change on all values to update all views
	public void setAllValues(){
		setTable();
	}
	
	private void setTable(){
		tableModel = new CachedTableModel(new UsersTableModel(db));
		tableModel.addTableModelListener(this);
		changefirer.firePropertyChange("table_model", "", tableModel);
		changefirer.firePropertyChange("cell_editor", "", getCellEditor());
	}
	
	public void deleteUser(int id){
		db.sql("DELETE FROM users WHERE id = ? LIMIT 1")
		.set(id)
		.update();
		
		setTable();
	}
	
	public void setUser(int userId){
		Map map =  db.sql("SELECT username,access_level FROM users WHERE id = ? LIMIT 1")
				.set(userId)
				.retrieve();
		String username = map.get("username").toString();
		int level = (int)(long)map.get("access_level");
		
		ArrayList<Object> list = (ArrayList<Object>)db.sql("SELECT description FROM privlages WHERE access_level = ?")
				.set(level)
						.retrieve().get("array");

		changefirer.firePropertyChange("username", "", username);
		changefirer.firePropertyChange("access_level", "", level);
		changefirer.firePropertyChange("privlages", "", list.toArray(new String[list.size()]));
		
	}
	
	
	public void setAccessLevel(int userId, int level){
			db.sql("UPDATE users SET access_level = ? WHERE id = ? LIMIT 1")
			.set(level)
			.set(userId)
			.update();
			
			ArrayList<Object> list = (ArrayList<Object>)db.sql("SELECT description FROM privlages WHERE access_level = ?")
					.set(level)
							.retrieve().get("array");

			changefirer.firePropertyChange("access_level", "", level);
			changefirer.firePropertyChange("privlages", "", list.toArray(new String[list.size()]));
			setTable();
	
		
	}
	
	
	private DefaultCellEditor getCellEditor(){
		Map<String,Object> map1 = db.sql("SELECT level FROM access_levels ORDER BY level DESC")
				.retrieve();
		
		Map<String,Object> map2 = db.sql("SELECT title FROM access_levels ORDER BY level DESC")
				.retrieve();
		
		List<String> list1 = (List<String>)map1.get("array");
		List<String> list2 = (List<String>)map2.get("array");
		List<String> list3 = new ArrayList<>();
		for (int i = 0; i < list1.size(); i++) {
			list3.add("Level "+String.valueOf(list1.get(i))+" ("+String.valueOf(list2.get(i))+")");
		}
		JComboBox<String> combo = new JComboBox<>(list3.toArray(new String[list3.size()]));
		
		DefaultCellEditor editor = new DefaultCellEditor(combo);
		editor.setClickCountToStart(2);
		return editor;
	}
	
	
	
	

	@Override
	public void tableChanged(TableModelEvent e) {
		setAllValues();
		
	}
	
}

