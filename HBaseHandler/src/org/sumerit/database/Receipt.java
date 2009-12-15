package org.sumerit.database;

public class Receipt
{
	private String userId;
	private String receiptId;
	private String receiptNumber;
	private String date;
	
	public Receipt( )
	{
		userId = "";
		receiptId = "";
		receiptNumber = "";
		date = "";
	}
	
	public Receipt(String userId, String receiptNumber, String date)
	{
		this.userId = userId;
		this.receiptId = "";
		this.receiptNumber = receiptNumber;
		this.date = date;
	}
	
	public Receipt(String receiptId, String userId, String receiptNumber, String date)
	{
		this.userId = userId;
		this.receiptId = receiptId;
		this.receiptNumber = receiptNumber;
		this.date = date;
	}
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public void setReceiptNumber(String receiptNumber)
	{
		this.receiptNumber = receiptNumber;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public String getUserId( )
	{
		return userId;
	}
	
	public String getReceiptId( )
	{
		return receiptId;
	}
	
	public String getReceiptNumber( )
	{
		return receiptNumber;
	}
	
	public String getDate( )
	{
		return date;
	}
}
