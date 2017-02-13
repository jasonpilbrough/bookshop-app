package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddIncidentalModel;
import com.jasonpilbrough.view.Drawable;

public class AddIncidentalController extends Controller {

	private AddIncidentalModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	private Controller parent;
	
	public AddIncidentalController(ViewHandler viewHandler, AddIncidentalModel model, Drawable view, Controller parent) {
		this(viewHandler,model,view);
		this.parent = parent;	
	}
	
	public AddIncidentalController(ViewHandler viewHandler, AddIncidentalModel model, Drawable view) {
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
				Drawable dialog4 = viewHandler.makeMessageDialog("Price wrong format. Number expected","Error",this);
				dialog4.draw();
			}catch (FailedValidationException e2) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),"Error",this);
				dialog4.draw();
			}
			
			break;
		case "cancel":
			model.close();
			Drawable dialog4 = viewHandler.makeMessageDialog("Transation cancelled","",this);
			dialog4.draw();
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
