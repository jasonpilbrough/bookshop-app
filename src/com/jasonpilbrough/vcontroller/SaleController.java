package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.SaleModel;
import com.jasonpilbrough.view.Drawable;

public class SaleController extends Controller {

	
	private ViewHandler viewHandler;
	private SaleModel model;
	private Drawable view;
	private boolean saleTransaction;
	
	public SaleController(ViewHandler viewHandler, SaleModel model, Drawable view, boolean saleTransaction) {
		this.viewHandler = viewHandler;
		this.model = model;
		this.view = view;
		this.saleTransaction = saleTransaction;
		
		model.addListener(view);
		view.initialise(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		//init command from view causes the model to push all values view
		case "init":
			model.setAllValues(saleTransaction);
			break;
		case "add to cart":
			try {
				String barcode = view.getFields().get("barcode").toString();
				//handles items that are out of stock
				if(!model.isItemInStock(barcode) && Boolean.parseBoolean(view.getFields().get("sale_transaction").toString())){
					Drawable dialog1 = viewHandler.makeConfirmDialog("This item is marked as out of stock.\nDo you"
							+ " wish to continue?", this);
					dialog1.draw();
					try {
						boolean confirmed = Boolean.parseBoolean(dialog1.getFields().get("dialog_input").toString());
						if(!confirmed){
							return;
						}
					} catch (NullPointerException ex) {
						ex.printStackTrace();
					} 
				}
				model.addToCart(barcode, 
						Double.parseDouble(view.getFields().get("price").toString()),
						Boolean.parseBoolean(view.getFields().get("sale_transaction").toString()), 
						Integer.parseInt(view.getFields().get("qty").toString()));
			} catch (NumberFormatException e2) {
				Drawable dialog4 = viewHandler.makeMessageDialog("Price wrong format. Number expected",this);
				dialog4.draw();
			} catch (FailedValidationException e2){
				Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
				dialog4.draw();
			}
			
			break;
		case "cancel":
			model.setAllValues(true);;
			break;
		case "checkout":
			if(model.getCartSize()>0){
				double total = model.getTotal();
				model.checkout(view.getFields().get("payment").toString());
				Drawable dialog4 = viewHandler.makeMessageDialog("Transaction successful. Total: "+total,this);
				dialog4.draw();
			}else{
				Drawable dialog4 = viewHandler.makeMessageDialog("No items in cart. Add items to cart before checking out",this);
				dialog4.draw();
			}

			break;
		case "key pressed":
			model.setPreview(view.getFields().get("barcode").toString());
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
