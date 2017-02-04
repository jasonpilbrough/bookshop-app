package com.jasonpilbrough.helper;


import java.io.FileNotFoundException;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 *
 * @author Jason
 */
public class MysqlSource implements Source{
	
	private final SettingsFile settingsFile;

    public MysqlSource(SettingsFile settingsFile) {
		super();
		this.settingsFile = settingsFile;
	}

	@Override
    public DataSource get(){
       
        /*HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3307/bookshop_app");
        config.setUsername("java");
        config.setPassword("user");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;*/
        //Properties props = new Properties();
        //FileInputStream fis = null;
        
    	MysqlDataSource ds = null;
       
            ds = new MysqlDataSource();
            try {
				ds.setURL(settingsFile.getSettings().get("url"));
				ds.setUser(settingsFile.getSettings().get("user"));
	            ds.setPassword(settingsFile.getSettings().get("password"));
			} catch (FileNotFoundException e) {
				throw new LogException(e);
			}
            
            
        
        return ds;
    }
    
}
