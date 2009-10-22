package org.sumerit.paperless.components;

import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.events.RPCEvent;
import org.sumerit.paperless.events.RPCListener;

public class Processor 
{
	private String hostname;
	private String ip;
	private InternetConnector connector;
	
	private RPCListener listener;
	
	public Processor(){};
	
	public boolean connect()
	{
		return connector.connect(this.hostname);
	}
	
	public void handleRPCFinished(RPCResponse response)
	{
		RPCEvent e = new RPCEvent(response);
		listener.handleEvent(e);
	}
	
	public synchronized void callRPC(final String proc)
	{
		Thread runner = new Thread(){
			public void run()
			{
				connector.initiateRPC(proc);
				handleRPCFinished(connector.getRPCResult());
			}
		};
	}
}
