package org.sumerit.database;

public class Location
{
	private String locationId;
	private String storeId;
	private String cityId;
	
	public Location( )
	{
		locationId = "";
		storeId = "";
		cityId = "";
	}
	
	public Location(String locationId, String storeId, String cityId)
	{
		this.locationId = locationId;
		this.storeId = storeId;
		this.cityId = cityId;
	}
	
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}
	
	public void setStoreId(String storeId)
	{
		this.storeId = storeId;
	}
	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}
	
	public String getLocationId( )
	{
		return locationId;
	}
		
	public String getStoreId( )
	{
		return storeId;
	}
	
	public String getCityId( )
	{
		return cityId;
	}
}
