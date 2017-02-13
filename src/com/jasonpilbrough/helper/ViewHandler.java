package com.jasonpilbrough.helper;

import javax.swing.JComponent;

import com.jasonpilbrough.model.AboutModel;
import com.jasonpilbrough.model.AddBugReportModel;
import com.jasonpilbrough.model.AddIncidentalModel;
import com.jasonpilbrough.model.AddLibraryItemModel;
import com.jasonpilbrough.model.AddMemberModel;
import com.jasonpilbrough.model.AddPurchaseModel;
import com.jasonpilbrough.model.AddShopItemModel;
import com.jasonpilbrough.model.AddUserModel;
import com.jasonpilbrough.model.ApplicationModel;
import com.jasonpilbrough.model.ApplicationSettingsModel;
import com.jasonpilbrough.model.CashUpModel;
import com.jasonpilbrough.model.ConsoleModel;
import com.jasonpilbrough.model.HeaderModel;
import com.jasonpilbrough.model.LoginModel;
import com.jasonpilbrough.model.MemberModel;
import com.jasonpilbrough.model.MembersModel;
import com.jasonpilbrough.model.SaleModel;
import com.jasonpilbrough.model.SalesReportModel;
import com.jasonpilbrough.model.SearchModel;
import com.jasonpilbrough.model.UserSettingsModel;
import com.jasonpilbrough.model.UsersModel;
import com.jasonpilbrough.model.WelcomeModel;
import com.jasonpilbrough.vcontroller.AboutController;
import com.jasonpilbrough.vcontroller.AddBugReportController;
import com.jasonpilbrough.vcontroller.AddIncidentalController;
import com.jasonpilbrough.vcontroller.AddLibraryItemController;
import com.jasonpilbrough.vcontroller.AddMemberController;
import com.jasonpilbrough.vcontroller.AddPurchaseController;
import com.jasonpilbrough.vcontroller.AddShopItemController;
import com.jasonpilbrough.vcontroller.AddUserController;
import com.jasonpilbrough.vcontroller.ApplicationController;
import com.jasonpilbrough.vcontroller.ApplicationSettingsController;
import com.jasonpilbrough.vcontroller.CashUpController;
import com.jasonpilbrough.vcontroller.ConsoleController;
import com.jasonpilbrough.vcontroller.Controller;
import com.jasonpilbrough.vcontroller.HeaderController;
import com.jasonpilbrough.vcontroller.LoginController;
import com.jasonpilbrough.vcontroller.ManageUsersController;
import com.jasonpilbrough.vcontroller.MemberController;
import com.jasonpilbrough.vcontroller.MembersController;
import com.jasonpilbrough.vcontroller.MenuController;
import com.jasonpilbrough.vcontroller.SaleController;
import com.jasonpilbrough.vcontroller.SalesReportController;
import com.jasonpilbrough.vcontroller.SearchController;
import com.jasonpilbrough.vcontroller.UserSettingsController;
import com.jasonpilbrough.vcontroller.WelcomeController;
import com.jasonpilbrough.view.AboutView;
import com.jasonpilbrough.view.AddBugReportView;
import com.jasonpilbrough.view.AddIncidentalView;
import com.jasonpilbrough.view.AddLibraryItemView;
import com.jasonpilbrough.view.AddMemberView;
import com.jasonpilbrough.view.AddShopItemView;
import com.jasonpilbrough.view.AddUserView;
import com.jasonpilbrough.view.ApplicationSettingsView;
import com.jasonpilbrough.view.ApplicationView;
import com.jasonpilbrough.view.CashUpView;
import com.jasonpilbrough.view.ConsoleView;
import com.jasonpilbrough.view.DialogView;
import com.jasonpilbrough.view.Drawable;
import com.jasonpilbrough.view.HeaderView;
import com.jasonpilbrough.view.LoginView;
import com.jasonpilbrough.view.ManageUsersView;
import com.jasonpilbrough.view.MemberView;
import com.jasonpilbrough.view.MembersView;
import com.jasonpilbrough.view.MenuView;
import com.jasonpilbrough.view.SaleView;
import com.jasonpilbrough.view.SalesReportView;
import com.jasonpilbrough.view.SearchView;
import com.jasonpilbrough.view.UserSettingsView;
import com.jasonpilbrough.view.WelcomeView;


/* Class responsible for providing views to the rest of the application
 * Calling 'displayView' will cause the desired view to initialise AND paint itself
 * Calling 'displayView' will return a JComponent which is useful for views (usually of type JPanel) 
 * that are embedded in other views, and allow the parent view to display them
 * Non-embedded view (usually of type JFrame) will return null when 'displayView' is called
 */
public class ViewHandler {
	
	private Database db;
	private AccessManager am;
	
	//TODO dont like having to do this
	private MembersController membersController;
	private ApplicationController appController;
	private ManageUsersController usersController;
	//TODO do NOT like this at all
	private HeaderModel headerModel = new HeaderModel();
	
	public ViewHandler(Database db, AccessManager am) {
		super();
		this.db = db;
		this.am = am;
	}

