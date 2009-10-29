package org.sumerit.paperless.test;

import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import org.sumerit.paperless.components.Processor;
import org.sumerit.paperless.connection.HttpConnector;
import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.events.ExceptionListener;
import org.sumerit.paperless.events.RPCEvent;
import org.sumerit.paperless.events.RPCListener;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.logging.DistributedLogger;
import org.sumerit.paperless.server.ReceiptProcessingServer;

public class Tester implements ExceptionListener, RPCListener {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Tester T = new Tester();
		DistributedLogger.setLevel(Level.ALL);
		//DistributedLogger.redirectOutput(new ConsoleHandler());	
		
		ReceiptProcessingServer server = new ReceiptProcessingServer(new HttpConnector());
		server.addExceptionListener(T);
		
		Processor P = new Processor(new HttpConnector());
		P.start();
		P.addListener(T);
		
		Scanner in = new Scanner(System.in);
		
		if(true)
		{
			server.start();
			P.connect("localhost");
			P.callRPC("processReceipt", "Orange 1.02");
		}			
		
		boolean prompt = true;
		while (prompt)
		{
			System.out.print("\n&> ");
			String choice = in.nextLine();
		
			if (choice.compareTo("start server") == 0)
				server.start();
			
			if (choice.compareTo("stop server") == 0)
				server.interrupt();
			
			if (choice.matches("connect .*"))
				P.connect(choice.split(" ")[1]);
			
			if (choice.matches("rpc [^ ]* .*"))
				P.callRPC(choice.split(" ")[1], choice.split(" ")[2]);		
			
			if (choice.compareTo("quit") == 0)
				prompt = false;
			
			if (choice.compareTo("help") == 0)
				printHelp();
		}
		
		System.out.println("Goodbye.");
		
		System.exit(1);
	}

	public static void printHelp()
	{
		System.out.println("Available Commands\n--------------------------------------\n");
		System.out.println("\tstart server\t\t - start a recipt processing server locally");
		System.out.println("\tstop server\t\t - stop the local recipt processing server");
		System.out.println("\tconnect <hostname>\t - connect to the processing server at <hostname>");
		System.out.println("\trpc <procedure>\t\t - make an RPC of type <procedure>");
		System.out.println("\tquit\t\t\t - quit this program");
	}
	
	@Override
	public void handleEvent(Exception e) 
	{	
		System.out.println(e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void handleEvent(RPCEvent e) 
	{
		System.out.println("RPC>> " + ((StringWritable) e.getReponse().getData()).get());
	}

}
