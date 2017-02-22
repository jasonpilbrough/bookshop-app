package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.LogException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddBugReportModel;
import com.jasonpilbrough.model.PushDevFilesModel;
import com.jasonpilbrough.view.Drawable;

public class PushDevFilesController extends Controller {

	private Drawable view;
	private PushDevFilesModel model;
	private ViewHandler viewHandler;
	
	
	public PushDevFilesController(ViewHandler viewHandler,PushDevFilesModel model, Drawable view) {
		this.model = model;
		this.view = view;
		this.viewHandler = viewHandler;
		
		model.addListener(view);
		view.initialise(this);
		view.draw();
		model.pushDevFiles();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		
		
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
		}
		
	}
}


