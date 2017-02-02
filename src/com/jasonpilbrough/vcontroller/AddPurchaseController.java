package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddPurchaseModel;
import com.jasonpilbrough.view.Drawable;

public class AddPurchaseController extends Controller {

	private AddPurchaseModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	private Controller parent;
	// USES THE SAME VIEW AS ADDING INCIDENTALS
	public AddPurchaseController(ViewHandler viewHandler, AddPurchaseModel model, Drawable view, Controller parent) {
		this(viewHandler,model,view);
		this.parent = parent;	
	}
	
	public AddPurchaseController(ViewHandler viewHandler, AddPurchaseModel model, Drawable view) {
		this.model = model;
		this.view = view;
		this.viewHandler = viewHandler;
		
		model.addListener(view);
		view.initialise(this);
		view.draw();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "init":
			model.setAllValues();
			break;
		case "confirm":
			try {
				model.add(view.getFields().get("type").toString(), 
						Double.parseDouble(view.getFields().get("price").toString()), 
						view.getFields().get("payment").toString());
				if(parent!=null){
					parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "confirm"));
				}
				
			} catch (NumberFormatException e2) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Price wrong format. Number expected",this);
				dialog4.draw();
			}
			
			break;
		case "cancel":
			model.close();
			Drawable dialog4 = viewHandler.makeMessageDialog("Transation cancelled",this);
			dialog4.draw();
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
