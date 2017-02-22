package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.LoginModel;
import com.jasonpilbrough.view.Drawable;

public class LoginController extends Controller {

	private Drawable view;
	private LoginModel model;
	private Controller parent;
	private ViewHandler viewHandler;
	
	public LoginController(ViewHandler viewHander,Controller parent, LoginModel model, Drawable view) {
		this.model = model;
		this.view = view;
		this.parent = parent;
		this.viewHandler = viewHander;
		
		model.addListener(view);
		view.initialise(this);
		view.draw();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
	
		case "login":
			login();
			break;
		case "enter pressed":
			login();
			break;
		case "key pressed":
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}
	
	private void login(){
		boolean valid = model.login(view.getFields().get("username").toString(), 
				 new String((char[])view.getFields().get("password")));
		if(valid){
			parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "welcome context"));
			if(model.accessAutoUpdate()){
				Drawable dialog3 = viewHandler.makeConfirmDialog("Would you like to check for an Application update?\n"
						+ "This will close the current application", "Confirmation Needed", this);
				dialog3.draw();
				
				boolean confirmed = Boolean.parseBoolean(dialog3.getFields().get("dialog_input").toString());
				if(confirmed){
					viewHandler.displayView("AutoUpdate");
					System.exit(0);
				}
			}
			
		}else{
			Drawable dialog4 = viewHandler.makeMessageDialog("Username or password incorrect","Error",this);
			dialog4.draw();
		}
	}

}
