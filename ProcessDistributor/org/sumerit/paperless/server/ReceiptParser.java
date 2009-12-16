package org.sumerit.paperless.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiptParser
{
	private String receipt;
	private final Pattern Rpattern = Pattern.compile("\\{receipt: [0-9]+\\}");
	private final Pattern INpattern = Pattern.compile("\\{item: [^\\}]+\\}");
	private final Pattern Spattern = Pattern.compile("\\{store: [0-9]+\\}");
	private final Pattern Upattern = Pattern.compile("\\{user: [0-9]+\\}");
	private final Pattern Qpattern = Pattern.compile("\\{quantity: [0-9]+\\}");
	private final Pattern Ppattern = Pattern.compile("\\{price: \\$?\\p{Blank}?[0-9]+\\.?[0-9]?[0-9]?\\}");
	private final Pattern Dpattern = Pattern.compile("\\{date: (19|20)\\d\\d-0[1-9]|1[012]-(0[1-9]|[12]\\d|3[01])\\}");
	
	public ReceiptParser(String _receipt)
	{
		this.receipt = _receipt;
	}
	
	public String getReceiptId()
	{
		Matcher M = Rpattern.matcher(receipt);
		if (!M.find())
			return "";
		String S = M.group(0);
		return S.substring(10, S.length()-1);
	}
	
	public String getItemName() {
		Matcher M = INpattern.matcher(receipt);
		if (!M.find())
			return "";
		String S = M.group(0);
		return S.substring(7, S.length()-1);
	}
	
	public String getStore(){
		Matcher M = Spattern.matcher(receipt);
		if (!M.find())
			return "";
		String S = M.group(0);
		return S.substring(8, S.length()-1);
	}
	
	public String getUser(){
		Matcher M = Upattern.matcher(receipt);
		if (!M.find())
			return "";
		String S = M.group(0);
		return S.substring(7, S.length()-1);
	}
	
	public String getQuantity(){
		Matcher M = Qpattern.matcher(receipt);
		if (!M.find())
			return "";
		String S = M.group(0);
		return S.substring(11, S.length()-1);
	}
	
	public String getPrice(){
		Matcher M = Ppattern.matcher(receipt);
		if (!M.find())
			return "";
		String S = M.group(0);
		int start = 8;
		if (S.charAt(start) == '$')
			start++;
		if (S.charAt(start) == ' ')
			start++;
		
		return S.substring(start, S.length()-1);
	}
	
	public String getDate(){
		Matcher M = Dpattern.matcher(receipt);
		if (!M.find())
			return "";
		String S = M.group(0);
		return S.substring(7, S.length()-1);
	}
}
