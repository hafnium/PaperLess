package org.sumerit.paperless.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.constants.RPCState;
import org.sumerit.paperless.events.RPCEvent;
import org.sumerit.paperless.events.RPCListener;
import org.sumerit.paperless.io.IntWritable;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.io.Writable;
import org.sumerit.paperless.logging.DistributedLogger;

public class Processor extends Thread
{
	private static int connCount = 0;
	private InternetConnector connector;
	
	private Vector<RPCListener> listeners;
	
	public Processor(InternetConnector connector)
	{
		this.connector = connector;
		this.listeners = new Vector<RPCListener>();
	};
	
	public boolean connect(String hostname)
	{
		return connector.connect(hostname, connector.getPort());
	}	
	
	public void addListener(RPCListener L)
	{
		this.listeners.add(L);
	}
	
	public void callRPC(final String proc, final String args)
	{
		DistributedLogger.debug("Making RPC call to function: " + proc);
		//if (this.initiateRPC(proc))
		//{
			DistributedLogger.debug("RPC call to function: " + proc + " accepted!");
			RPCEvent e = new RPCEvent(this.invokeRPC(new StringWritable(), RPCCommand.EXECUTE, proc, args));
			
			if (e.getReponse() == null)
				return;
			
			if (listeners != null)
			{
				for (int i = 0; i < listeners.size(); i++)
					listeners.elementAt(i).handleEvent(e);
			}			
		//} 
	}
		
	public boolean initiateRPC(String proc) 
	{
		RPCResponse response = invokeRPC(new IntWritable(), RPCCommand.CHECK_AVAILABLE, proc, "");
			
		if (((IntWritable) response.getData()).get() == RPCState.SUCCESS)
			return true;
		else
			return false;	
	}
	
	public RPCResponse invokeRPC(Writable T, int type, String proc, String args)
	{
		if(this.connector.getSocket() == null || !this.connector.getSocket().isConnected())
		{
			DistributedLogger.warning("Processor::initiateRPC(): Cannot initiate RPC because client is not connected");
			return null;
		}			
		
		try {
			// Create incoming socket for response
			DistributedLogger.debug("Creating incoming socket for response");
			ServerSocket listener = new ServerSocket(0);
			
			// Write command to outgoing socket
			RPCCommand cmd = new RPCCommand(type, proc, args, listener.getLocalPort());
			DistributedLogger.debug("Writing command " + cmd.toString() + " to outbound socket");
			DataOutputStream os = this.connector.getOutputStream();								
			
			cmd.write(os);			
			os.flush();
			
			// Listen for response on incoming socket (BLOCKS)
			Socket localSocket = listener.accept();
			
			// Read back response
			RPCResponse response = new RPCResponse(T);
			DataInputStream is = new DataInputStream(localSocket.getInputStream());			
			response.readFrom(is);
								
			is.close();
			localSocket.close();
			listener.close();
			
			return response;
		} catch (IOException e) {
			DistributedLogger.fatal("Processor::invokeRPC(): Could not connect to host (IO Exception): " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			DistributedLogger.fatal("Processor::invokeRPC(): Could not connect to host (Unknown Exception): " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public void disconnect() {
		this.connector.disconnect();		
	}	
}
