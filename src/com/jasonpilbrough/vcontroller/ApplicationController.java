package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.AccessException;
import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.ApplicationModel;
import com.jasonpilbrough.view.Drawable;

public class ApplicationController extends Controller {

	
	private ViewHandler viewHandler;
	private ApplicationModel model;
	private Drawable view;
	private AccessManager am; 
	
	public ApplicationController(ViewHandler viewHandler, ApplicationModel model, Drawable view, AccessManager am) {
		this.viewHandler = viewHandler;
		this.model = model;
		this.view = view;
		this.am = am;
		
		
	}
	
	//not ideal but avoid components on view initilising before parent
	public void initController(){
		model.addListener(view);
		view.initialise(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		//init command from view causes the model to push all values view
		case "init":
			model.setAllValues();
			break;
		case "library context":
			model.setLibraryContext();
			break;
		case "store context":
			try {
				if(!am.allowedAccess("BSOP")){
					throw new AccessException();
				}
			model.setStoreContext(true);
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "store context with refund":
			model.setStoreContext(false);
			break;
		case "welcome context":
			model.setWelcomeContext();
			break;
		case "login context":
			model.setLoginContext();
			break;
		case "window closing":
			model.windowClosing();
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
