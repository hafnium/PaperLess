package org.sumerit.paperless.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DistributedLogger 
{
	private static final Logger self = Logger.getLogger("org.sumerit.paperless.distributedLogger");

	private DistributedLogger()	{}
	
	public static void redirectOutput(Handler handler)
	{
		self.addHandler(handler);
	}

	//keyword "synchronized" make this method thread safe, only one thread
	public static synchronized void debug(String msg)
	{
		self.info(msg);
	}

	public static synchronized void info(String msg)
	{
		self.info(msg);
	}

	public static synchronized void fatal(String msg)
	{
		self.severe(msg);
	}
	
	public static synchronized void warning(String msg)
	{
		self.warning(msg);
	}
	
	public static synchronized void setLevel(Level level)
	{
		self.setLevel(level);
	}
}
