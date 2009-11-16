package org.sumerit.database;

public class Store 
{
	private String storeId;
	private String name;
	private String address;
	
	public Store( )
	{
		storeId = "";
		name = "";
		address = "";
	}
	
	public Store(String name, String address)
	{
		this.storeId = "";
		this.name = name;
		this.address = address;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getStoreId( )
	{
		return storeId;
	}
	
	public String getName( )
	{
		return name;
	}
	
	public String getAddress( )
	{
		return address;
	}
}
