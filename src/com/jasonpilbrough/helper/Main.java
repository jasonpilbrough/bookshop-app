package com.jasonpilbrough.helper;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	
	
	public static void main(String[] args) {
		//TODO this should not be here
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		SettingsFile settingsFile = new SettingsFile("res/settings.txt");
		Logger logger = new Logger(settingsFile);

		Database db = new Database(new MysqlSource(settingsFile).get());
		AccessManager am = new AccessManager(db);
		
		logger.setAccessManager(am);
		
		ViewHandler viewHandler = new ViewHandler(db,am);
		
		viewHandler.displayView("Application");
		
		//to handle all new threads created
		logger = new Logger(settingsFile);
		logger.setAccessManager(am);
		
	
	}

}
