package com.jasonpilbrough.helper;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import javax.sql.DataSource;

/**
 *
 * @author Jason
 */
public interface Source {
    
    public DataSource get();
    
}
