package com.jasonpilbrough.view;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.helper.SmartJComboBox;
import com.jasonpilbrough.helper.SmartJRadioButton;
import com.jasonpilbrough.helper.SmartJTextField;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.Tag;

public class AddIncidentalView extends JPanel implements Drawable {
	
	private SmartJTextField price;
    private SmartJButton cancel, confirm;
    private SmartJComboBox<String> combo;
    private SmartJRadioButton cash, card, eft;
    private ButtonGroup radiogroup;

   
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "combo_model":
			combo.setModel((ComboBoxModel<String>)evt.getNewValue());
			draw();
			break;
		case "close":
			SwingUtilities.windowForComponent(this).setVisible(false);;
			setVisible(false);
			draw();
			break;
		case "price":
			price.setText(evt.getNewValue().toString());
			break;
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}

	}

	@Override
	public void initialise(Controller controller) {
		   setVisible(true);
	       setBounds(0, 0, 300, 160);
	       
	       price = new SmartJTextField("");
	       
	       cancel = new SmartJButton("Cancel").withRegisteredController(controller);
	       confirm = new SmartJButton("Confirm").withRegisteredController(controller);
	       combo = new SmartJComboBox<>();
	       
	       cash = new SmartJRadioButton("CASH");
	       cash.setSelected(true);
		   card = new SmartJRadioButton("CARD");
		   eft = new SmartJRadioButton("EFT");
		   radiogroup = new ButtonGroup();
		   radiogroup.add(cash);
	       radiogroup.add(card);
		   radiogroup.add(eft);
	       
	       controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));

	}

	@Override
	public void draw() {
		JPanel parent = new JPanel();
        

        price = new SmartJTextField().withSomeState(price);
        
        cancel = new SmartJButton().withSomeState(cancel);
        confirm = new SmartJButton().withSomeState(confirm);
        
        combo  = new SmartJComboBox<>().withSomeState(combo);
        radiogroup = new ButtonGroup();
		
		cash = new SmartJRadioButton().withSomeState(cash, radiogroup);
	    card = new SmartJRadioButton().withSomeState(card, radiogroup);
	    eft = new SmartJRadioButton().withSomeState(eft, radiogroup);
	    
	    //Object[] complexMessage = new Object[]{combo,price};
	    //JOptionPane optionPane = new JOptionPane();
	    //optionPane.setMessage(complexMessage);
	    //optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    //JDialog dialog = optionPane.createDialog(null, "Width 100");
	    //dialog.setVisible(true);
        
        DesignGridLayout layout = new DesignGridLayout(parent);
        layout.row().grid(new JLabel("Type:")).add(combo);
        layout.row().grid(new JLabel("Price:")).add(price);
        layout.row().grid(new JLabel("Payment:")).add(cash).add(card).add(eft);
        //layout.row().bar().add(cancel,Tag.CANCEL).add(confirm, Tag.APPLY);
        layout.row().bar().add(confirm, Tag.APPLY).add(cancel, Tag.CANCEL);
        removeAll();
        add(parent);
        revalidate();

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("price", price.getText());
		map.put("type", combo.getSelectedItem().toString());
		map.put("payment", cash.isSelected()?"CASH":card.isSelected()?"CARD":"EFT");
		return map;
	}

}
