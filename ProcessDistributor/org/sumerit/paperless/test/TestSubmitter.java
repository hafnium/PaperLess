package org.sumerit.paperless.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.sumerit.paperless.distributor.ProcessDistributor;
import org.sumerit.paperless.io.StringWritable;

public class TestSubmitter 
{	
	public static void usage()
	{
		System.out.println("Usage: TestSubmitted [gateway server] [test input file]");
	}
	
	public static void main(String[] args)
	{
		if (args.length < 2) {
			usage();
			System.exit(-1);
		}
		
		try {
			String[] inputs = readInputs(args[1]);		
			for (int i = 0; i < inputs.length; i++)
			{
				System.out.println("Sending test " + inputs[i]);
				StringWritable str = new StringWritable(inputs[i]);
				
				Socket sock = new Socket(args[0], ProcessDistributor.listeningPort);
				str.write(new DataOutputStream(sock.getOutputStream()));
				sock.close();
				
				Thread.sleep(0);
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
	
	public static String[] readInputs(String file) throws NumberFormatException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int numTests = Integer.parseInt(reader.readLine());
		
		String[] ret = new String[numTests];
		
		for (int i = 0; i < numTests; i++)
			ret[i] = reader.readLine();
		
		return ret;
	}
}
