package org.sumerit.paperless.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.sumerit.paperless.io.Writable;

public class RPCResponse implements Writable
{
	private Writable data;
	
	public RPCResponse(){}
	
	public RPCResponse(Writable _data)
	{
		this.data = _data;
	}
	
	public Writable getResponse()
	{
		return this.data;
	}
	
	public void write(DataOutputStream os) throws IOException
	{
		data.write(os);
	}
	
	public void readFrom(DataInputStream is) throws IOException
	{
		data.readFrom(is);
	}
}
