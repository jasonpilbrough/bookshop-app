package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.UserSettingsModel;
import com.jasonpilbrough.view.Drawable;

public class UserSettingsController extends Controller {

	private Drawable view;
	private UserSettingsModel model;
	private ViewHandler viewHandler;
	
	
	public UserSettingsController(ViewHandler viewHandler,UserSettingsModel model, Drawable view) {
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
			Drawable dialog3 = viewHandler.makePasswordDialog(this);
			dialog3.draw();
			
			Object password = dialog3.getFields().get("dialog_input");
			if(password==null){
				return;
			}
			if(model.checkPassword(password.toString())){
				model.changeUserDetails(view.getFields().get("username").toString(), 
						new String((char[])view.getFields().get("password")));
				Drawable dialog4 = viewHandler.makeMessageDialog("Changes saved",this);
				dialog4.draw();
			}else{
				Drawable dialog4 = viewHandler.makeMessageDialog("Incorrect password",this);
				dialog4.draw();
			}
			
			
			break;
		case "cancel":
			model.setAllValues();
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}
}


