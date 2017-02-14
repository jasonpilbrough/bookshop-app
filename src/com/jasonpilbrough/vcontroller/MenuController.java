package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.AccessException;
import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.view.Drawable;

public class MenuController extends Controller {

	private Drawable view;
	private ViewHandler viewHandler;
	private Controller parent;
	private AccessManager am;
	

	public MenuController(Controller parent, ViewHandler viewHandler, Drawable view, AccessManager am) {
		super();
		this.view = view;
		this.viewHandler = viewHandler;
		this.parent = parent;
		this.am = am;
		
		//model.addListener(view);
		view.initialise(this);
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "exit":
			System.exit(0);
			break;
		case "about":
			viewHandler.displayView("AboutView");
			break;
		case "update":
			try{
				if(!am.allowedAccess("ACAU")){
					throw new AccessException();
				}
				Drawable dialog3 = viewHandler.makeConfirmDialog("Are you sure? This will close the current application","Confirmation needed", this);
				dialog3.draw();
				
				boolean confirmed = Boolean.parseBoolean(dialog3.getFields().get("dialog_input").toString());
				if(confirmed){
					viewHandler.displayView("AutoUpdate");
					System.exit(0);
				}
			}catch(AccessException e1){
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			
			
			break;
		case "report bug":
			viewHandler.displayView("AddBugReportView");
			break;
		case "stack trace":
			try {
				if(!am.allowedAccess("ACC")){
					throw new AccessException();
				}
				viewHandler.displayView("ConsoleView");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
		
			break;
		case "logout":
			am.logoutUser();
			parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "login context"));
			break;
		case "go to library":
			parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "library context"));
			break;	
		case "go to store":
			parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "store context"));
			break;	
		case "add member":
			try {
				if(!am.allowedAccess("ADDL")){
					throw new AccessException();
				}
				viewHandler.displayView("AddMember");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;	
		case "add library item":
			try {
				if(!am.allowedAccess("ADDL")){
					throw new AccessException();
				}
				viewHandler.displayView("AddLibraryItem");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "add loan":
			try {
				if(!am.allowedAccess("BLOP")){
					throw new AccessException();
				}
				parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "library context"));
				Drawable d = viewHandler.makeMessageDialog("To add a loan, Select a member -> New Loan",""); 
				d.draw();
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			
			break;
		case "add shop item":
			try {
				if(!am.allowedAccess("ADDS")){
					throw new AccessException();
				}
				viewHandler.displayView("AddShopItem");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "add sale":
			try {
				if(!am.allowedAccess("BSOP")){
					throw new AccessException();
				}
				parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "store context"));
				Drawable d1 = viewHandler.makeMessageDialog("To add a sale, Enter barcode -> Add to Cart -> Checkout",""); 
				d1.draw();
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			
			break;
		case "add refund":
			try {
				if(!am.allowedAccess("BSOP")){
					throw new AccessException();
				}
				parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "store context with refund"));
				Drawable d2 = viewHandler.makeMessageDialog("To add a refund, Enter barcode -> Add to Cart -> Checkout",""); 
				d2.draw();
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			
			break;
		case "add incidental":
			try {
				if(!am.allowedAccess("BOOP")){
					throw new AccessException();
				}
				viewHandler.displayView("AddIncidental");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "add purchase":
			try {
				if(!am.allowedAccess("POP")){
					throw new AccessException();
				}
				viewHandler.displayView("AddPurchase");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "search members":
			viewHandler.displayView("SearchView","Members");
			break;
		case "search library items":
			viewHandler.displayView("SearchView","Library Items");
			break;
		case "search loans":
			viewHandler.displayView("SearchView","Loans");
			break;
		case "search shop items":
			viewHandler.displayView("SearchView","Shop Items");
			break;
		case "search sales":
			viewHandler.displayView("SearchView","Sales");
			break;
		case "search incidentals":
			viewHandler.displayView("SearchView","Other income");
			break;
		case "search purchases":
			viewHandler.displayView("SearchView","Purchases");
			break;
		case "manage users":
			try {
				if(!am.allowedAccess("ACUM")){
					throw new AccessException();
				}
				viewHandler.displayView("ManageUsers");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "cash up":
			try {
				if(!am.allowedAccess("ACCU")){
					throw new AccessException();
				}
				viewHandler.displayView("CashUp");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "sales report":
			try {
				if(!am.allowedAccess("ACSR")){
					throw new AccessException();
				}
				viewHandler.displayView("SalesReport");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "user settings":
			try {
				if(!am.allowedAccess("ACUS")){
					throw new AccessException();
				}
				viewHandler.displayView("UserSettingsView");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		case "application settings":
			try {
				if(!am.allowedAccess("ACAS")){
					throw new AccessException();
				}
				viewHandler.displayView("ApplicationSettingsView");
			} catch (AccessException e1) {
				Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),"Error",this);
				dialog4.draw();
			}
			break;
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