	public JComponent displayView(String name){
		switch(name){
		case "Application":
			ApplicationView av = new ApplicationView(this);
			ApplicationModel apm = new ApplicationModel();
			appController = new ApplicationController(this, apm, av,am);
			appController.initController();
			break;
		case "LoginView":
			LoginView lv = new LoginView();
			LoginController lc = new LoginController(this,appController, new LoginModel(db,am), lv);
			return lv;
		case "WelcomeView":
			WelcomeView wv = new WelcomeView();
			WelcomeController wc = new WelcomeController(appController, wv, new WelcomeModel(db, am));
			return wv;
		case "MenuBar":
			MenuView menu = new MenuView();
		    MenuController controller2 = new MenuController(appController,this, menu,am);
			return menu;
		case "MembersView":
			MembersModel mm = new MembersModel(db);
			MembersView mv = new MembersView();
			membersController = new MembersController(this,mm,mv);
			return mv;
		case "AddMember":
			AddMemberController amc = new AddMemberController(membersController, this, 
					new AddMemberModel(db), new AddMemberView());
			break;
		case "AddLibraryItem":
			AddLibraryItemController alic = new AddLibraryItemController(this, new AddLibraryItemModel(db)
					, new AddLibraryItemView());
			break;
		case "AddShopItem":
			AddShopItemController asic = new AddShopItemController(this,new AddShopItemModel(db), 
					new AddShopItemView());
			break;
		case "AddIncidental":
			AddIncidentalView view = new AddIncidentalView();
			AddIncidentalController aic = new AddIncidentalController(this, new AddIncidentalModel(db),
					view);

			Drawable dialog = new DialogView("complex","Add Other Income", view);
			dialog.draw();
		    break;
		case "AddPurchase":
			AddIncidentalView apv = new AddIncidentalView();
			AddPurchaseController apc = new AddPurchaseController(this, new AddPurchaseModel(db),
					apv);
			Drawable dialog2 = new DialogView("complex","Add Purchase", apv);
			dialog2.draw();
			break;
		case "AddUser":
			AddUserController auc = new AddUserController(usersController,this, new AddUserModel(db)
					, new AddUserView());
			break;
		case "ManageUsers":
			usersController= new ManageUsersController(this, new UsersModel(db), 
					new ManageUsersView());
			break;
		case "CashUp":
			CashUpController cuc= new CashUpController(this, new CashUpModel(db,am), 
					new CashUpView());
			break;
		case "SalesReport":
			SalesReportController src= new SalesReportController(this, new SalesReportModel(db), 
					new SalesReportView());
			break;
		case "AboutView":
			AboutController ac= new AboutController(this,new AboutModel(db), 
					new AboutView());
			break;
		case "ApplicationSettingsView":
			ApplicationSettingsController asc= new ApplicationSettingsController(this,new ApplicationSettingsModel(db,am), 
					new ApplicationSettingsView());
			break;
		case "UserSettingsView":
			UserSettingsController usc= new UserSettingsController(this,new UserSettingsModel(db,am), 
					new UserSettingsView());
			break;
		case "AddBugReportView":
			AddBugReportController abrc= new AddBugReportController(this,new AddBugReportModel(db,am), 
					new AddBugReportView());
			break;
		case "ConsoleView":
			ConsoleModel stm = new ConsoleModel(db);
			Logger.setConsoleModel(stm);
			ConsoleController stc= new ConsoleController(this,stm, 
					new ConsoleView());
			break;
		default:
			throw new RuntimeException("View " + name + " is not registered with view handler OR wrong view"
					+ "handler method used");
				
		}
		return null;
	}
	

	
	
	public JComponent displayView(String name, int id){
		switch(name){
		case "MemberView":
			MemberView view = new MemberView();
			MemberController mc = new MemberController(membersController, this,
					new MemberModel(db, id,am), view, am);
			break;
		default:
			throw new RuntimeException("View " + name + " is not registered with view handler OR wrong view"
					+ "handler method used");
				
		}
		return null;
	}
	
	public JComponent displayView(String name, String param){
		switch(name){
		case "SearchView":
			SearchController sc = new SearchController(this, new SearchModel(db,param, am), 
					new SearchView(),am);
			break;
		case "HeaderView":
			HeaderView hv = new HeaderView();
			HeaderController headerController = new HeaderController(appController, headerModel, hv,param);
			return hv;
		case "SaleView":
			SaleModel sm = new SaleModel(db);
			SaleView sv = new SaleView();
			SaleController sc2 = new SaleController(this,sm,sv,Boolean.parseBoolean(param));
			return sv;
		default:
			throw new RuntimeException("View " + name + " is not registered with view handler OR wrong view"
					+ "handler method used");
				
		}
		return null;
	}
	
	public JComponent displayView(String name, String type,double amount, Controller parent){
		switch(name){
		case "AddIncidental":
			AddIncidentalView view = new AddIncidentalView();
			AddIncidentalController aic = new AddIncidentalController(this, new AddIncidentalModel(db,type,amount),
					view, parent);
			
			Drawable dialog = new DialogView("complex","Add Other Income", view);
			dialog.draw();
			
			
		    break;
		default:
			throw new RuntimeException("View " + name + " is not registered with view handler OR wrong view"
					+ "handler method used");
				
		}
		return null;
		
	}
	
	public Drawable makePasswordDialog(Controller c){
		Drawable d = new DialogView("password","Enter current password","");
		d.initialise(c);
		return d;
	}
	
	public Drawable makeInputDialog(String message,String title, Controller c){
		Drawable d = new DialogView("input",title,message);
		d.initialise(c);
		return d;
	}
	
	
	public Drawable makeConfirmDialog(String message,String title, Controller c){
		Drawable d = new DialogView("confirm", title, message);
		d.initialise(c);
		return d;
	}
	
	public Drawable makeMessageDialog(String message,String title, Controller c){
		Drawable d = new DialogView("message",title, message);
		d.initialise(c);
		return d;
	}
	public Drawable makeMessageDialog(String message, String title){
		Drawable d = new DialogView("message",title, message);
		return d;
	}

}
