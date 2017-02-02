package com.jasonpilbrough.helper;

public class Main {
	
	
	public static void main(String[] args) {

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
