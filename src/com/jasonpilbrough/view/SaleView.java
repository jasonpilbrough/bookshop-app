package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.SmartCellEditor;
import com.jasonpilbrough.helper.SmartCellRenderer;
import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJRadioButton;
import com.jasonpilbrough.helper.SmartJSpinner;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.Tag;

public class SaleView extends JPanel implements Drawable {

	private JTable cart;
	private SmartJButton add, checkout,cancel;
	private SmartJTextField barcode, price, total;
    private SmartJRadioButton cash, card, eft,sale,refund;
    private ButtonGroup paymentGroup, transactionGroup;
	private SmartJSpinner qty;
	private JLabel preview;
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
			case "table_model":
				cart.setModel((TableModel)evt.getNewValue());
				draw();
				break;
			case "barcode":
				barcode.setText(evt.getNewValue().toString());
				draw();
				break;
			case "price":
				price.setText(evt.getNewValue().toString());
				//Drawing here messes up the focus somehow
				//draw();
				break;
			case "qty":
				qty.setValue(evt.getNewValue());
				//Drawing here messes up the focus somehow
				//draw();
				break;
			case "total":
				total.setText(evt.getNewValue().toString());
				//Drawing here messes up the focus somehow
				//draw();
				break;
			case "preview":
				preview.setText(evt.getNewValue().toString());
				//Drawing here messes up the focus somehow
				//draw();
				break;
			case "refund_transaction":
				refund.setSelected(true);
				break;
			case "sale_transaction":
				sale.setSelected(true);
				break;
			default:
				throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}
		


		//System.out.println(barcode.getSelectedText());

	}

	@Override
	public void initialise(Controller controller){
		setVisible(true);
	       setBounds(0, 0, 420, 400);
	       cart = new JTable();
	       cart.setGridColor(new java.awt.Color(218, 218, 218));
	       cart.setShowVerticalLines(false);
	       cart.setShowHorizontalLines(false);
	       cart.setAutoCreateRowSorter(true);
	       
		   
	       add = new SmartJButton("Add to Cart").withRegisteredController(controller);
	       checkout = new SmartJButton("Checkout").withRegisteredController(controller);
	       cancel = new SmartJButton("Cancel").withRegisteredController(controller);
	       
	       barcode = new SmartJTextField().withKeyListener(controller);
	       price = new SmartJTextField();
	       total = new SmartJTextField();
	       preview = new JLabel();
	       qty = new SmartJSpinner(new SpinnerNumberModel(1, 1, 999, 1));
	       
	       cash = new SmartJRadioButton("CASH");
	       cash.setSelected(true);
		   card = new SmartJRadioButton("CARD");
		   eft = new SmartJRadioButton("EFT");
		   
		   sale = new SmartJRadioButton("SALE");
	       sale.setSelected(true);
		   refund = new SmartJRadioButton("REFUND");
		   
		   paymentGroup = new ButtonGroup();
		   paymentGroup.add(cash);
	       paymentGroup.add(card);
		   paymentGroup.add(eft);
	       
		   transactionGroup = new ButtonGroup();
		   transactionGroup.add(sale);
		   transactionGroup.add(refund);
	       
	       //init command from view causes the model to push all values view
	       controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
		
		barcode = new SmartJTextField().withSomeState(barcode);
		price = new SmartJTextField().withSomeState(price);
		total = new SmartJTextField().withSomeState(total);
		add = new SmartJButton().withSomeState(add);
		checkout = new SmartJButton().withSomeState(checkout);
		cancel = new SmartJButton().withSomeState(cancel);
		preview = new JLabel(preview.getText());
		qty = new SmartJSpinner().withSomeState(qty);
		
		paymentGroup = new ButtonGroup();
		transactionGroup = new ButtonGroup();
		
		cash = new SmartJRadioButton().withSomeState(cash, paymentGroup);
	    card = new SmartJRadioButton().withSomeState(card, paymentGroup);
	    eft = new SmartJRadioButton().withSomeState(eft, paymentGroup);
	    

		sale = new SmartJRadioButton().withSomeState(sale, transactionGroup);
	    refund = new SmartJRadioButton().withSomeState(refund, transactionGroup);

		resizeColumns();
		setCustomCells();
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(420,130));
		scrollPane.setViewportView(cart);
		
		scrollPane.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Cart",
                TitledBorder.LEFT,
                TitledBorder.TOP));
		
		//remove the id column from the table
		//TODO dont like this, too tightly coupled to model with column count
		if(cart.getColumnCount()==4){
			//table.removeColumn(table.getColumnModel().getColumn(0));
		}
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		layout.row().grid(new JLabel("Transaction:")).add(sale).add(refund);
		layout.row().grid(new JLabel("Barcode:")).add(barcode);
		layout.row().grid(new JLabel("Price:")).add(price).grid(new JLabel("Qty:")).add(qty);
		//for spacing
		layout.row().grid(new JLabel(""));
		layout.row().grid(new JLabel("Preview:")).add(preview);
		layout.row().bar().add(add,Tag.APPLY);
		layout.row().grid().add(scrollPane);
        layout.row().grid(new JLabel("Payment:")).add(cash).add(card).add(eft);
		layout.row().grid().grid(new JLabel("Total:")).add(total);
		layout.row().bar().add(cancel,Tag.CANCEL).add(checkout,Tag.APPLY);
        removeAll();
        add(parent);
        revalidate();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("barcode", barcode.getText());
		if(cart.getSelectedRow()>-1){
			//map.put("selected_row_id", ""+table.getValueAt(table.getSelectedRow(), 0));
		}
		map.put("price", price.getText());
		map.put("qty", qty.getValue().toString());
		map.put("payment", cash.isSelected()?"CASH":card.isSelected()?"CARD":"EFT");
		map.put("sale_transaction", sale.isSelected());
		return map;
	}
	
	//SUMS 100%
	float[] columnWidthPercentage = {73.0f, 20.0f, 7.0f};
	
	private void resizeColumns() {
	    int tW = (int)cart.getPreferredSize().getWidth();
	    TableColumn column;
	    TableColumnModel jTableColumnModel = cart.getColumnModel();
	    int cantCols = jTableColumnModel.getColumnCount();
	    for (int i = 0; i < cantCols; i++) {
	        column = jTableColumnModel.getColumn(i);
	        int pWidth = Math.round(columnWidthPercentage[i] * tW);
	        column.setPreferredWidth(pWidth);
    }
	    
	    
	}
	private void setCustomCells(){
		for (int i = 0; i < cart.getColumnModel().getColumnCount(); i++) {
			cart.getColumnModel().getColumn(i).setCellRenderer(new SmartCellRenderer());
			cart.getColumnModel().getColumn(i).setCellEditor(new SmartCellEditor(new JTextField(), cart.getModel().getColumnClass(i)));
		}
		
	}

}
