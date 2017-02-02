package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.model.WelcomeModel;
import com.jasonpilbrough.view.Drawable;

public class WelcomeController extends Controller {

	private Drawable view;
	private Controller parent;
	private WelcomeModel model;
	
	public WelcomeController(Controller parent, Drawable view, WelcomeModel model) {
		this.model = model;
		this.view = view;
		this.parent = parent;
		
		model.addListener(view);
		view.initialise(this);
		view.draw();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "init":
			model.setAllValues();
		case "go to library":
			parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "library context"));
			break;
		case "go to store":
			parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "store context"));
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}

}