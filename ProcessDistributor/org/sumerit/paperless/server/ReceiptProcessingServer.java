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
		
		return new StringWritable(SQL);
	}

}
