package org.sumerit.paperless.test;

import java.io.IOException;
import java.net.UnknownHostException;

import org.sumerit.paperless.server.ReceiptParser;

public class TestReceiptParser 
{
	public static void usage()
	{
		System.out.println("Usage: TestReceiptParser [test input file]");
	}
	
	public static void main(String[] args)
	{
		if (args.length < 1) {
			usage();
			System.exit(-1);
		}
		
		try {
			String[] inputs = TestSubmitter.readInputs(args[0]);		
			for (int i = 0; i < inputs.length; i++)
			{
				System.out.println("\n\n=====================Testing " + inputs[i] + "===================");
				String[] res = testProcessReceipt(inputs[i]);
				for (int j = 0; j < res.length; j++)
					System.out.println(res[j]);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String[] testProcessReceipt(String receipt)
	{
		ReceiptParser parser = new ReceiptParser(receipt);
		String[] res = new String[6];
		
		res[0] = parser.getReceiptId();
		res[1] = parser.getItemName();
		res[2] = parser.getStore();
		res[3] = parser.getUser();
		res[4] = parser.getQuantity();
		res[5] = parser.getPrice();
		
		return res;
	}
}
