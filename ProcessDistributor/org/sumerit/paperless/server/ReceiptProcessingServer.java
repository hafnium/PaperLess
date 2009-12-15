package org.sumerit.paperless.server;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.io.Writable;

public class ReceiptProcessingServer extends ProcessingServer 
{
	public native String ocrImage(String imageFilename);
	
	static {
		System.load("/af3/sma2t/local/ocrConnector.so");
	}	
	
	private class SQLCommand
	{
		private String sql;
		private final String dbname;
		
		public SQLCommand(String dbname)
		{
			this.dbname = dbname;
			this.sql = "";
		}
		
		public void addItem(String name, float cost)
		{
			sql += "INSERT INTO " + dbname + " itemName, itemCost VALUES('" + name + "', " + cost + ");";
		}
		
		public String get()
		{
			return this.sql;
		}
	};
	
	public ReceiptProcessingServer(InternetConnector connector) 
	{
		super(connector);
	}

	public static final int LISTENING_PORT = 11128;
	private static final String[] services = {"processReceipt"};
	
	public boolean checkAvailableTypes(String query) 
	{
		if (Arrays.binarySearch(services, query) < 0)
			return false;
		else
			return true;
	}
	
	protected int getPort()
	{
		return ReceiptProcessingServer.LISTENING_PORT;
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
		SQLCommand sql = new SQLCommand("receipts");
		
		StringTokenizer reader = new StringTokenizer(receipt, " ");
		
		while(reader.hasMoreTokens())
		{
			String itemName = reader.nextToken();			
			float itemCost = Float.parseFloat(reader.nextToken());
			
			sql.addItem(itemName, itemCost);
		}
		
		System.out.println(ocrImage("World"));
		
		return new StringWritable(sql.get());
	}

}
