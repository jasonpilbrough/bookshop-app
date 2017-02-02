package com.jasonpilbrough.vcontroller;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.model.AddMemberModel;
import com.jasonpilbrough.view.Drawable;

public class AddMemberController extends Controller {

	private AddMemberModel model;
	private Drawable view;
	private ViewHandler viewHandler;
	private Controller parent;
	private static Object modalMonitor = new Object();

	public AddMemberController(Controller parent, ViewHandler viewHandler,AddMemberModel model, Drawable view) {
		this.model = model;
		this.view = view;
		this.viewHandler = viewHandler;
		this.parent = parent;
		
		model.addListener(view);
		view.initialise(this);
		view.draw();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "confirm":
			if(Boolean.parseBoolean(view.getFields().get("defered").toString())){
				try {
					model.addMember(view.getFields().get("name").toString(), view.getFields().get("phone_number").toString(),false);
					parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));	
					Drawable dialog4 = viewHandler.makeMessageDialog("Member successfully added",this);
					dialog4.draw();
				} catch (FailedValidationException e2) {
					Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
					dialog4.draw();
				}
				
			}else{
				viewHandler.displayView("AddIncidental", "MEMBERSHIP FEE",model.getMembershipFee(),new Controller() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						switch(e.getActionCommand()){
						case "confirm":
							try {
								model.addMember(view.getFields().get("name").toString(), view.getFields().get("phone_number").toString(),true);
								parent.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));	
								//dialog.setVisible(false);
								Drawable dialog4 = viewHandler.makeMessageDialog("Member successfully added",this);
								dialog4.draw();
							} catch (FailedValidationException e2) {
								Drawable dialog4 = viewHandler.makeMessageDialog(e2.getMessage(),this);
								dialog4.draw();
							}
							break;
						default:
							throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
						}
						
					}
				});
			   
				
			}
			
			break;
			
		default:
			throw new RuntimeException("Command "+e.getActionCommand()+" not registered with controller");
	}

	}

}
