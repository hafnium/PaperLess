package org.sumerit.paperless.distributor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import org.sumerit.paperless.components.Processor;
import org.sumerit.paperless.connection.HttpConnector;
import org.sumerit.paperless.events.RPCListener;
import org.sumerit.paperless.io.StringWritable;
import org.sumerit.paperless.logging.DistributedLogger;

/**
 * Spawns a new Processor thread for each ProcessRequest that it receives.
 * @author sma2t
 *
 */
public class ProcessDistributor extends Thread
{	
	private class ProcessCreator extends Thread
	{
		private String hostname;
		private StringWritable receipt;
		private Vector<RPCListener> listeners;
		
		public ProcessCreator(StringWritable receipt, String hostname, Vector<RPCListener> listeners)
		{
			this.hostname = hostname;
			this.receipt = receipt;
			this.listeners = listeners;
		}
		
		public void run()
		{
			Processor P = new Processor(new HttpConnector());			
			if (this.listeners != null)
				P.addListeners(this.listeners);
			P.start();
			if (!P.connect(this.hostname)) {
				add(receipt);
				return;
			}
			//System.out.println("Calling RPC command: " + receipt.get());
			boolean success = P.callRPC("processReceipt", receipt.get());
			P.disconnect();
			
			if (!success) {
				System.out.println("Requeueing...");
				add(receipt);
				return;
			}
			
			incrReceiptsProcessed();
		}
	}
	
	private class QueuePoller extends Thread
	{
		public void run()
		{
			while(true)
			{
				if (!receiptQueue.isEmpty())
				{
					ProcessCreator P = new ProcessCreator(receiptQueue.poll(), getNextAvailableServer(), rpcListeners);
					P.start();
					
					System.out.println("Elements left in queue: " + receiptQueue.size());
				}
			}
		}
	}
	
	private Vector<String> availableServers;
	private Vector<String> blacklistedServers;
	private int availableIndex = 0;
	
	private ServerSocket listeningSocket;
	public static final int listeningPort = 10280;
	private static final String killHash = "4078a7f2c5a9f82a77f9bb7bd98aae58";
	
	private Vector<RPCListener> rpcListeners;
	
	private ArrayBlockingQueue<StringWritable> receiptQueue;
	private QueuePoller queuePoller;
	
	private int receiptsProcessed = 0;
	private int recvCount = 0;
	
	private boolean poll;
	
	public ProcessDistributor(String serverList)
	{
		this.availableServers = new Vector<String>();
		this.blacklistedServers = new Vector<String>();
		
		this.reloadServerInformation(serverList);
		
		try {
			this.listeningSocket = new ServerSocket(listeningPort);
		} catch (IOException e) {
			DistributedLogger.fatal("Gateway failed because server socket failed to be created!");
			return;
		}
		
		poll = true;
		receiptQueue = new ArrayBlockingQueue<StringWritable>(10000);
		queuePoller = new QueuePoller();
		queuePoller.start();
	}
	
	public void addListener(RPCListener L)
	{
		if (rpcListeners == null)
			rpcListeners = new Vector<RPCListener>();
		
		this.rpcListeners.add(L);
	}
	
	private synchronized String getNextAvailableServer()
	{
		String ret = this.availableServers.get(this.availableIndex); 
		
		DistributedLogger.info("Using server " + this.availableIndex + "::" + ret);
		
		if (this.availableIndex + 1 >= this.availableServers.size())
			this.availableIndex = 0;
		else
			this.availableIndex++;
		
		return ret;
	}
	
	private synchronized void incrReceiptsProcessed()
	{
		receiptsProcessed++;
		DistributedLogger.info("Processed " + receiptsProcessed + " receipts");		
		System.out.println("Processed " + receiptsProcessed + " receipts");
	}
	
	private synchronized void add(StringWritable R)
	{
		this.receiptQueue.add(R);
		recvCount++;
		
		if (((float) recvCount) % 500 == 0 )
			System.out.println("Received " + recvCount);
	}
	
	public void run()
	{
		while(poll)
		{
			// Blocks
			Socket handlingSocket = null;
			try {
				handlingSocket = listeningSocket.accept();
			} catch (IOException e) {
				DistributedLogger.warning("Failed to accept incoming receipt processing request");
			}
			
			if (handlingSocket == null)
				break;
				
			if (!this.poll)
				break;
			
			DistributedLogger.debug("GATEWAY:: Receipt coming through...");
			
			StringWritable receipt = new StringWritable();
			try {
				InputStream is = handlingSocket.getInputStream();
				receipt.readFrom(new DataInputStream(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.add(receipt);
		}
	}
	
	private void shutdown()
	{
		this.poll = false;
		try {
			this.listeningSocket.close();
		} catch (IOException e) {
		}
	}
	
	public void kill(String kill_code)
	{
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return;
		}
		
		if (m == null)
			return;
		
	    m.update(kill_code.getBytes(), 0, kill_code.length());
	    String code = new BigInteger(1, m.digest()).toString(16);

		if (code.compareTo(killHash) == 0)
			this.shutdown();
	}
	
	public boolean reloadServerInformation(String file)
	{
		Vector<String> temp;
		try 
		{
			FileReader is = new FileReader(new File(file));
			BufferedReader reader = new BufferedReader(is);
			
			int numServers = Integer.parseInt(reader.readLine());
			temp = new Vector<String>(numServers);		
			
			System.out.println("Reading in " + numServers + " servers from " + file);
			
			for (int i = 0; i < numServers; i++) {
				temp.add(i, new String(reader.readLine()));
			}
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		this.availableServers.removeAllElements();
		this.blacklistedServers.removeAllElements();
		this.availableServers.ensureCapacity(temp.size());
		for (int i = 0; i < temp.size(); i++)
		{
			this.availableServers.add(i, new String(temp.get(i)));
			System.out.println("Adding server " + this.availableServers.get(i));
		}
		return true;
	}
}
