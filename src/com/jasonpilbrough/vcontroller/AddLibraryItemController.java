package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddLibraryItemModel;
import com.jasonpilbrough.view.Drawable;

public class AddLibraryItemController extends Controller {

	private AddLibraryItemModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	
	public AddLibraryItemController(ViewHandler viewHandler,AddLibraryItemModel model, Drawable view) {
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
			model.setAsItemNew();
			break;
		case "key pressed":
			if(model.itemExists(view.getFields().get("barcode").toString())){
				model.setAsItemExists();
			}else{
				model.setAsItemNew();
			}
			break;
		case "confirm":
			try {
				if(model.itemExists(view.getFields().get("barcode").toString())){
					model.addToStock(view.getFields().get("barcode").toString(), 
							Integer.parseInt(view.getFields().get("qty").toString()));
				}else{
					model.addLibraryItem(view.getFields().get("barcode").toString(), view.getFields().get("title").toString(),
							view.getFields().get("author").toString(),view.getFields().get("type").toString(),
							Double.parseDouble(view.getFields().get("hirefee").toString()),
							Integer.parseInt(view.getFields().get("qty").toString()));
					
				}
				Drawable dialog4 = viewHandler.makeMessageDialog("Library Item successfully added","Success",this);
				dialog4.draw();
			} catch (FailedValidationException e2) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),"Error",this);
				dialog4.draw();
			}
			
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
