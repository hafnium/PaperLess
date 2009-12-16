package org.sumerit.paperless.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.io.Writable;

public class ReceiptProcessingServer extends ProcessingServer 
{
	/*
	public native String ocrImage(String imageFilename);
	
	static {
		System.load("/af3/sma2t/local/ocrConnector.so");
	}	
	*/
	
	// MySql Variables
	private Statement stmt;
	private ResultSet rs;
	private String url = "jdbc:mysql://76.120.194.253:3306/Paperless";
	private String user = "sean";
	private String pass = "test";
	private Connection con = null;
	
	public ReceiptProcessingServer(InternetConnector connector) 
	{
		super(connector);
	}

	private static final String[] services = {"processReceipt"};
	
	public boolean checkAvailableTypes(String query) 
	{
		if (Arrays.binarySearch(services, query) < 0)
			return false;
		else
			return true;
	}

	public Writable execute(String proc, String args) 
	{
		String[] argv = args.split("\n");
		if (proc.compareTo("processReceipt") == 0)
		{
			return this.processReceipt(argv[0]);
		}
		
		return new StringWritable("ERROR: Could not execute request, please check arguments");
	}
	
	public Writable processReceipt(String receipt)
	{
		String SQL = "";
		ReceiptParser parser = new ReceiptParser(receipt);
		//ReceiptDatum = ocrImage(receipt);
		
		/** TODO: Write code to extract relevant bits from the parser and save them to the database
		 * At the end of this, there should be a String that contains a concatenated list of all 
		 * SQL statements that were executed during this process.
		 */
		connect( );
		String ReceiptId = parser.getReceiptId( );
		String ItemName = parser.getItemName( );
		String Price = parser.getPrice( );
		String Quantity = parser.getQuantity( );
		String LocationId = parser.getStore( );
		String UserId = parser.getUser( );
		String Date = parser.getDate( );
		SQL += addReceipt(UserId, LocationId, Date);
		SQL += addLine(ReceiptId, ItemName, Price, Quantity);
					
		return new StringWritable(SQL);
	}
	
	public String addReceipt(String UserId, String LocationId, String Date)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Receipt WHERE " +
					"UserId='" + UserId + "' AND " +
					"LocationId='" + LocationId + "' AND " +
					"Date='" + Date + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("Receipt already exists in DB.");
				disconnect( );
				return "Receipt already exists in DB.";
			}//end while loop

			stmt.executeUpdate("INSERT INTO Receipt(UserId, LocationId, Date) VALUES ('" + 
					UserId + "', '" + LocationId + "', '" + 
					Date + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return "SELECT * from Receipt WHERE " +	"UserId='" + UserId + "' AND " +
		"LocationId='" + LocationId + "' AND " +
		"Date='" + Date + "'\n" + "INSERT INTO Receipt(UserId, LocationId, Date) VALUES ('" + 
		UserId + "', '" + LocationId + "', '" + Date + "')\n";
	}
	
	public String addLine(String ReceiptId, String ItemName, String Price, String Quantity)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Line WHERE " +
					"ReceiptId='" + ReceiptId + "' AND " +
					"ItemName='" + ItemName + "' AND " +
					"Price='" + Price + "' AND " +
					"Quantity='" + Quantity + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("Line already exists in DB.");
				disconnect( );
				return "Line already exists in DB.";
			}//end while loop

			stmt.executeUpdate("INSERT INTO Line (ReceiptId, ItemName, Price, Quantity) VALUES ('" +
					ReceiptId + "', '" +
					ItemName + "', '" +
					Price + "', '" +
					Quantity + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return "SELECT * from Line WHERE " +
		"ReceiptId='" + ReceiptId + "' AND " +
		"ItemName='" + ItemName + "' AND " +
		"Price='" + Price + "' AND " +
		"Quantity='" + Quantity + "'\n" + "INSERT INTO Line(ReceiptId, ItemName, Price, Quantity) VALUES ('" +
		ReceiptId + "', '" + ItemName + "', '" + Price + "', '" + Quantity + "')";
	}

	public void connect( )
	{  	
		// Establish Connection With Database
		try 
		{
			//Register the JDBC driver for MySQL.
		    Class.forName("com.mysql.jdbc.Driver");

		    //Get a connection to the database
		    con = DriverManager.getConnection(url, user, pass);	
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
	}
	
	public void disconnect( )
	{
		try
		{
			con.close( );
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
	}
}
