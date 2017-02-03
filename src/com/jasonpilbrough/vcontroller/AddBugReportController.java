package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.LogException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddBugReportModel;
import com.jasonpilbrough.view.Drawable;

public class AddBugReportController extends Controller {

	private Drawable view;
	private AddBugReportModel model;
	private ViewHandler viewHandler;
	
	
	public AddBugReportController(ViewHandler viewHandler,AddBugReportModel model, Drawable view) {
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
		case "confirm":
			try {
				boolean saved = model.save(view.getFields().get("message").toString());
				if(saved){
					Drawable dialog4 = viewHandler.makeMessageDialog("Bug reported. Thank you!",this);
					dialog4.draw();
				}
				
			} catch (IOException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Failed to report bug. "+e1.getMessage(),this);
				dialog4.draw();
				throw new LogException(e1);
			} catch (FailedValidationException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),this);
				dialog4.draw();
			}
			break;
		
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
		}
		
	}
}





