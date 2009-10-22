package org.sumerit.paperless.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.sumerit.paperless.components.RPCCommand;
import org.sumerit.paperless.components.RPCResponse;
import org.sumerit.paperless.constants.RPCState;
import org.sumerit.paperless.io.IntWritable;
import org.sumerit.paperless.logging.Logger;

public class HttpConnector extends InternetConnector 
{
	private static final int port = 80;
	
	public int getPort()
	{
		return HttpConnector.port;
	}
	
	public RPCResponse getRPCResult() 
	{
		return this.rpcResponse;
	}

	public boolean initiateRPC(String proc) 
	{
		try {
			ServerSocket listener = new ServerSocket();
			DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());						
			RPCCommand cmd = new RPCCommand(RPCCommand.CHECK, proc, listener.getLocalPort());
			RPCResponse response = new RPCResponse(new IntWritable());
			
			cmd.write(os);	
						
			// This next call blocks
			Socket localSocket = listener.accept();
			
			DataInputStream is = new DataInputStream(localSocket.getInputStream());			
			response.readFrom(is);
			
			if (((IntWritable) response.getResponse()).get() == RPCState.SUCCESS)
				return true;
			else
				return false;
			
		} catch (IOException e) {
			Logger.getInstance().fatal("HttpConnector::initiateRPC(): Could not connect to host (IO Exception): " + e.getMessage());
			return false;
		} catch (Exception e) {
			Logger.getInstance().fatal("HttpConnector::initiateRPC(): Could not connect to host (Unknown Exception): " + e.getMessage());
			return false;
		}	
	}

	public boolean invokeRPC(String[] args) 
	{
		return false;
	}

}
