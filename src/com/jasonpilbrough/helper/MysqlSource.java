package com.jasonpilbrough.helper;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 *
 * @author Jason
 */
public class MysqlSource implements Source{

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
        try {
            ds = new MysqlDataSource();
            ds.setURL("jdbc:mysql://localhost:3307/bookshop_app");
            ds.setUser("java");
            ds.setPassword("user");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    
}
