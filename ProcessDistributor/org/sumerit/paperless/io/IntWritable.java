package org.sumerit.paperless.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntWritable implements Writable {

	private int value;

	public IntWritable(){};
	
	public IntWritable(int value)
	{
		this.value = value;
	}
	
	public int get()
	{
		return this.value;
	}
	
	@Override
	public void readFrom(DataInputStream is) throws IOException {
		value = is.readInt();
	}

	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeInt(value);
	}

}
