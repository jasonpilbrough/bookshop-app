package com.jasonpilbrough.view;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.jasonpilbrough.helper.SmartJFrame;
import com.jasonpilbrough.helper.ViewHandler;
import com.jasonpilbrough.vcontroller.Controller;

import net.java.dev.designgridlayout.DesignGridLayout;

public class ApplicationView extends SmartJFrame implements Drawable{
	
	private ViewHandler viewHandler;
	private String context;
	
	public ApplicationView(ViewHandler viewHandler){
		this.viewHandler = viewHandler;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()){
		case "context":
			context = evt.getNewValue().toString();
			draw();
			break;
			
		default:
			throw new RuntimeException("Property " + evt.getPropertyName() + " not registered with view");
		}
		
	}
	
	public void initialise(final Controller controller){
		setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
       
       setVisible(true);
       setBounds(0, 0, 540, 540);
       
      
       setTitle("Highway Christian Community Bookshop");
       this.addWindowListener(new WindowListener() {
		
		@Override
		public void windowOpened(WindowEvent e) {}
		@Override
		public void windowIconified(WindowEvent e) {}
		@Override
		public void windowDeiconified(WindowEvent e) {}
		@Override
		public void windowDeactivated(WindowEvent e) {}
		@Override
		public void windowClosing(WindowEvent e) {
			 controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "window closing"));
			
		}
		@Override
		public void windowClosed(WindowEvent e) {}
		@Override
		public void windowActivated(WindowEvent e) {}
	});

       //init command from view causes the model to push all values view
       controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "init"));
       
	}
	
	
	public void draw(){
				
		//MenuBar bar = new MenuBar(db);
    	//bar.initialise();
    	//setJMenuBar(bar);
    	
		JPanel parent = new JPanel();
		
		DesignGridLayout layout = new DesignGridLayout(parent);
		if(!context.equalsIgnoreCase("welcome")){
			
		}
		
		if(context.equalsIgnoreCase("login")){
			setJMenuBar(new JMenuBar());
			layout.row().grid().add(viewHandler.displayView("LoginView"));
		}else if(context.equalsIgnoreCase("library")){
			setJMenuBar((JMenuBar) viewHandler.displayView("MenuBar"));
			layout.row().grid().add(viewHandler.displayView("HeaderView","library"));
			layout.row().grid().add(viewHandler.displayView("MembersView"));
		}else if(context.equalsIgnoreCase("store")){
			setJMenuBar((JMenuBar) viewHandler.displayView("MenuBar"));
			layout.row().grid().add(viewHandler.displayView("HeaderView","store"));
			layout.row().grid().add(viewHandler.displayView("SaleView","true"));
		}else if(context.equalsIgnoreCase("store_r")){
			setJMenuBar((JMenuBar) viewHandler.displayView("MenuBar"));
			layout.row().grid().add(viewHandler.displayView("HeaderView","store"));
			layout.row().grid().add(viewHandler.displayView("SaleView","false"));
		}
		else if (context.equalsIgnoreCase("welcome")){
			setJMenuBar((JMenuBar) viewHandler.displayView("MenuBar"));
			layout.row().grid().add(viewHandler.displayView("WelcomeView"));
		}
		
		getContentPane().removeAll();
        getContentPane().add(parent);
        
        revalidate();
        repaint();
	}
	
	@Override
	public Map<String, Object> getFields() {
		Map<String,Object> map = new HashMap<>();
		return map;
	}
	

	

}
