package org.sumerit.paperless.test;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.sumerit.paperless.connection.HttpConnector;
import org.sumerit.paperless.events.ExceptionListener;
import org.sumerit.paperless.events.RPCEvent;
import org.sumerit.paperless.events.RPCListener;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.logging.DistributedLogger;
import org.sumerit.paperless.server.ReceiptProcessingServer;

public class TestServer implements ExceptionListener, RPCListener
{
	public static void main(String[] args)
	{
		TestServer T = new TestServer();
		DistributedLogger.setLevel(Level.OFF);
		try {
			DistributedLogger.redirectOutput(new FileHandler("PaperLess.server.out"));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		ReceiptProcessingServer server = new ReceiptProcessingServer(new HttpConnector());
		server.addExceptionListener(T);
		System.out.println("Starting server...");
		DistributedLogger.info("Starting server...");
		server.start();
		
		while (server.isAlive()) {}
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
