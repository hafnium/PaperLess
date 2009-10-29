package org.sumerit.paperless.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Writable {
	public void readFrom(DataInputStream is) throws IOException;
	public void write(DataOutputStream os) throws IOException;	
}
