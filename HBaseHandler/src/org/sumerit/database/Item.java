package src;

public class Item
{
	private String itemId;
	private String name;
	private String locationId;
	
	public Item( )
	{
		itemId = "";
		name = "";
		locationId = "";
	}
	
	public Item(String name, String locationId)
	{
		this.itemId = "";
		this.name = name;
		this.locationId = locationId;
	}
	
	public Item(String itemId, String name, String locationId)
	{
		this.itemId = itemId;
		this.name = name;
		this.locationId = locationId;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}
	
	public String getItemId( )
	{
		return itemId;
	}
	
	public String getName( )
	{
		return name;
	}
	
	public String getLocationId( )
	{
		return locationId;
	}
}
