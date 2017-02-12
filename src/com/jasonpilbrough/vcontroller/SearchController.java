package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.AccessException;
import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.SearchModel;
import com.jasonpilbrough.view.Drawable;

public class SearchController extends Controller {

	
	private ViewHandler viewHandler;
	private SearchModel model;
	private Drawable view;
	private AccessManager am;
	
	public SearchController(ViewHandler viewHandler, SearchModel model, Drawable view, AccessManager am) {
		this.viewHandler = viewHandler;
		this.model = model;
		this.view = view;
		this.am = am;
		
		model.addListener(view);
		view.initialise(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		//init command from view causes the model to push all values view
		case "init":
			model.setAllValues();
			break;
		case "delete":
			try {
				if(!am.allowedAccess(model.getTableCode())){
					throw new AccessException();
				}
				int[] ids = (int[])view.getFields().get("selected_ids");
				if(ids.length>0){
					Drawable dialog3 = viewHandler.makeConfirmDialog("Are you sure? This action cannot be undone", this);
					dialog3.draw();
					
					boolean confirmed = Boolean.parseBoolean(dialog3.getFields().get("dialog_input").toString());
					if(confirmed){
						model.delete(ids);
					}
					
				}else{
					Drawable dialog4 = viewHandler.makeMessageDialog("No rows selected",this);
					dialog4.draw();
				}
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),this);
				dialog4.draw();
			}
			
			break;
		case "enter pressed":
			model.setFilter(view.getFields().get("filter").toString());
			break;
		case "box selection":
			model.setTable(view.getFields().get("selected_table").toString());
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
