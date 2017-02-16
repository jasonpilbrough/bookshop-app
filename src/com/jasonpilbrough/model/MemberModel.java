package com.jasonpilbrough.model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jasonpilbrough.helper.AccessException;
import com.jasonpilbrough.helper.AccessManager;
import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.DateInTime;
import com.jasonpilbrough.helper.FailedValidationException;
import com.jasonpilbrough.tablemodel.AccessControlledTableModel;
import com.jasonpilbrough.tablemodel.CachedTableModel;
import com.jasonpilbrough.tablemodel.LoansTableModel;
import com.jasonpilbrough.tablemodel.ValidatedTableModel;

/* Models a single member in the application.
 * Any changes to model (ie. calling set methods) cause the PropertyChangeSupport to fire event
 * containing details of the change. Any view that has been added as a listener, using the 'addListener'
 * method with be notified of this change. This model also stores the state of the view, (eg. if it is
 * editable or not)
 *
 */

public class MemberModel implements TableModelListener{

	private Database db;
	private AccessManager am;
	private int id;
	private PropertyChangeSupport changefirer;
	
	private boolean editable;
	private TableModel tableModel;
	
	private static final int maxNameLength = 50;
	private static final int minNameLength = 1;
	private static final int reqPhoneLength = 10;
	
	
	public MemberModel(Database db, int id, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
		this.id = id;
		changefirer = new PropertyChangeSupport(this);
		setTable();
		
	}
	
	public void addListener(PropertyChangeListener listener){
		changefirer.addPropertyChangeListener(listener); 
	}
	
	//Fires a change on all values to update all views
	public void setAllValues(){
		setTable();
		editable = false;
		changefirer.firePropertyChange("editable", "", false);	
		//changefirer.firePropertyChange("id", "", id);
		changefirer.firePropertyChange("name", "", getName());
		changefirer.firePropertyChange("phone_number", "", getPhoneNumber());
		changefirer.firePropertyChange("expire_date", "", getExpireDate());
	}
	
	private void setTable(){
		tableModel = new AccessControlledTableModel(new LoansTableModel(db,id)
				, am, "ELOP");
		tableModel.addTableModelListener(this);
		changefirer.firePropertyChange("loans_table_model", "", tableModel);
	}
	
	
	public void setEditable(boolean val){
		boolean oldval = editable;
		editable = val;
		changefirer.firePropertyChange("editable", oldval, val);
	}
	
	public void setName(String name) throws FailedValidationException{
		if(name.length()>maxNameLength){
			throw new FailedValidationException("Name too long");
		}
		if(name.length()<minNameLength){
			throw new FailedValidationException("Name too short");
		}
		String oldval = getName();
		db.sql("UPDATE members SET name= '?' WHERE id = ? LIMIT 1")
					.set(name)
					.set(id)
					.update();
		changefirer.firePropertyChange("name", oldval, name);
	}
	
	public void setPhoneNumber(String number) throws FailedValidationException{
		if(number.length()>reqPhoneLength){
			throw new FailedValidationException("Phone number too long");
		}
		if(number.length()<reqPhoneLength){
			throw new FailedValidationException("Phone number too short");
		}
		
		for (int i = 0; i < number.length(); i++) {
			if(number.charAt(i) < 48 ||number.charAt(i) > 57){
				throw new FailedValidationException("Phone number wrong format");
			}
		}
		
		String oldval = getPhoneNumber();
		db.sql("UPDATE members SET phone_number= '?' WHERE id = ? LIMIT 1")
					.set(number)
					.set(id)
					.update();
		changefirer.firePropertyChange("phone_number", oldval, number);
	}
	
	public void returnLoan(int id){
		db.sql("DELETE FROM loans WHERE id=? LIMIT 1")
					.set(id)
					.update();
		setTable();
	}
	
	
	public void extendLoan(int id){
		
		DateInTime date = new DateInTime(db.sql("SELECT date_due FROM loans WHERE id = ?")
				.set(id)
				.retrieve()
				.get("date_due").toString());
		
		String val = db.sql("SELECT value FROM settings WHERE name='loan_extension' LIMIT 1")
				.retrieve().get("value").toString();
		
		db.sql("UPDATE loans SET date_due='?' WHERE id=? LIMIT 1")
					.set(date.addWeeks(Integer.parseInt(val)))
					.set(id)
					.update();
		setTable();
	}
	
