package org.sumerit.paperless.server;

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
		SQLCommand sql = new SQLCommand("receipts");
		
		ReceiptParser parser = new ReceiptParser(receipt);
			
		sql.addItem(parser.getItemName(), Float.parseFloat(parser.getPrice()));
		
		//System.out.println(ocrImage("World"));
		
		return new StringWritable(sql.get());
	}
	
	public static String[] testProcessReceipt(String receipt)
	{
		ReceiptParser parser = new ReceiptParser(receipt);
		String[] res = new String[6];
		
		res[0] = parser.getItemName();
		res[1] = parser.getStore();
		res[2] = parser.getStoreLocation()[0] + ", " + parser.getStoreLocation()[1] + " " + parser.getStoreLocation()[2];
		res[3] = parser.getUser();
		res[4] = parser.getQuantity();
		res[5] = parser.getPrice();
		
		return res;
	}

}
