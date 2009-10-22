package org.sumerit.paperless.logging;

public class Logger 
{
	private static Logger self;

	//key word PRIVATE prevent to invoke this constructor
	private Logger()
	{
	}

	//use this static method to get a instance of Logger
	public static Logger getInstance()
	{
		if (self == null)
			self = new Logger();
		return self;
	}

	//keyword "synchronized" make this method thread safe, only one thread
	public synchronized void debug(String msg)
	{
		self.debug(msg);
	}

	public synchronized void info(String msg)
	{
		self.info(msg);
	}

	public synchronized void fatal(String msg)
	{
		self.fatal(msg);
	}
	
	public synchronized void warning(String msg)
	{
		self.warning(msg);
	}
}
