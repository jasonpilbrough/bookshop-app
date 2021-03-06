package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.io.IOException;

import com.jasonpilbrough.helper.LogException;
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
					Drawable dialog4 = viewHandler.makeMessageDialog("Sales report successfully saved","Success",this);
					dialog4.draw();
				}
				
			} catch (IOException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Failed to save Sales report. "+e1.getMessage(),"Error",this);
				dialog4.draw();
				throw new LogException(e1);
			}
			break;
		case "print":
			try {
				boolean saved = model.print(view.getFields().get("date1").toString(), 
						view.getFields().get("date2").toString());
				if(saved){
					Drawable dialog4 = viewHandler.makeMessageDialog("Sales report sent to printer successfully","Success",this);
					dialog4.draw();
				}
				
			} catch (IOException| PrinterException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Failed to print Sales report. "+e1.getMessage(),"Error",this);
				dialog4.draw();
				throw new LogException(e1);
			}
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}
}