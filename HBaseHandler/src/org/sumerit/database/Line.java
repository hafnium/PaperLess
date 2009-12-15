package src;

public class Line
{
	private String lineId;
	private String receiptId;
	private String itemId;
	private String price;
	private String quantity;
	
	public Line( )
	{
		lineId = "";
		receiptId = "";
		itemId = "";
		price = "";
		quantity = "";
	}
	
	public Line(String receiptId, String itemId, String price, String quantity)
	{
		this.lineId = "";
		this.receiptId = receiptId;
		this.itemId = itemId;
		this.price = price;
		this.quantity = quantity;
	}
	
	public Line(String lineId, String receiptId, String itemId, String price, String quantity)
	{
		this.lineId = lineId;
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
	
	public String getLineId( )
	{
		return lineId;
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
