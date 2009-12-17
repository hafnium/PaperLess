package org.sumerit.paperless.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.sumerit.paperless.components.RPCResponse;
import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.constants.RPCState;
import org.sumerit.paperless.io.IntWritable;
import org.sumerit.paperless.io.StringWritable;
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

	public RPCResponse execute(String proc, String args) 
	{
		StringWritable res = new StringWritable();
		int state = RPCState.ERROR_UNKNOWN_PROCESS;
		
		String[] argv = args.split("\n");
		if (proc.compareTo("processReceipt") == 0)
		{
			try {
				if(this.processReceipt(argv[0], res))
					state = RPCState.SUCCESS;
			} catch (ClassNotFoundException e)
			{
				DistributedLogger.fatal("Could not load mySQL JDBC Connector");
				e.printStackTrace();
				res = new StringWritable("ERROR: Could not load mySQL JDBC Connector");
			} catch (SQLException e)
			{
				DistributedLogger.fatal("Error executing SQL commands");
				e.printStackTrace();
				res = new StringWritable("ERROR: Error executing SQL commands");
			}
		} else
			res = new StringWritable("ERROR: Could not execute request, please check arguments");
		
		return new RPCResponse(new IntWritable(state), res);
	}
	
	public synchronized boolean processReceipt(String receipt, StringWritable res) throws ClassNotFoundException, SQLException
	{
		String SQL = "";
		ReceiptParser parser = new ReceiptParser(receipt);
		//ReceiptDatum = ocrImage(receipt);
		
		Class.forName("com.mysql.jdbc.Driver");
	    Connection con = DriverManager.getConnection(url, user, pass);
	    if (con == null)
	    {
	    	DistributedLogger.fatal("Could not connect to database");
	    	res.set(new String("Could not connect to database"));
	    	return false;
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
		
		try
		{
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (stmt == null)
			{
				con.close();
				DistributedLogger.fatal("Could not connect to database");
		    	res.set(new String("ERROR: Could not connect to database"));
		    	return false;
			}
			
			String selectSQL = "SELECT * from Receipt WHERE ReceiptId='" + ReceiptId + "'";
			ResultSet rs = stmt.executeQuery(selectSQL);
			
			if (rs.next()) 
			{
				stmt.close();
				DistributedLogger.warning("Receipt already exists in DB");
			} else
			{
				String insertSQL = "INSERT INTO Receipt (ReceiptId, UserId, LocationId, Date) VALUES ('" + ReceiptId + "', '" + UserId + "', '" + LocationId + "', '" + Date + "')";
				
				stmt.executeUpdate(insertSQL); 
				stmt.close();
				SQL += insertSQL;
			}
		} catch(SQLException e)
		{
			con.close();
			res.set(new String("ERROR: SQL Exception"));
			return false;
		}
		
		try
		{
			// Create a stmt object
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (stmt == null)
			{
				con.close();
				DistributedLogger.fatal("Could not connect to database");
				res.set(new String("ERROR: Could not connect to database"));
				return false;
			}

			String selectSQL = "SELECT * from Line WHERE " + 	"ReceiptId='" + ReceiptId + "' AND " + 
																"ItemName='" + ItemName + "' AND " + 
																"Price='" + Price + "' AND " +	
																"Quantity='" + Quantity + "'";
			ResultSet rs = stmt.executeQuery(selectSQL);
			

			// Check if User is already in the database
			if(rs.next())
			{
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
				SQL += "\n\t" + insertSQL;
			}
		} catch(SQLException e)
		{
			con.close();
			res.set(new String("ERROR: SQL Exception"));
			return false;
		}
		
		con.close();
		
		res.set(SQL);
		return true;
	}
}
