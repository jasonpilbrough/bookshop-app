package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddLibraryItemModel;
import com.jasonpilbrough.model.UsersModel;
import com.jasonpilbrough.view.Drawable;

public class ManageUsersController extends Controller {

	private UsersModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	
	public ManageUsersController(ViewHandler viewHandler,UsersModel model, Drawable view) {
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
		case "add":
			viewHandler.displayView("AddUser");
			break;
		case "delete":
			Drawable dialog3 = viewHandler.makeConfirmDialog("Are you sure? This action cannot be undone", this);
			dialog3.draw();
			try {
				//System.out.println(dialog3.getFields().get("dialog_input").toString());
				boolean confirmed = Boolean.parseBoolean(dialog3.getFields().get("dialog_input").toString());
				if(confirmed){
					model.deleteUser(Integer.parseInt(view.getFields().get("selected_row_id").toString()));
				}
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
