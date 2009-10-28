package org.sumerit.paperless.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.sumerit.paperless.components.RPCResponse;
import org.sumerit.paperless.logging.DistributedLogger;

public abstract class InternetConnector 
{
	protected Socket socket = null;
	protected ServerSocket listeningSocket = null;
	protected RPCResponse rpcResponse;
	
	public abstract int getPort();
	
	public void listen() throws IOException
	{
		if (this.listeningSocket == null)
			listeningSocket = new ServerSocket(this.getPort());
		
		listeningSocket.accept();
	}
	
	public void getInputStream()
	
	public boolean connect(String hostname) 
	{
		if (socket != null && socket.isConnected())
		{
			DistributedLogger.warning("InternetConnector::connect(): Client already connected to server");
			return false;
		}
		
		try {
			socket = new Socket(hostname, this.getPort());
		} catch (UnknownHostException e) {
			DistributedLogger.fatal("InternetConnector::connect(): Could not connect to host (Unknown Host): " + e.getMessage());
			return false;
		} catch (IOException e) {
			DistributedLogger.fatal("InternetConnector::connect(): Could not connect to host (IO Exception): " + e.getMessage());
			return false;
		} catch (Exception e) {
			DistributedLogger.fatal("InternetConnector::connect(): Could not connect to host (Unknown Exception): " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean disconnect() 
	{
		if (!socket.isConnected())
		{
			DistributedLogger.warning("HttpConnector::disconnect(): Client not connected to server");
			return false;
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			DistributedLogger.fatal("HttpConnector::disconnect(): Could not disconnect from host (IO Exception): " + e.getMessage());
			return false;
		} catch (Exception e) {
			DistributedLogger.fatal("HttpConnector::disconnect(): Could not disconnect from host (Unknown Exception): " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public abstract boolean initiateRPC(String proc);
	public abstract boolean invokeRPC(String[] args);
	public abstract RPCResponse getRPCResult();
}
