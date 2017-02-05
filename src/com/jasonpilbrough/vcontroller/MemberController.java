package com.jasonpilbrough.vcontroller;
import java.awt.event.ActionEvent;

import com.jasonpilbrough.helper.AccessException;
import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.MemberModel;
import com.jasonpilbrough.view.Drawable;

/* Controller class for view. Handles input on the view and updates the model
 * accordingly. Controller is responsible for registering views on the model and initializing the
 * views.
 */

public class MemberController extends Controller {
	
	private MemberModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	private Controller parent;
	private AccessManager am;

	public MemberController(Controller parent, ViewHandler viewHandler,MemberModel model, Drawable view, AccessManager am) {
		this.model = model;
		this.view = view;
		this.viewHandler = viewHandler;
		this.parent = parent;
		this.am =am;
		
		model.addListener(view);
		view.initialise(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			//init command from view causes the model to push all values view
			case "init":
				model.setAllValues();
				if(model.hasMembershipExpired()){
					Drawable dialog4 = viewHandler.makeMessageDialog("Membership has expired",this);
					dialog4.draw();
				}
				
				break;
			case "edit":
				try{
					if(!am.allowedAccess("ELOP")){
						throw new AccessException();
					}
					model.setEditable(true);
				}catch (AccessException e2) {
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
				break;
			case "delete":
				try {
					if(!am.allowedAccess("ELOP")){
						throw new AccessException();
					}
					Drawable dialog3 = viewHandler.makeConfirmDialog("Are you sure? This action cannot be undone", this);
					dialog3.draw();
					
					boolean confirmed = Boolean.parseBoolean(dialog3.getFields().get("dialog_input").toString());
					if(confirmed){
						model.deleteMember();
					}
					
					parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));	
				} catch (AccessException e2) {
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
					
				break;
			case "confirm":
				try {
					model.setName(view.getFields().get("name").toString());
					model.setPhoneNumber((view.getFields().get("phone_number")).toString());
					model.setEditable(false);
					parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
				} catch (FailedValidationException e2) {
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
					
				break;
			case "cancel":
				model.setAllValues();
				break;
			case "renew":
				try {
					if(!am.allowedAccess("BLOP")){
						throw new AccessException();
					}
					//TODO preferences - remove hard 20
					viewHandler.displayView("AddIncidental", "MEMBERSHIP FEE", model.getMembershipFee(), new Controller() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
							model.renewMembership();
							parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));	
							
						}
					});
				} catch (AccessException e2){
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
				
				break;			
			case "return":
				try {
					if(!am.allowedAccess("BLOP")){
						throw new AccessException();
					}
					final int id = Integer.parseInt(view.getFields().get("selected_row_id").toString());
					double fine = model.getFine(id);
					if(fine>0){
						viewHandler.displayView("AddIncidental", "FINE", fine, new Controller() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								model.returnLoan(id);
								
							}
						});
						
						
					}else{
						model.returnLoan(id);
					}
					parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));	
				} catch (NumberFormatException e2) {
					Drawable dialog4 = viewHandler.makeMessageDialog("No loan selected",this);
					dialog4.draw();
				} catch (AccessException e2){
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
				
				
				break;
			case "extend":
				try {
					if(!am.allowedAccess("BLOP")){
						throw new AccessException();
					}
					model.extendLoan(Integer.parseInt(view.getFields().get("selected_row_id").toString()));
					parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));	
				} catch (NumberFormatException e2) {
					Drawable dialog4 = viewHandler.makeMessageDialog("No loan selected",this);
					dialog4.draw();
				} catch (AccessException e2){
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
				
				break;
			case "new loan":
				
				
				try {
					if(!am.allowedAccess("BLOP")){
						throw new AccessException();
					}
					//handles members with more outstanding loans than allowed
					if(!model.loanLimitReached()){
						Drawable dialog1 = viewHandler.makeConfirmDialog("This member has already reached their outstanding loan limit"
								+ "\nDo you wish to continue?", this);
						dialog1.draw();
						
						boolean confirmed = Boolean.parseBoolean(dialog1.getFields().get("dialog_input").toString());
						if(!confirmed){
							return;
						}
						
					}
					
					Drawable dialog = viewHandler.makeInputDialog("Enter Barcode", this);
					dialog.draw();
					final String barcode = dialog.getFields().get("dialog_input").toString();
					
					//handles items that are out of stock
					if(!model.isItemInStock(barcode)){
						Drawable dialog1 = viewHandler.makeConfirmDialog("This item is marked as out of stock.\nDo you"
								+ " wish to continue?", this);
						dialog1.draw();
						
						boolean confirmed = Boolean.parseBoolean(dialog1.getFields().get("dialog_input").toString());
						if(!confirmed){
							return;
						}
						
					}
					
					//handles item with hire fee
					double fee = model.getHireFee(barcode);
					if(fee>0){
						viewHandler.displayView("AddIncidental", "HIRE FEE", fee, new Controller() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								try {
									model.newLoan(barcode);
								} catch (FailedValidationException e1) {
									Drawable dialog4 = viewHandler.makeMessageDialog(e1.getMessage(),this);
									dialog4.draw();
								} 
								
							}
						});
						
						
					}else{
						model.newLoan(barcode);
					}
					parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));	
				} catch (FailedValidationException e2){
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				} catch (AccessException e2){
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
				
					
				break;
			default:
				throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
		}

	}

}
