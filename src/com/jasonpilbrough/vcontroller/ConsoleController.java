package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AboutModel;
import com.jasonpilbrough.model.ConsoleModel;
import com.jasonpilbrough.view.Drawable;

public class ConsoleController extends Controller {

	private Drawable view;
	private ConsoleModel model;
	private ViewHandler viewHandler;
	
	
	public ConsoleController(ViewHandler viewHandler,ConsoleModel model, Drawable view) {
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
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}
		
	}



}