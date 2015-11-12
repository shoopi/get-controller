package main.java.nl.tue.ieis.get.data;

import java.sql.Connection;
import java.sql.DriverManager;
import org.zkoss.zk.ui.Sessions;

public class DBConnection {

	public final static String driverClassName = "org.sqlite.JDBC";
	public final static String sqlitelUrl = "jdbc:sqlite:";
	public final static String dbLocation = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/getService.db");
	//private static final String dbName = "getService.db";
	
	public static Connection getConnection()
	  {
	    Connection conn = null;
	    try {
	      Class.forName(driverClassName);
	      System.out.println(sqlitelUrl + dbLocation);
	      conn = DriverManager.getConnection(sqlitelUrl + dbLocation);
	      System.out.println("Connected to SQLite Database");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    return conn;
	  }
}