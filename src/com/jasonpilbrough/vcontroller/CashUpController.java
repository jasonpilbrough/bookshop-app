package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.LogException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddLibraryItemModel;
import com.jasonpilbrough.model.CashUpModel;
import com.jasonpilbrough.view.Drawable;

public class CashUpController extends Controller {

	private CashUpModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	
	public CashUpController(ViewHandler viewHandler,CashUpModel model, Drawable view) {
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
		case "apply":
			try {
				model.setCashSales(Double.parseDouble(view.getFields().get("cash_in_box").toString()));
				model.setRecordedCashSales();
				model.setVarience(Double.parseDouble(view.getFields().get("cash_in_box").toString()));
				model.setFloat();
			} catch (NumberFormatException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("'Cash in Box' wrong format. Number expected","Error",this);
				dialog4.draw();
			}
			break;
		case "save":
			try {
				boolean saved = model.save(Double.parseDouble(view.getFields().get("cash_in_box").toString()), 
						view.getFields().get("explaination").toString());
				if(saved){
					Drawable dialog4 = viewHandler.makeMessageDialog("Cash up successfully saved","Success",this);
					dialog4.draw();
				}
				
				
			} catch (NumberFormatException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("'Cash in Box' wrong format. Number expected","Error",this);
				dialog4.draw();
			} catch (IOException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Failed to save cash up. "+e1.getMessage(),"Error",this);
				dialog4.draw();
				throw new LogException(e1);
			}
			
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
