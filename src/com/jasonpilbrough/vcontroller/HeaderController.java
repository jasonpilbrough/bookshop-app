package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.model.ApplicationModel;
import com.jasonpilbrough.model.HeaderModel;
import com.jasonpilbrough.view.Drawable;

/* Controller class for view class. Handles input on the view and updates the model
 * accordingly. Controller is responsible for registering views on the model and initializing the
 * views.
 */

public class HeaderController extends Controller{

	
	private HeaderModel model;
	private Drawable view;
	private Controller parent;
	private String context; 
	
	public HeaderController(Controller parent, HeaderModel model, Drawable view, String context) {
		this.model = model;
		this.view = view;
		this.parent = parent;
		
		this.context = context;
		model.addListener(view);
		view.initialise(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		//init command from view causes the model to push all values view
		case "init":
			if(context.equalsIgnoreCase("library")){
				model.setAsLibContext();
			} else if(context.equalsIgnoreCase("store")){
				model.setAsStoreContext();
			}
			break;
		case "back":
			parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "welcome context"));
			model.setAsLibContext();
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}

}
