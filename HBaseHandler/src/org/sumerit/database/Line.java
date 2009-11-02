package org.sumerit.database;

public class Line
{
	private String receiptId;
	private String itemId;
	private String price;
	private String quantity;
	
	public Line( )
	{
		receiptId = "";
		itemId = "";
		price = "";
		quantity = "";
	}
	
	public Line(String receiptId, String itemId, String price, String quantity)
	{
		this.receiptId = receiptId;
		this.itemId = itemId;
		this.price = price;
		this.quantity = quantity;
	}
	
	public void setReceiptId(String receiptId)
	{
		this.receiptId = receiptId;
	}
	
	public void setItemId(String itemId)
	{
		this.itemId = itemId;
	}
	
	public void setPrice(String price)
	{
		this.price = price;
	}
	
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}
	
	public String getReceiptId( )
	{
		return receiptId;
	}
	
	public String getItemId( )
	{
		return itemId;
	}
	
	public String getPrice( )
	{
		return price;
	}
	
	public String getQuantity( )
	{
		return quantity;
	}
}
