package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.FailedValidationException;

public class AddShopItemModel {

	private Database db;
	private PropertyChangeSupport changefirer;

	private static final int maxTextLength = 50;
	private static final int minTextLength = 1;
	
	public AddShopItemModel(Database db) {
		super();
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		
	}

	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void addShopItem(String barcode, String title,String author, String type, double costPrice, 
			double sellingPrice,int qty) throws FailedValidationException{
		
		if(barcode.length()>maxTextLength){
			throw new FailedValidationException("Barcode too long");
		}
		if(barcode.length()<minTextLength){
			throw new FailedValidationException("Barcode too short");
		}
		
		if(title.length()>maxTextLength){
			throw new FailedValidationException("Title too long");
		}
		if(title.length()<minTextLength){
			throw new FailedValidationException("Title too short");
		}
		
		if(author.length()>maxTextLength){
			throw new FailedValidationException("Author too long");
		}
		if(author.length()<minTextLength){
			throw new FailedValidationException("Author too short");
		}
		
		db.sql("INSERT INTO shop_items(barcode, title, author,media_type,cost_price, selling_price, quantity) "
				+ "VALUES('?', '?', '?', '?', ?, ?, ?)")
					.set(barcode)
					.set(title)
					.set(author)
					.set(type)
					.set(costPrice)
					.set(sellingPrice)
					.set(qty)
					.update();
		changefirer.firePropertyChange("close", null, null);
	}
	
	public void addToStock(String barcode, int qty){
		db.sql("UPDATE shop_items SET quantity = quantity + ? WHERE barcode = '?' LIMIT 1")
			.set(qty)
			.set(barcode)
			.update();
		changefirer.firePropertyChange("close", null, null);
	}
	
	public void setAsItemExists(){
		changefirer.firePropertyChange("item_exists", null, null);
	}
	
	public void setAsItemNew(){
		changefirer.firePropertyChange("item_new", null, null);
	}
	
	public boolean itemExists(String barcode){
		Map map = db.sql("SELECT id FROM shop_items WHERE barcode = '?' LIMIT 1")
				.set(barcode)
				.retrieve();
		return map.get("id")!=null;
	}
	
}

