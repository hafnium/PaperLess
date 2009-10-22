package org.sumerit.paperless.events;

import org.sumerit.paperless.components.RPCResponse;

public class RPCEvent
{		
	private RPCResponse response;
	
	public RPCEvent(){ this(null); }
	
	public RPCEvent(RPCResponse _response)
	{
		this.response = _response;
	}
}
