package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import com.jasonpilbrough.helper.LogException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AboutModel;
import com.jasonpilbrough.view.Drawable;

public class AboutController extends Controller {

	private Drawable view;
	private AboutModel model;
	private ViewHandler viewHandler;
	
	
	public AboutController(ViewHandler viewHandler,AboutModel model, Drawable view) {
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
			try {
				model.setAllValues();
			} catch (FileNotFoundException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),this);
				dialog4.draw();
				throw new LogException(e1);
			}
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}



}
