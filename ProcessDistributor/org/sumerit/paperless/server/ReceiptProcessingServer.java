package org.sumerit.paperless.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.io.Writable;
import org.sumerit.paperless.logging.DistributedLogger;

public class ReceiptProcessingServer extends ProcessingServer 
{
	private final String user = "sean";
	private final String pass = "test";
	private final String url = "jdbc:mysql://76.120.194.253:3306/Paperless";
	
	/*
	public native String ocrImage(String imageFilename);
	
	static {
		System.load("/af3/sma2t/local/ocrConnector.so");
	}	
	*/	
	
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
			try {
				return this.processReceipt(argv[0]);
			} catch (ClassNotFoundException e)
			{
				DistributedLogger.fatal("Could not load mySQL JDBC Connector");
			} catch (SQLException e)
			{
				DistributedLogger.fatal("Error executing SQL commands");
			}
		}
		
		return new StringWritable("ERROR: Could not execute request, please check arguments");
	}
	
	public Writable processReceipt(String receipt) throws ClassNotFoundException, SQLException
	{
		String SQL = "";
		ReceiptParser parser = new ReceiptParser(receipt);
		//ReceiptDatum = ocrImage(receipt);
		
		Class.forName("com.mysql.jdbc.Driver");
	    Connection con = DriverManager.getConnection(url, user, pass);
	    if (con == null)
	    {
	    	DistributedLogger.fatal("Could not connect to database");
	    	return new StringWritable("ERROR: Could not connect to database");
	    }
	    
		String ReceiptId = parser.getReceiptId( );
		String ItemName = parser.getItemName( );
		String Price = parser.getPrice( );
		String Quantity = parser.getQuantity( );
		String LocationId = parser.getStore( );
		String UserId = parser.getUser( );
		String Date = parser.getDate( );
		if(UserId.length( ) == 0)
			UserId = "1";
		if(Quantity.length( ) == 0)
			Quantity = "1";
		
		{
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (stmt == null)
			{
				con.close();
				DistributedLogger.fatal("Could not connect to database");
		    	return new StringWritable("ERROR: Could not connect to database");
			}
			
			String selectSQL = "SELECT * from Receipt WHERE ReceiptId='" + ReceiptId + "'";
			ResultSet rs = stmt.executeQuery(selectSQL);
			
			if (rs.next()) 
			{
				con.close();
				stmt.close();
				DistributedLogger.warning("Receipt already exists in DB");
			} else
			{
				String insertSQL = "INSERT INTO Receipt (ReceiptId, UserId, LocationId, Date) VALUES ('" + ReceiptId + "', '" + UserId + "', '" + LocationId + "', '" + Date + "')";
				
				stmt.executeUpdate(insertSQL); 
				stmt.close();
				SQL += insertSQL;
			}
		}
		
		{
			// Create a stmt object
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (stmt == null)
			{
				con.close();
				DistributedLogger.fatal("Could not connect to database");
		    	return new StringWritable("ERROR: Could not connect to database");
			}

			String selectSQL = "SELECT * from Line WHERE " + 	"ReceiptId='" + ReceiptId + "' AND " + 
																"ItemName='" + ItemName + "' AND " + 
																"Price='" + Price + "' AND " +	
																"Quantity='" + Quantity + "'";
			ResultSet rs = stmt.executeQuery(selectSQL);
			

			// Check if User is already in the database
			if(rs.next())
			{
				con.close();
				stmt.close();
				DistributedLogger.warning("Receipt already exists in DB");
			} else
			{
				String insertSQL = "INSERT INTO Line (ReceiptId, ItemName, Price, Quantity) VALUES ('" + 	ReceiptId + "', '" +
																											ItemName + "', '" +
																											Price + "', '" +
																											Quantity + "')";
				stmt.executeUpdate(insertSQL); 
				stmt.close();
				SQL += insertSQL;
			}
		}
		
		con.close();
					
		return new StringWritable(SQL);
	}
}