	public void newLoan(String barcode) throws FailedValidationException{
	
		
		Map map = db.sql("SELECT title FROM library_items WHERE barcode= '?' LIMIT 1")
							.set(barcode)
							.retrieve();;

		if(map.get("title")==null){
			throw new FailedValidationException("Item does not exist");
		}
		
		String val = db.sql("SELECT value FROM settings WHERE name='loan_duration' LIMIT 1")
				.retrieve().get("value").toString();
		
		db.sql("INSERT INTO loans(member_id,library_item_id,date_issued,date_due) "
				+ "VALUES(?,(SELECT id FROM library_items WHERE barcode= '?' LIMIT 1), '?','?')")
					.set(id)
					.set(barcode)
					.set(new DateInTime().toString())
					.set(new DateInTime().addWeeks(Integer.parseInt(val)).toString())
					.update();
		setTable();
	}
	
	public void renewMembership(){
		
		DateInTime oldval = getExpireDate();
		
		String val = db.sql("SELECT value FROM settings WHERE name='membership_duration' LIMIT 1")
				.retrieve().get("value").toString();
		
		//if membership has expired, set new expire date 1 year from now, else add one year to membership
		DateInTime newval = null;
		if(oldval.compareTo(new DateInTime())<0){
			 newval = new DateInTime().addYears(Integer.parseInt(val));
		}else{
			 newval = oldval.addYears(Integer.parseInt(val));
		}
		
		db.sql("UPDATE members SET expire_date= '?' WHERE id = ? LIMIT 1")
					.set(newval.toString())
					.set(id)
					.update();
		changefirer.firePropertyChange("expire_date", oldval, newval);
	}
	
	public void deleteMember(){
		db.sql("DELETE FROM members WHERE id=? LIMIT 1")
					.set(id)
					.update();
		changefirer.firePropertyChange("close", null, null);
	}
	
	public double getFine(int id){
		DateInTime date = new DateInTime(db.sql("SELECT date_due FROM loans WHERE id = ?")
				.set(id)
				.retrieve()
				.get("date_due").toString());
		
		
		int weeks = date.diffInWeeks(new DateInTime());
		if(weeks<0){
			return 0;
		}else{
			String val = db.sql("SELECT value FROM settings WHERE name='fine' LIMIT 1")
			.retrieve().get("value").toString();
			double fine = Double.parseDouble(val);
			return weeks*fine;
		}
		
		
	}
	
	public double getHireFee(String barcode) throws FailedValidationException{
		Map map = db.sql("SELECT hire_price FROM library_items WHERE barcode = ? LIMIT 1")
				.set(barcode)
				.retrieve();

		
		if(map.get("hire_price")==null){
			throw new FailedValidationException("Item does not exist");
		}
		return (double)map.get("hire_price");
	}
	
	public boolean isItemInStock(String barcode) throws FailedValidationException{
		Map map = db.sql("SELECT CAST(quantity AS SIGNED) - COUNT(loans.id) AS diff FROM library_items "
				+ "LEFT JOIN loans ON loans.library_item_id = library_items.id WHERE barcode = '?' GROUP BY barcode")
				.set(barcode)
				.retrieve();
		
		if(map.get("diff")==null)
			throw new FailedValidationException("Item doesn't exist");
		
		return (int)(long)map.get("diff") > 0;
	}
	
	public boolean loanLimitReached(){
		int num = (int)(long)db.sql("SELECT COUNT(*) AS num FROM loans WHERE member_id = ?")
				.set(id)
				.retrieve()
				.get("num");
		
		String val = db.sql("SELECT value FROM settings WHERE name='loan_cap' LIMIT 1")
				.retrieve().get("value").toString();
		int loanLimit = Integer.parseInt(val);
		return num<loanLimit;
	}
	
	public boolean hasMembershipExpired(){
		int ans = getExpireDate().compareTo(new DateInTime());
		
		return ans < 0;
	}
	
	public double getMembershipFee(){
		String val = db.sql("SELECT value FROM settings WHERE name='membership_fee' LIMIT 1")
				.retrieve().get("value").toString();
		return Double.parseDouble(val);
	}
	
	
	
	private String getName(){
		return db.sql("SELECT name FROM members WHERE id = ?")
					.set(id)
					.retrieve()
					.get("name").toString();	
	}
	
	private String getPhoneNumber(){
		return db.sql("SELECT phone_number FROM members WHERE id = ?")
					.set(id)
					.retrieve()
					.get("phone_number").toString();
	}
	
	private DateInTime getExpireDate(){
		return new DateInTime(db.sql("SELECT expire_date FROM members WHERE id = ?")
					.set(id)
					.retrieve()
					.get("expire_date").toString());
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		changefirer.firePropertyChange("loans_table_model", "", tableModel);
		
	}
	
	
	
	
}
