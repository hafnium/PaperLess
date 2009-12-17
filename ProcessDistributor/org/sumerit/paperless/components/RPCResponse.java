package org.sumerit.paperless.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.sumerit.paperless.constants.RPCState;
import org.sumerit.paperless.io.IntWritable;
import org.sumerit.paperless.io.Writable;

public class RPCResponse implements Writable
{
	private IntWritable state;
	private Writable data;
	
	public RPCResponse(){}
	
	public RPCResponse(Writable _data)
	{
		this(new IntWritable(RPCState.SUCCESS), _data);
	}
	
	public RPCResponse(IntWritable _state, Writable _data)
	{
		this.state = _state;
		this.data = _data;
	}
	
	public Writable getData()
	{
		return this.data;
	}
	
	public int getState()
	{
		return this.state.get();
	}
	
	public void setState(IntWritable _state)
	{
		this.state = _state;
	}
	
	public void write(DataOutputStream os) throws IOException
	{
		data.write(os);
		state.write(os);
	}
	
	public void readFrom(DataInputStream is) throws IOException
	{
		data.readFrom(is);
		state.readFrom(is);
	}
}
