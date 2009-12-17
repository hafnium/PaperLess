package org.sumerit.paperless.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.sumerit.paperless.components.RPCCommand;
import org.sumerit.paperless.components.RPCResponse;
import org.sumerit.paperless.connection.InternetConnector;
import org.sumerit.paperless.constants.RPCState;
import org.sumerit.paperless.events.ExceptionListener;
import org.sumerit.paperless.io.IntWritable;
import org.sumerit.paperless.logging.DistributedLogger;

public abstract class ProcessingServer extends Thread
{
	private class ConnectionHandler extends Thread
	{
		private Socket socket;
		private ExceptionListener errListener;
		
		public ConnectionHandler(Socket socket, ExceptionListener errListener) 
		{
			this.socket = socket;
			this.errListener = errListener;
		}
		
		public void run()
		{
			if(this.socket.isClosed())
				return;
			
			try {
				// Receive command
				DistributedLogger.debug("SERVER:: Receiving command");
				final RPCCommand command = new RPCCommand();
				DataInputStream is = new DataInputStream(socket.getInputStream());
				try {
					command.readFrom(is);
				} catch (EOFException e)
				{
					return;
				}
							
				// Pass handling of command to function
				DistributedLogger.debug("SERVER:: Handling request");
				
				// cleanup
				is.close();
				
				DistributedLogger.info("Got command " + command.toString());
				
				if (command.getType() == RPCCommand.CHECK_AVAILABLE)
				{
					if (checkAvailableTypes(command.getProcedure()))
					{
						DistributedLogger.info("Accepted command: " + command.toString());
						
						// Get remote socket information
						InetSocketAddress clientAddress = (InetSocketAddress) this.socket.getRemoteSocketAddress();
						
						// Respond with a SUCCESS
						DistributedLogger.debug("Responding to " + clientAddress.getHostName() + ":" + command.getCallback());
						Socket responder = new Socket(clientAddress.getHostName(), command.getCallback());
						DataOutputStream os = new DataOutputStream(responder.getOutputStream());
						
						RPCResponse response = new RPCResponse(new IntWritable(RPCState.SUCCESS));
						
						response.write(os);
						os.flush();
						os.close();
						responder.close();
					}
				} else if (command.getType() == RPCCommand.EXECUTE)
				{
					if (checkAvailableTypes(command.getProcedure()))
					{
						RPCResponse response = execute(command.getProcedure(), command.getArguments());
						DistributedLogger.info("Executed command: " + command.toString());
						System.out.println("Executed command: " + command.toString());
						
						// Get remote socket information
						InetSocketAddress clientAddress = (InetSocketAddress) this.socket.getRemoteSocketAddress();
						
						// Send response back
						DistributedLogger.debug("Responding to " + clientAddress.getHostName() + ":" + command.getCallback());
						Socket responder = new Socket(clientAddress.getHostName(), command.getCallback());
						DataOutputStream os = new DataOutputStream(responder.getOutputStream());
						
						response.write(os);
						os.flush();
						os.close();						
						responder.close();
					}
				}		
				
				this.socket.close();
			} catch (IOException e)
			{
				DistributedLogger.fatal("ProcessingServer::handleConnection(): Could not get request from host (IO Exception): " + e.getMessage());
				errListener.handleEvent(e);
			}
		}		
	};
	
	private InternetConnector listeningConnector = null;	
	private boolean poll;	
	private ExceptionListener errListener;
	
	public abstract boolean checkAvailableTypes(String query);
	public abstract RPCResponse execute(String proc, String args);
	
	public ProcessingServer(InternetConnector connector)
	{		
		listeningConnector = connector;
		this.poll = true;
	}
	
	public void addExceptionListener(ExceptionListener listener)
	{
		this.errListener = listener;
	}
	
	public boolean attemptInterrupt()
	{
		this.poll = false;
		
		return true;
	}
	
	protected void listen() throws IOException
	{				
		while(this.poll)
		{
			// Blocks
			Socket listeningSocket = listeningConnector.accept();
			
			if (listeningSocket == null)
				break;
				
			if (!this.poll)
				break;
			
			DistributedLogger.debug("SERVER:: Connection accepted");
			
			ConnectionHandler handler = new ConnectionHandler(listeningSocket, this.errListener);
			handler.start();
		}		
		
		this.listeningConnector.close();
	}

	@Override
	public void run() 
	{
		try 
		{
			this.listen();
		} catch (IOException e) 
		{
			this.errListener.handleEvent(e);
		} 		
	}
	
	public void interrupt()
	{		
		this.poll = false;	
		try {
			this.listeningConnector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}
}
