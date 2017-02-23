package com.jasonpilbrough.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.tablemodel.CartTableModel;

public class SaleModel {

	
	private Database db;
	private PropertyChangeSupport changefirer;
	private TableModel tableModel;
	private List<String[]> cart;
	
	public SaleModel(Database db) {
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		cart = new ArrayList<>();
	}

	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener);
	}
	
	//Fires a change on all values to update all views
	public void setAllValues(boolean saleTransaction){
		cart = new ArrayList<>();
		tableModel = new CartTableModel(cart);
		changefirer.firePropertyChange("barcode", null, "");
		changefirer.firePropertyChange("preview", null, "<item does not exist>");
		changefirer.firePropertyChange("price", null, 0.0);
		changefirer.firePropertyChange("qty", null, 1);
		changefirer.firePropertyChange("table_model", null, tableModel);
		changefirer.firePropertyChange("total", null, getTotal());
		if(saleTransaction)
			changefirer.firePropertyChange("sale_transaction", null, "");
		else
			changefirer.firePropertyChange("refund_transaction", null, "");
	}
	
	public double getTotal(){
		double total = 0;
		for (String[] strings : cart) {
			total += Double.parseDouble(strings[1])*Double.parseDouble(strings[2]);
		}
		return total;
		
	}
	
	public int getCartSize(){
		return cart.size();
	}
	
	public void setPreview(String barcode){
		Map<String,Object> map = db.sql("SELECT title, author, selling_price "
						+ "FROM shop_items WHERE barcode = '?' LIMIT 1")
						.set(barcode)
						.retrieve();
		if(map.size()>0){
			String preview = String.format("%s  (%s)", trim(map.get("title").toString()),trim(map.get("author").toString()));
			changefirer.firePropertyChange("preview", null, preview);
			changefirer.firePropertyChange("price", null, (double)map.get("selling_price"));
		}else{
			changefirer.firePropertyChange("price", null, 0.0);
			changefirer.firePropertyChange("preview", null, "<item does not exist>");
		}
	}
	
	private String trim(String val){
		int thresh = 20;
		
		if(val.length()>thresh){
			return val.substring(0, thresh-3)+ "...";
		}else{
			return val;
		}
		
	}
	
	public void addToCart(String barcode, double price, boolean saleTransacion, int qty) throws FailedValidationException{
		if(price<0){
			throw new FailedValidationException("Price must be positive");
		}
		
		Map map = db.sql("SELECT title FROM shop_items WHERE barcode = '?' LIMIT 1")
						.set(barcode)
						.retrieve();
		
		if(map.get("title")==null){
			throw new FailedValidationException("Item does not exist");
		}
		String title = map.get("title").toString();
		
		cart.add(new String[]{title,price*(saleTransacion?1:-1)+"",qty+"", saleTransacion+""});
		tableModel = new CartTableModel(cart);
		changefirer.firePropertyChange("table_model", null, tableModel);
		changefirer.firePropertyChange("barcode", null, "");
		changefirer.firePropertyChange("price", null, "");
		changefirer.firePropertyChange("preview", null, "<item does not exist>");
		changefirer.firePropertyChange("qty", null, 1);
		changefirer.firePropertyChange("total", null, getTotal());
		changefirer.firePropertyChange("sale_transaction", null, "");
		
	}
	public void checkout(String paymentMethod){
		for (String[] strings : cart) {
			//TODO doing reverse search on title like this may cause problems
			db.sql("INSERT INTO sales(shop_item_id, price_per_unit_sold, quantity_sold, sale_date, payment)"
					+ "VALUES((SELECT id FROM shop_items WHERE title = '?' LIMIT 1) , ? , ? , '?','?')")
					.set(strings[0])
					.set(strings[1])
					.set(strings[2])
					.set(new DateInTime())
					.set(paymentMethod)
					.update();
			
			int num = (int) Math.signum(Double.parseDouble(strings[1]));
			if(num==0 && Boolean.parseBoolean(strings[3]))
				num=1;
			else if (num==0 && !Boolean.parseBoolean(strings[3]))
				num = -1;
			
			db.sql("UPDATE shop_items SET quantity = IF(CAST(quantity AS signed)- 1 < 0, 0, quantity - 1) WHERE title = '?' LIMIT 1")
				.set(Integer.parseInt(strings[2]) * num)
				.set(strings[0])
				.update();
			
			cart = new ArrayList<>();
			tableModel = new CartTableModel(cart);
			changefirer.firePropertyChange("table_model", null, tableModel);
			changefirer.firePropertyChange("total", null, getTotal());
		}
	}
	
	public boolean isItemInStock(String barcode) throws FailedValidationException{
		Map map =db.sql("SELECT quantity FROM shop_items WHERE barcode = '?' LIMIT 1")
				.set(barcode)
				.retrieve();
		
		if(map.get("quantity")==null)
			throw new FailedValidationException("Item doesn't exist");
		
		return (int)(long)map.get("quantity") > 0;
	}
	
	
}
