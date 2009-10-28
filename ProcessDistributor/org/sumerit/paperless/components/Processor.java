package org.sumerit.paperless.components;

import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.events.RPCEvent;
import org.sumerit.paperless.events.RPCListener;
import org.sumerit.paperless.logging.DistributedLogger;

public class Processor 
{
	private InternetConnector connector;
	
	private RPCListener listener;
	
	public Processor(InternetConnector connector)
	{
		this.connector = connector;
	};
	
	public boolean connect(String hostname)
	{
		return connector.connect(hostname);
	}
	
	public void handleRPCFinished(RPCResponse response)
	{
		RPCEvent e = new RPCEvent(response);
		listener.handleEvent(e);
	}
	
	public synchronized void callRPC(final String proc)
	{
		DistributedLogger.info("Making RPC call to function: " + proc);
		Thread runner = new Thread(){
			public void run()
			{
				if (connector.initiateRPC(proc))
				{
					handleRPCFinished(connector.getRPCResult());
				} 
			}
		};
	}
}
