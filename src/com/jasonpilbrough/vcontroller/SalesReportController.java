package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.jasonpilbrough.helper.FileException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.SalesReportModel;
import com.jasonpilbrough.view.Drawable;

public class SalesReportController extends Controller {

	private SalesReportModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	
	public SalesReportController(ViewHandler viewHandler,SalesReportModel model, Drawable view) {
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
		case "generate":
			model.setAllValues(view.getFields().get("date1").toString(), view.getFields().get("date2").toString());
			break;
		case "save":
			try {
				boolean saved = model.save(view.getFields().get("date1").toString(), 
						view.getFields().get("date2").toString());
				if(saved){
					Drawable dialog4 = viewHandler.makeMessageDialog("Sales report successfully saved",this);
					dialog4.draw();
				}
				
			} catch (IOException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Failed to save Sales report. "+e1.getMessage(),this);
				dialog4.draw();
				throw new FileException(e1);
			}
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}
}