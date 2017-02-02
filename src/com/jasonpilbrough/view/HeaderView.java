package com.jasonpilbrough.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.jasonpilbrough.helper.SmartJButton;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;


/* Defines the header view that appears at the top of each screen. 
 * Requires the 'initialise' method to be invoked prior to any interaction with the view. 
 * 'draw' method called to show view on screen. 
 * 'propertyChange' called by model whenever it is updated (in this view usually determining title and which
 * navigation buttons are enabled 
 * 'getFields' method is unused in this class. 
 */

public class HeaderView extends JPanel implements Drawable{
	
	private SmartJButton back;
	private JLabel title,title2;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "lib_context":
			title.setText("Library ");
			draw();
			break;
		case "store_context":
			title.setText("Bookstore");
			draw();
			break;
						
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}
		
	}

	@Override
	public void initialise(Controller controller) {
		
	
       setVisible(true);
       setBounds(0, 0, 540, 70);
       //setBackground(Color.red);
		
        title = new JLabel();
        title2 = new JLabel("Highway Christian Community");
		back = new SmartJButton("Back").withRegisteredController(controller);
		
		//init command from view causes the model to push all values view
        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
	}

	@Override
	public void draw() {
		
		title = new JLabel(title.getText());
		if(title.getText().equalsIgnoreCase("library"))
			title.setFont (title.getFont().deriveFont (28.0f));
		else
			title.setFont (title.getFont().deriveFont (25.0f));
		
		title2 = new JLabel(title2.getText());
		title2.setFont (title2.getFont().deriveFont (14.0f));
		
		JPanel parent = new JPanel();
		parent.setPreferredSize(new Dimension(521, 70));
		
		back = new SmartJButton().withSomeState(back);
		
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		layout.row().grid().add(title).empty(2).add(back);
		layout.row().grid().add(title2);
		
		removeAll();
        add(parent);   
        revalidate();
        repaint();
		
	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		return map;
	}

}
