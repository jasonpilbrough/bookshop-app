package com.jasonpilbrough.view;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.jasonpilbrough.vcontroller.Controller;


/* Dialog boxes are handled like all other views, as a drawable interface
 * Dialog boxes share the controller of the view that generated them, this controller is passed in with
 * the initialize method as usual
 * The draw method activates the dialog which sets the answer field, this field is accessed in the same way
 * other fields are accessed in other views, with the getFields method
 */

public class DialogView implements Drawable {
	
	private Object message;
	private String initialValue;
	private String answer;
	private String type;
	private String title;
	
	//TODO might delete this
	private Controller controller;
	
	public DialogView(String type,String title, Object message) {
		this(type,title, message,"");
	}

	public DialogView(String type,String title, Object message, String initialValue) {
		this.message = message;
		this.initialValue = initialValue;
		this.type = type;
		this.title = title;
	}
	
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) { }

	@Override
	public void initialise(Controller controller) {
		this.controller = controller;

	}

	@Override
	public void draw() {
		switch(type){
		case "input":
			answer = JOptionPane.showInputDialog(null,message, title, JOptionPane.QUESTION_MESSAGE);
			break;
		case "complex":
			JOptionPane optionPane = new JOptionPane(message,JOptionPane.PLAIN_MESSAGE,JOptionPane.DEFAULT_OPTION,null,new Object[]{});
		    JDialog dialog = optionPane.createDialog(null);
		    dialog.setTitle(title);
		    dialog.setVisible(true);
			break;
		case "message":
			 JOptionPane.showMessageDialog(null,message,title, JOptionPane.INFORMATION_MESSAGE);
			break;
		case "confirm":
			int temp = JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
			if(temp==0){
				answer = true+"";
			}else{
				answer = false+"";
			}
			break;
		case "password":
			JPasswordField pass = new JPasswordField();
			int option =  JOptionPane.showConfirmDialog(null,pass,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
			
			if(option == 0) // pressing OK button
			{
			    answer = new String(pass.getPassword());
			}
			break;
			default:
				throw new RuntimeException("Dialog type "+type+" is not recogonised");
				
		}
			
		
		//generates an event that must be handled by the controller of the view that generated this dialog
		//controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "dialog"));

	}

	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		map.put("dialog_input", answer);
		return map;
	}

}
