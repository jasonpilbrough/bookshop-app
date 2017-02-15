package com.jasonpilbrough.helper;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;


/* Provides database interaction for the application. After initialization method 'sql' is 
 * called to set the query or update statement. Question marks can be used in place of actual
 * parameters. The 'set' method allows each parameter to be set one after each other. If any 
 * parameters are not set when the command is executed then an error is thrown
 * 
 */

public class Database {
    private final DataSource src;
    private final String query;
    
    private Connection conn;
    private Statement statement;
    private static int connections = 0;
    public static int saves = 0;

    public Database(DataSource src) {
        this(src,"");
    }
    
    public Database(DataSource src, String query){
        this.src = src;
        this.query = query;
        
    }
    
    //TODO remove - for dev purposes, also remove all counts
    public static void print(){
    	if(connections%100==0||saves%1000==0){
    		//System.out.println("CONNECTIONS "+(connections)+"  SAVES "+saves);
    	}
    }
    
    //used for returning more than one column and row at the same time
    public List<List<Object>> retrieve2D(){
    	if(query.contains("?")){
            throw new IllegalStateException("Query string still has unset parameters");
        }
        connections++;
        print();
        ResultSet rs = null;
        
        try {
			conn =  src.getConnection();
			statement = conn.createStatement();
			rs =  statement.executeQuery(query);
			int numCols = rs.getMetaData().getColumnCount();
			List<List<Object>> data = new ArrayList<>();
			while(rs.next()){
				List<Object> temp = new ArrayList<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					temp.add(rs.getObject(i));
				}
				data.add(temp);
			}
			return data;
		} catch (SQLException e) {
            System.out.println(query);
			throw new LogException(e);
		} finally {
        	try {
        		if(rs!=null)
        			rs.close();
        		if(statement!=null)
        			statement.close();
        		if(conn!=null)
        			conn.close();
    		} catch (SQLException e) {
    			 throw new LogException(e);
    		}
		}
    
    }
    
    public Map<String,Object> retrieve(){
        if(query.contains("?")){
            throw new IllegalStateException("Query string still has unset parameters");
        }
        connections++;
        print();
        ResultSet rs = null;
        try {
            conn =  src.getConnection();
            statement = conn.createStatement();
            rs =  statement.executeQuery(query);
            
            Map<String,Object> map = new HashMap<>();
            if(!rs.next()){
            	return map;
            }
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            	Object ob = rs.getObject(i);
            	if(ob!=null){
            		map.put(rs.getMetaData().getColumnLabel(i), ob);
            	}
				
			}
            
            if(rs.next()){
            	if(rs.getMetaData().getColumnCount()>1)
            		throw new RuntimeException("This method should not be used where the result set spans more than one row AND more than one column");
            	rs.beforeFirst();
            	map = new HashMap<>();
            	List<Object> list = new ArrayList<>();
            	while(rs.next()){
            		list.add(rs.getObject(1));
            	}
            	map.put("array", list);
            }
            
            return map;
        } catch (SQLException ex) {
            System.out.println(query);
            throw new LogException(ex);
        }finally {
        	try {
        		if(rs!=null)
        			rs.close();
        		if(statement!=null)
        			statement.close();
        		if(conn!=null)
        			conn.close();
    		} catch (SQLException e) {
    			 throw new LogException(e);
    		}
		}
    }
    
    public void update(){
        if(query.contains("?")){
            throw new IllegalStateException("Query string still has unset parameters");
        }
       // System.out.println("CONNECTION "+count++);
        try {
            conn =  src.getConnection();
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println(query);
            throw new LogException(ex);
        }finally {
        	try {
        		if(statement!=null)
        			statement.close();
        		if(conn!=null)
        			conn.close();
    		} catch (SQLException e) {
    			 throw new LogException(e);
    		}
		}
    }
    

    public Database sql(String sql) {
       return new Database(src, sql);
    }

    public Database set(Object o) {
        return new Database(src, query.replaceFirst("\\?", o.toString()));
    }
    
    public String getQuery(){
    	return query;
    }
    
}
