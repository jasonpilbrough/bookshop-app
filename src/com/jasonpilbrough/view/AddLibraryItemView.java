package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpinnerNumberModel;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.helper.SmartJRadioButton;
import com.jasonpilbrough.helper.SmartJSpinner;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;
import net.java.dev.designgridlayout.Tag;

public class AddLibraryItemView extends SmartJFrame implements Drawable {

	private SmartJTextField barcode, title, author;
    private SmartJButton confirm;
    private SmartJRadioButton book, cd, dvd;
    private ButtonGroup radiogroup;
    private SmartJSpinner hirefee, qty;
    
    private boolean itemExists;
    
    
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
			case "item_new":
				itemExists = false;
				draw();
				barcode.requestFocusInWindow();
				barcode.getCaret().setDot(barcode.getText().length());
				break;
			case "item_exists":
				itemExists = true;
				draw();
				barcode.requestFocusInWindow();
				barcode.getCaret().setDot(barcode.getText().length());
				break;
			case "close":
				setVisible(false);
				draw();
			break;
			default:
				throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}

	}

	@Override
	public void initialise(Controller controller) {
		setVisible(true);
	    setBounds(0, 0, 330, 240);
	    setTitle("Add Library Item");
	    
	    barcode = new SmartJTextField("").withKeyListener(controller);
	    title = new SmartJTextField("");
	    author = new SmartJTextField("");
	    book = new SmartJRadioButton("BOOK");
	    book.setSelected(true);
	    cd = new SmartJRadioButton("CD");
	    dvd = new SmartJRadioButton("DVD");
	    radiogroup = new ButtonGroup();
	    radiogroup.add(book);
	    radiogroup.add(cd);
	    radiogroup.add(dvd);
	    hirefee = new SmartJSpinner(new SpinnerNumberModel(0, 0, 1000, 5));
	    qty = new SmartJSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
	    
	    confirm = new SmartJButton("Confirm").withRegisteredController(controller);
	    
	    controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        
        
		barcode = new SmartJTextField().withSomeState(barcode);
		title = new SmartJTextField().withSomeState(title);
		author = new SmartJTextField().withSomeState(author);
	    
		radiogroup = new ButtonGroup();
		
		book = new SmartJRadioButton().withSomeState(book, radiogroup);
	    cd = new SmartJRadioButton().withSomeState(cd, radiogroup);
	    dvd = new SmartJRadioButton().withSomeState(dvd, radiogroup);
	    hirefee = new SmartJSpinner().withSomeState(hirefee);
	    qty = new SmartJSpinner().withSomeState(qty);
        
        confirm = new SmartJButton().withSomeState(confirm);
        
        DesignGridLayout layout = new DesignGridLayout(parent);
        RowGroup group = new RowGroup();
        addGroup(layout, "Library Item Details");
        layout.row().grid(new JLabel("Barcode: ")).add(barcode);
        if(itemExists){
            layout.row().grid().add(new JLabel(" Item already exists"));
            layout.row().grid(new JLabel("Add:")).add(qty);
        }else{
        	layout.row().grid(new JLabel("Title:")).add(title);
            layout.row().grid(new JLabel("Author:")).add(author);
            layout.row().grid(new JLabel("Type:")).add(book,cd,dvd);
            layout.row().grid(new JLabel("Hire Fee:")).add(hirefee).grid(new JLabel("Qty:")).add(qty);
        }
        
        layout.row().bar().add(confirm, Tag.APPLY);
        getContentPane().removeAll();
        getContentPane().add(parent);
        revalidate();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("barcode", barcode.getText());
		map.put("title", title.getText());
		map.put("author", ""+author.getText());
		map.put("type", book.isSelected()?"BOOK":cd.isSelected()?"CD":"DVD");
		map.put("hirefee", hirefee.getValue().toString());
		map.put("qty", qty.getValue().toString());
		return map;
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
