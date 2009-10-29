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
import org.sumerit.paperless.logging.DistributedLogger;

public class HttpConnector extends InternetConnector 
{
	public static final int port = 8080;
	
	public int getPort()
	{
		return HttpConnector.port;
	}
	
	public RPCResponse getRPCResult() 
	{
		return this.rpcResponse;
	}

	public boolean invokeRPC(String[] args) 
	{
		return false;
	}

}
