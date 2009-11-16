package org.sumerit.database;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.*;

public class HBaseHandler 
{
	private Statement stmt;
	private ResultSet rs;
	private String url = "jdbc:mysql://dbm2.itc.virginia.edu:3306/myDatabase";
	private String user = "wb3b";
	private String pass = "Black-Flag!";
	private Connection con = null;
	private MessageDigest digest = null;
	
	public HBaseHandler( )
	{
		
	}
	
	public void connect( )
	{
		// Establish Connection With Database
		try 
		{
			//Register the JDBC driver for MySQL.
		    Class.forName("com.mysql.jdbc.Driver");

		    //Get a connection to the database for a
		    // user named auser with the password
		    // drowssap, which is password spelled
		    // backwards.
		    con = DriverManager.getConnection(url, user, pass);	
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
	}
	
	public void disconnect( )
	{
		try
		{
			con.close( );
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
	}
	
	public void addUser(User toAdd)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from User WHERE " +
					"FirstName='" + toAdd.getFirstName( ) + "' AND " +
					"LastName='" + toAdd.getLastName( ) + "' AND " +
					"Email='" + toAdd.getEmail( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("User already exists in DB.");
				disconnect( );
				return;
			}//end while loop

			digest = MessageDigest.getInstance("MD5");
			byte[] byteArray = toAdd.getPassword( ).getBytes();
			digest.update(byteArray);
			//digest.digest( );
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			String md5 = bigInt.toString(16);
						
			stmt.executeUpdate("INSERT INTO User(FirstName, LastName, Email, " +
					"Password) VALUES ('" + toAdd.getFirstName( ) + "', '" +
					toAdd.getLastName( ) + "', '" + toAdd.getEmail( ) + "', '" + 
					md5 + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
	}
	
	public void addStore(Store toAdd)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Store WHERE " +
					"Name='" + toAdd.getName( ) + "' AND " +
					"Address='" + toAdd.getAddress( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("Store already exists in DB.");
				disconnect( );
				return;
			}//end while loop

			stmt.executeUpdate("INSERT INTO Store(Name, Address) VALUES ('" + 
					toAdd.getName( ) + "', '" +	toAdd.getAddress( ) + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
	}
	
	public void addCity(City toAdd)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from City WHERE " +
					"City='" + toAdd.getCity( ) + "' AND " +
					"State='" + toAdd.getState( ) + "' AND " +
					"Zip='" + toAdd.getZip( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("City already exists in DB.");
				disconnect( );
				return;
			}//end while loop

			stmt.executeUpdate("INSERT INTO City(City, State, Zip) VALUES ('" + 
					toAdd.getCity( ) + "', '" +	toAdd.getState( ) + "', '" +
					toAdd.getZip( ) + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
	}
	
	public void addReceipt(Receipt toAdd)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Receipt WHERE " +
					"UserId='" + toAdd.getUserId( ) + "' AND " +
					"ReceiptNumber='" + toAdd.getReceiptNumber( ) + "' AND " +
					"Date='" + toAdd.getDate( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("Receipt already exists in DB.");
				disconnect( );
				return;
			}//end while loop

			stmt.executeUpdate("INSERT INTO Receipt(UserId, ReceiptNumber, Date) VALUES ('" + 
					toAdd.getUserId( ) + "', '" + toAdd.getReceiptNumber( ) + "', '" + 
					toAdd.getDate( ) + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
	}
	
	public void addItem(Item toAdd)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Item WHERE " +
					"Name='" + toAdd.getName( ) + "' AND " +
					"LocationId='" + toAdd.getLocationId( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("Item already exists in DB.");
				disconnect( );
				return;
			}//end while loop

			stmt.executeUpdate("INSERT INTO Item(Name, LocationId) VALUES ('" + 
					toAdd.getName( ) + "', '" + toAdd.getLocationId( ) + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
	}
	
	public void addLine(Line toAdd)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Line WHERE " +
					"ReceiptId='" + toAdd.getReceiptId( ) + "' AND " +
					"ItemId='" + toAdd.getItemId( ) + "' AND " +
					"Price='" + toAdd.getPrice( ) + "' AND " +
					"Quantity='" + toAdd.getQuantity( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("Line already exists in DB.");
				disconnect( );
				return;
			}//end while loop

			stmt.executeUpdate("INSERT INTO Line(ReceiptId, ItemId, Price, Quantity) VALUES ('" +
					toAdd.getReceiptId( ) + "', '" +
					toAdd.getItemId( ) + "', '" +
					toAdd.getPrice( ) + "', '" +
					toAdd.getQuantity( ) + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
	}
	
	public void addLocation(Location toAdd)
	{
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Location WHERE " +
					"StoreId='" + toAdd.getStoreId( ) + "' AND " +
					"CityId='" + toAdd.getCityId( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				System.out.println("Location already exists in DB.");
				disconnect( );
				return;
			}//end while loop

			stmt.executeUpdate("INSERT INTO Location(StoreId, CityId) VALUES ('" + 
					toAdd.getStoreId( ) + "', '" + toAdd.getCityId( ) + "')"); 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
	}
	
	public static void main(String [] args)
	{
		HBaseHandler handler = new HBaseHandler( );
		handler.addUser(new User("Wil", "Burns", "wb3b@virginia.edu", "password"));
	}
}
