package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.MembersModel;
import com.jasonpilbrough.view.Drawable;

public class MembersController extends Controller{

	private ViewHandler viewHandler;
	private MembersModel model;
	private Drawable view;
	
	public MembersController(ViewHandler viewHandler, MembersModel model, Drawable view) {
		this.viewHandler = viewHandler;
		this.model = model;
		this.view = view;
		
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
		case "select":
			try {
				int i = (int)(long)(view.getFields().get("selected_row_id"));
				viewHandler.displayView("MemberView", i);
			} catch (NullPointerException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("No member selected","Error",this);
				dialog4.draw();
			}
			break;
		case "add":
			viewHandler.displayView("AddMember");
			break;
		case "filter search":
			model.setFilter(view.getFields().get("filter").toString());
			break;
		case "enter pressed":
			model.setFilter(view.getFields().get("filter").toString());
			break;
		case "key pressed":
			//model.setFilter(view.getFields().get("filter").toString());
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}

}
