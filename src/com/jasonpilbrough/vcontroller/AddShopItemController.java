package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddShopItemModel;
import com.jasonpilbrough.view.Drawable;

public class AddShopItemController extends Controller {


	private AddShopItemModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	
	public AddShopItemController(ViewHandler viewHandler,AddShopItemModel model, Drawable view) {
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
					model.addShopItem(view.getFields().get("barcode").toString(), view.getFields().get("title").toString(),
							view.getFields().get("author").toString(),view.getFields().get("type").toString(),
							Double.parseDouble(view.getFields().get("cost_price").toString()),
							Double.parseDouble(view.getFields().get("selling_price").toString()),
							Integer.parseInt(view.getFields().get("qty").toString()));
				}
						Drawable dialog4 = viewHandler.makeMessageDialog("Shop Item successfully added","Success",this);
						dialog4.draw();
			} catch (NumberFormatException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Cost price or selling price wrong format. Number expected","Error",this);
				dialog4.draw();
			} catch (FailedValidationException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
