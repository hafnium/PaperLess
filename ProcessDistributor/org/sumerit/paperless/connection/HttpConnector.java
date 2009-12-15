package org.sumerit.paperless.connection;

import org.sumerit.paperless.components.RPCResponse;

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
