package src;

public class City
{
	private String cityId;
	private String city;
	private String state;
	private String zip;
	
	public City( )
	{
		cityId = "";
		city = "";
		state = "";
		zip = "";
	}
	
	public City(String city, String state, String zip)
	{
		this.cityId = null;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public City(String cityId, String city, String state, String zip)
	{
		this.cityId = cityId;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	
	public String getCityId( )
	{
		return cityId;
	}
	
	public String getCity( )
	{
		return city;
	}
	
	public String getState( )
	{
		return state;
	}
	
	public String getZip( )
	{
		return zip;
	}
}
