package com.jasonpilbrough.helper;

public class Main {
	
	
	public static void main(String[] args) {
		
		Logger logger = new Logger();

		Database db = new Database(new MysqlSource().get());
		AccessManager am = new AccessManager(db);
		
		logger.setAccessManager(am);
		
		ViewHandler viewHandler = new ViewHandler(db,am);
		
		viewHandler.displayView("Application");
		
		logger = new Logger();
		logger.setAccessManager(am);
		
	
	}

}
