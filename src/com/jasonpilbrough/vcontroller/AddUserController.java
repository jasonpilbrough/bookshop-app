package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddUserModel;
import com.jasonpilbrough.view.Drawable;

public class AddUserController extends Controller {

	private AddUserModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	private Controller parent;
	
	public AddUserController(Controller parent,ViewHandler viewHandler,AddUserModel model, Drawable view) {
		this.parent = parent;
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
				if(Boolean.parseBoolean(view.getFields().get("default_password").toString())){
					model.addUser(view.getFields().get("username").toString(), 
							view.getFields().get("access").toString());
				}else{
					model.addUser(view.getFields().get("username").toString(), view.getFields().get("password").toString(),
							view.getFields().get("access").toString());
				}
				
				Drawable dialog4 = viewHandler.makeMessageDialog("User successfully added","Success" ,this);
				dialog4.draw();
				parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
			} catch (FailedValidationException e2) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),"Error",this);
				dialog4.draw();
			}
			
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
