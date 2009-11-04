package org.sumerit.database;

public class User 
{
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;

	public User( )
	{
		userId = "";
		firstName = "";
		lastName = "";
		email = "";
		password = "";
	}
	
	public User(String userId, String firstName, String lastName, String email, String password)
	{
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getUserId( )
	{
		return userId;
	}
	
	public String getFirstName( )
	{
		return firstName;
	}
	
	public String getLastName( )
	{
		return lastName;
	}
	
	public String getEmail( )
	{
		return email;
	}
	
	public String getPassword( )
	{
		return password;
	}
}
