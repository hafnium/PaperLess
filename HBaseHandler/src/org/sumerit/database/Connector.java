package src;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Scanner;
import org.apache.hadoop.hbase.io.BatchUpdate;
import org.apache.hadoop.hbase.io.Cell;
import org.apache.hadoop.hbase.io.RowResult;
import org.apache.hadoop.hbase.util.Bytes;

public class Connector {

  public static void main(String args[]) throws IOException {
    // You need a configuration object to tell the client where to connect.
    // But don't worry, the defaults are pulled from the local config file.
    HBaseConfiguration config = new HBaseConfiguration();

    // This instantiates an HTable object that connects you to the "myTable"
    // table. 
    HTable table = new HTable(config, "myTable");

    // To do any sort of update on a row, you use an instance of the BatchUpdate
    // class. A BatchUpdate takes a row and optionally a timestamp which your
    // updates will affect.  If no timestamp, the server applies current time
    // to the edits.
    BatchUpdate batchUpdate = new BatchUpdate("myRow");

    // The BatchUpdate#put method takes a byte [] (or String) that designates
    // what cell you want to put a value into, and a byte array that is the
    // value you want to store. Note that if you want to store Strings, you
    // have to getBytes() from the String for HBase to store it since HBase is
    // all about byte arrays. The same goes for primitives like ints and longs
    // and user-defined classes - you must find a way to reduce it to bytes.
    // The Bytes class from the hbase util package has utility for going from
    // String to utf-8 bytes and back again and help for other base types.
    batchUpdate.put("myColumnFamily:columnQualifier1", 
      Bytes.toBytes("columnQualifier1 value!"));

    // Deletes are batch operations in HBase as well. 
    batchUpdate.delete("myColumnFamily:cellIWantDeleted");

    // Once you've done all the puts you want, you need to commit the results.
    // The HTable#commit method takes the BatchUpdate instance you've been 
    // building and pushes the batch of changes you made into HBase.
    table.commit(batchUpdate);

    // Now, to retrieve the data we just wrote. The values that come back are
    // Cell instances. A Cell is a combination of the value as a byte array and
    // the timestamp the value was stored with. If you happen to know that the 
    // value contained is a string and want an actual string, then you must 
    // convert it yourself.
    Cell cell = table.get("myRow", "myColumnFamily:columnQualifier1");
    // This could throw a NullPointerException if there was no value at the cell
    // location.
    String valueStr = Bytes.toString(cell.getValue());
    
    // Sometimes, you won't know the row you're looking for. In this case, you
    // use a Scanner. This will give you cursor-like interface to the contents
    // of the table.
    Scanner scanner = 
      // we want to get back only "myColumnFamily:columnQualifier1" when we iterate
      table.getScanner(new String[]{"myColumnFamily:columnQualifier1"});
    
    
    // Scanners return RowResult instances. A RowResult is like the
    // row key and the columns all wrapped up in a single Object. 
    // RowResult#getRow gives you the row key. RowResult also implements 
    // Map, so you can get to your column results easily. 
    
    // Now, for the actual iteration. One way is to use a while loop like so:
    RowResult rowResult = scanner.next();
    
    while (rowResult != null) {
      // print out the row we found and the columns we were looking for
      System.out.println("Found row: " + Bytes.toString(rowResult.getRow()) +
        " with value: " + rowResult.get(Bytes.toBytes("myColumnFamily:columnQualifier1")));
      rowResult = scanner.next();
    }
    
    // The other approach is to use a foreach loop. Scanners are iterable!
    for (RowResult result : scanner) {
      // print out the row we found and the columns we were looking for
      System.out.println("Found row: " + Bytes.toString(result.getRow()) +
        " with value: " + result.get(Bytes.toBytes("myColumnFamily:columnQualifier1")));
    }
    
    // Make sure you close your scanners when you are done!
    // Its probably best to put the iteration into a try/finally with the below
    // inside the finally clause.
    scanner.close();
  }
}