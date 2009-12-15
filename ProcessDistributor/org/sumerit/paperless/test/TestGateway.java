package org.sumerit.paperless.test;

import java.util.logging.Level;

import org.sumerit.paperless.distributor.ProcessDistributor;
import org.sumerit.paperless.events.RPCEvent;
import org.sumerit.paperless.events.RPCListener;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.logging.DistributedLogger;

public class TestGateway implements RPCListener 
{
	public static void usage()
	{
		System.out.println("Usage: TestGateway [server list file]");
	}
	
	public static void main(String[] args)
	{
		if (args.length < 1) {
			usage();
			System.exit(-1);
		}
		
		DistributedLogger.setLevel(Level.OFF);
		
		ProcessDistributor D = new ProcessDistributor(args[0]);
		D.addListener(new TestGateway());
		D.start();
		while(D.isAlive()) {}
	}

	@Override
	public void handleEvent(RPCEvent e) 
	{
		System.out.println("RPC>> " + ((StringWritable) e.getReponse().getData()).get());
	}
}
