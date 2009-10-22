package org.sumerit.paperless.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.sumerit.paperless.components.RPCResponse;
import org.sumerit.paperless.logging.Logger;

public abstract class InternetConnector 
{
	protected Socket socket;
	protected RPCResponse rpcResponse;
	
	public abstract int getPort();
	
	public boolean connect(String hostname) 
	{
		if (socket.isConnected())
		{
			Logger.getInstance().warning("InternetConnector::connect(): Client already connected to server");
			return false;
		}
		
		try {
			socket = new Socket(hostname, this.getPort());
		} catch (UnknownHostException e) {
			Logger.getInstance().fatal("InternetConnector::connect(): Could not connect to host (Unknown Host): " + e.getMessage());
			return false;
		} catch (IOException e) {
			Logger.getInstance().fatal("InternetConnector::connect(): Could not connect to host (IO Exception): " + e.getMessage());
			return false;
		} catch (Exception e) {
			Logger.getInstance().fatal("InternetConnector::connect(): Could not connect to host (Unknown Exception): " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean disconnect() 
	{
		if (!socket.isConnected())
		{
			Logger.getInstance().warning("HttpConnector::disconnect(): Client not connected to server");
			return false;
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			Logger.getInstance().fatal("HttpConnector::disconnect(): Could not disconnect from host (IO Exception): " + e.getMessage());
			return false;
		} catch (Exception e) {
			Logger.getInstance().fatal("HttpConnector::disconnect(): Could not disconnect from host (Unknown Exception): " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public abstract boolean initiateRPC(String proc);
	public abstract boolean invokeRPC(String[] args);
	public abstract RPCResponse getRPCResult();
}
