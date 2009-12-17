package org.sumerit.paperless.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringWritable implements Writable
{
	private String data;
	
	public StringWritable()
	{
		this("");
	}
	
	public StringWritable(String data)
	{
		this.data = data;
	}
	
	@Override
	public void readFrom(DataInputStream is) throws IOException 
	{
		this.data = is.readUTF();
	}

	@Override
	public void write(DataOutputStream os) throws IOException 
	{
		os.writeUTF(this.data);
	}

	public String get()
	{
		return this.data;
	}
	
	public void set(String _data)
	{
		this.data = _data;
	}
}
