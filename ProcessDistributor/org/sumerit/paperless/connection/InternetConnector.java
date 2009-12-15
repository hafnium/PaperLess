package org.sumerit.paperless.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.sumerit.paperless.components.RPCCommand;
import org.sumerit.paperless.components.RPCResponse;
import org.sumerit.paperless.constants.RPCState;
import org.sumerit.paperless.io.IntWritable;
import org.sumerit.paperless.logging.DistributedLogger;

public abstract class InternetConnector 
{
	protected Socket socket = null;
	
	protected DataOutputStream out = null;
	
	protected ServerSocket listeningSocket = null;
	protected RPCResponse rpcResponse;

	private String hostname;

	private int port;
	
	public abstract int getPort();
	
	public Socket accept() throws IOException
	{
		if (this.listeningSocket == null)
			listeningSocket = new ServerSocket(this.getPort());
		
		// BLOCKS
		Socket ret = null;
		try {
			ret = listeningSocket.accept();
		} catch (SocketException e)
		{
			DistributedLogger.warning("Socket closed");
		}
		
		return ret; 
	}
	
	public void close() throws IOException
	{
		if (this.socket != null)
			this.socket.close();
		if (this.listeningSocket != null)
			this.listeningSocket.close();
		if (this.out != null)
			this.out.close();
	}
	
	public boolean connect(String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
		
		//return true;
		return this.connect();
	}
	
	private boolean connect() 
	{
		if (socket != null && socket.isConnected())
		{
			DistributedLogger.warning("InternetConnector::connect(): Client already connected to server");
			return false;
		}		
		
		try {
			DistributedLogger.debug("Connecting to " + hostname + ":" + port);
			
			socket = new Socket(hostname, port);
			this.out = new DataOutputStream(socket.getOutputStream());
			
			DistributedLogger.debug("Connected!");
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
		if (socket == null || !socket.isConnected())
		{
			DistributedLogger.warning("HttpConnector::disconnect(): Client not connected to server");
			return false;
		}
		
		try {
			out.close();
			socket.close();
			
			out = null;
			socket = null;
		} catch (IOException e) {
			DistributedLogger.fatal("HttpConnector::disconnect(): Could not disconnect from host (IO Exception): " + e.getMessage());
			return false;
		} catch (Exception e) {
			DistributedLogger.fatal("HttpConnector::disconnect(): Could not disconnect from host (Unknown Exception): " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public DataOutputStream getOutputStream()
	{
		this.disconnect();
		this.connect();
		return this.out;
	}
	
	public Socket getSocket()
	{
		return this.socket;
	}

	public SocketAddress getRemoteSocketAddress() 
	{
		return this.socket.getRemoteSocketAddress();
	}
}
