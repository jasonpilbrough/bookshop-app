package com.jasonpilbrough.vcontroller;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import com.jasonpilbrough.helper.LogException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AboutModel;
import com.jasonpilbrough.model.ChangeLogModel;
import com.jasonpilbrough.view.Drawable;

public class ChangeLogController extends Controller {
	private Drawable view;
	private ChangeLogModel model;
	private ViewHandler viewHandler;
	
	
	public ChangeLogController(ViewHandler viewHandler,ChangeLogModel model, Drawable view) {
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
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
				throw new LogException(e1);
			}
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}



}