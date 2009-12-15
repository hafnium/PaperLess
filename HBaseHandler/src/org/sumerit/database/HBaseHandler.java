package src;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Scanner;
import org.apache.hadoop.hbase.io.BatchUpdate;
import org.apache.hadoop.hbase.io.Cell;
import org.apache.hadoop.hbase.io.RowResult;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseHandler 
{
	private Connection con = null;
	private MessageDigest digest = null;
	
	private HBaseConfiguration config = null;
	private HTable table = null;
	private Scanner scanner = null;
	
	public HBaseHandler( )
	{
		try
		{
			// You need a configuration object to tell the client where to connect.
			// But don't worry, the defaults are pulled from the local config file.
			config = new HBaseConfiguration( );
		}
		catch(IOException IOE)
		{
			System.out.println("Initialization Error.");
		}
	}
	
	public void connect( )
	{
		
	}
	
	public void disconnect( )
	{
		
	}
	
	public void addUser(User toAdd)
	{
		// This instantiates an HTable object that connects you to the "myTable"
		// table. 
		table = new HTable(config, "myTable");
//		 Storing data
		long writeid = table.startUpdate(row);
		table.put(writeid, columnName1, bytes);
		table.put(writeid, columnName2, bytes);
		table.delete(writeid, columnName3);
		table.commit(writeid);
//		 Reading data
		byte[] data = table.get(row, columnName1);

	}
	
/*	public void addStore(Store toAdd)
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
	
	public User getUser(String email)
	{
		User toReturn = null;
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from User WHERE " +
					"User.Email='" + email + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				String userId = rs.getString("UserId");
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				String password = rs.getString("Password");
				toReturn = new User(userId, firstName, lastName, email, password);
		    }//end while loop 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return toReturn;
	}
	
	public ArrayList <Receipt> getReceipts(User user)
	{
		ArrayList <Receipt> toReturn = new ArrayList <Receipt> ( );
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Receipt WHERE " +
					"Receipt.UserId='" + user.getUserId( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				String receiptId = rs.getString("ReceiptId");
				String userId = rs.getString("UserId");
				String receiptNumber = rs.getString("ReceiptNumber");
				String date = rs.getString("Date");
				Receipt toAdd = new Receipt(receiptId, userId, receiptNumber, date);
				toReturn.add(toAdd);	
		    }//end while loop 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return toReturn;
	}
	
	public Item getItem(Line line)
	{
		Item toReturn = null;
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Item WHERE " +
					"ItemId='" + line.getItemId( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				String itemId = rs.getString("ItemId");
				String name = rs.getString("Name");
				String locationId = rs.getString("LocationId");
				toReturn = new Item(itemId, name, locationId);	
		    }//end while loop 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return toReturn;
	}
	
	public ArrayList <Line> getLines(Receipt receipt)
	{
		ArrayList <Line> toReturn = new ArrayList <Line> ( );
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Line WHERE " +
					"Line.ReceiptId='" + receipt.getReceiptId( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				String lineId = rs.getString("LineId");
				String receiptId = rs.getString("ReceiptId");
				String itemId = rs.getString("ItemId");
				String price = rs.getString("Price");
				String quantity = rs.getString("Quantity");
				Line toAdd = new Line(lineId, receiptId, itemId, price, quantity);
				toReturn.add(toAdd);	
		    }//end while loop 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return toReturn;
	}
	
	public City getCity(String city, String state, String zip)
	{
		City toReturn = null;
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from City WHERE " +
					"City='" + city + "' AND State='" + state + "' AND Zip='" + zip + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				String cityId = rs.getString("CityId");
				toReturn = new City(cityId, city, state, zip);	
		    }//end while loop 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return toReturn;
	}
	
	public Store getStore(String name, String address)
	{
		Store toReturn = null;
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT * from Store WHERE " +
					"Name='" + name + "' AND Address='" + address + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				String storeId = rs.getString("StoreId");
				toReturn = new Store(storeId, name, address);	
		    }//end while loop 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return toReturn;
	}
	
	public Location getLocation(Store store, City city)
	{
		Location toReturn = null;
		connect( );
		try
		{
			// Create a stmt object
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			//Query the database, storing the result
			// in an object of type ResultSet
			rs = stmt.executeQuery("SELECT LocationId from Location, Store, City WHERE " +
					"Store.StoreId='" + store.getStoreId( ) + "' AND City.CityId='" + city.getCityId( ) + "'");
			

			// Check if User is already in the database
			while(rs.next())
			{
				String locationId = rs.getString("LocationId");
				toReturn = new Location(locationId, store.getStoreId( ), city.getCityId( ));	
		    }//end while loop 
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
	    }//end catch
		finally
		{
			disconnect( );
		}
		return toReturn;
	}*/
	
	public static void main(String [] args)
	{
		HBaseHandler handler = new HBaseHandler( );
		
		// Create data structures to be added to DB
		User user = new User("Wil", "Burns", "wb3b@virginia.edu", "password");
		City city = new City("Charlottesville", "VA", "22903");
		Store store = new Store("Harris Teeter", "975 Emmet Street North");
		Location location = new Location("1", "1");
		Item item1 = new Item("Milk", "1");
		Item item2 = new Item("Eggs", "1");
		Item item3 = new Item("Bacon", "1");
		Line line1 = new Line("1", "1", "3.39", "2");
		Line line2 = new Line("1", "2", "1.89", "1");
		Line line3 = new Line("1", "3", "2.99", "3");
		
		// Perform inserts
		handler.addUser(user);
/*		handler.addCity(city);
		handler.addStore(store);
		handler.addLocation(location);
		handler.addItem(item1);
		handler.addItem(item2);
		handler.addItem(item3);
		handler.addLine(line1);
		handler.addLine(line2);
		handler.addLine(line3);
		handler.addReceipt(new Receipt("2", "1", "2009-12-07"));*/
		
		//user = handler.getUser("wb3b@virginia.edu");
		System.out.println("User Id: " + user.getUserId( ));
		
		// Print user's receipt list
		/*ArrayList <Receipt> receipts = handler.getReceipts(user);
		for(Receipt toPrint : receipts)
		{
			System.out.println("Receipt id:" + toPrint.getReceiptId( ));
			ArrayList <Line> lines = handler.getLines(toPrint);
			for(Line line : lines)
			{
				Item item = handler.getItem(line);
				System.out.println("Item: " + item.getName( ) + " Price: " + line.getPrice( ) + " Quantity: " + line.getQuantity( ));
			}
		}*/
	}
}