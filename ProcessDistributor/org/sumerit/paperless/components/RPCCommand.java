package org.sumerit.paperless.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.sumerit.paperless.io.Writable;

public class RPCCommand implements Writable
{
	public static final int CHECK_AVAILABLE = 0x1;
	public static final int EXECUTE = 0x2;
	
	private int type;
	private String proc;
	private String args;
	
	/**
	 * Callback port
	 */
	private int callback;
	
	public RPCCommand(){};
	
	public RPCCommand(int type, String proc, String args, int callback)
	{
		this.type = type;
		this.proc = proc;
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
		os.writeUTF(proc);
		os.writeUTF(args);
		os.writeInt(callback);
	}
	
	public void readFrom(DataInputStream is) throws IOException
	{
		this.type = is.readInt();
		this.proc = is.readUTF();
		this.args = is.readUTF();
		this.callback = is.readInt();
	}

	public String getProcedure() 
	{
		return this.proc;
	}
	
	public String toString()
	{
		return this.proc + "(" + this.args + ") [" + this.type + "]";
	}
}
