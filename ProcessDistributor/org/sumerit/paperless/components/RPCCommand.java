package org.sumerit.paperless.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.sumerit.paperless.io.Writable;

public class RPCCommand implements Writable
{
	public static final int CHECK = 0x1;
	
	private int type;
	private String args;
	
	/**
	 * Callback port
	 */
	private int callback;
	
	public RPCCommand(int type, String args, int callback)
	{
		this.type = type;
		this.args = args;
		this.callback = callback;
	}
	
	public int getCallback()
	{
		return this.callback;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public String getArguments()
	{
		return this.args;
	}
	
	public void write(DataOutputStream os) throws IOException
	{
		os.writeInt(type);
		os.writeUTF(args);
		os.writeInt(callback);
	}
	
	public void readFrom(DataInputStream is) throws IOException
	{
		this.type = is.readInt();
		this.args = is.readUTF();
		this.callback = is.readInt();
	}
}
