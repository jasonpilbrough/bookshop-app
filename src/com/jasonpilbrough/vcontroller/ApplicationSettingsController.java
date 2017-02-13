package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.util.List;

import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.ApplicationSettingsModel;
import com.jasonpilbrough.view.Drawable;

public class ApplicationSettingsController extends Controller {

	private Drawable view;
	private ApplicationSettingsModel model;
	private ViewHandler viewHandler;
	
	
	public ApplicationSettingsController(ViewHandler viewHandler,ApplicationSettingsModel model, Drawable view) {
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
			List<Object[]> settings = (List<Object[]>)view.getFields().get("settings");
			for (Object[] object : settings) {
				model.changeSetting(object[0].toString(), object[1]);
			}

			Drawable dialog4 = viewHandler.makeMessageDialog("Changes saved","Success",this);
			dialog4.draw();
			break;
		case "cancel":
			model.setAllValues();
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}



}
