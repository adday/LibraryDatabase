import java.sql.*;
import java.util.ArrayList;

public class Lab4{	

  private static void checkin(Connection con, Borrowed_by record){
        Statement stmt;
		ResultSet rs;

	  	try{
		// Get a Statement object
        stmt = con.createStatement();
        
		//check if book checked out
		rs = stmt.executeQuery("SELECT * FROM Borrowed WHERE ISBN = '"
									+ record.get_ISBN() + "' AND member_id = '" 
									+ record.get_member_id() + "' AND checkin_date = '9999-01-01'");
		
		if(!rs.next()){
			System.out.println("No checked out book has ISBN " + record.get_ISBN());
			return;
		}
		
		String update = "UPDATE Borrowed SET checkin_date = '" + record.get_checkin_date() 
							+ "' WHERE ISBN = '" + record.get_ISBN() + "' AND member_id = '"
							+ record.get_member_id() + "' AND checkin_date IS NULL;"; 
		
		// Get a Statement object
        stmt = con.createStatement();
        
		//execute update
		stmt.executeUpdate(update);
       
		System.out.println ("Checked in book with ISBN " + record.get_ISBN());
      
      }catch(Exception e){
        System.out.print(e);
      }//end catch

  }
  
  private static void checkout(Connection con, Borrowed_by record){
	    Statement stmt;
		ResultSet rs;

	  	try{	
	    // Get a Statement object
        stmt = con.createStatement();
        
		//check if copies available
		rs = stmt.executeQuery("SELECT total_copies FROM Stored_on WHERE ISBN = '"
									+ record.get_ISBN() + "';"); 
		int sum = 0;
		while (rs.next()) {
			sum += rs.getInt("total_copies");
		}
		if(sum > 0)
		{
		String update = "INSERT INTO Borrowed VALUES ('" + record.get_ISBN() +
			"'," + record.get_member_id() + ",'" + record.get_checkout_date() + "','9999-01-01')";
		stmt.executeUpdate(update);
		System.out.println ("Checked out book with ISBN " + record.get_ISBN());
		}	
		else
			System.out.println("No copies of ISBN " + record.get_ISBN() + " available for checkout.");
      }
      catch(Exception e){
        System.out.print(e);
      }//end catch      
  }
  
  
  private static Connection sqlConnect(){
	  Connection con = null;

    try {
      // Register the JDBC driver for MySQL.
      Class.forName("com.mysql.jdbc.Driver");

      // Define URL of database server for
      // database named 'adday' on the faure.
      String url =
            "jdbc:mysql://faure/adday";

      // Get a connection to the database for a
      // user named 'aday' with the password
      // 830523317.
      con = DriverManager.getConnection(
                        url,"adday", "830523317");

	  return con;
	  }//end try
	catch( Exception e ) {
		e.printStackTrace();
		return null;
	}//end catch
  }

  public static void main(String args[]){
	Connection con = sqlConnect();
	
	ArrayList<Borrowed_by> records = new ArrayList<Borrowed_by>();
	
	//read in XML data
	Lab4_xml XMLparser = new Lab4_xml();
	records = XMLparser.readXML ("/s/bach/a/class/cs430dl/Current/more_assignments/LabData/Libdata.xml");
	
	//process records for day's activities
	for(int i=0;i<records.size();i++){
		if(records.get(i).get_checkin_date() != "NULL")
			checkin(con,records.get(i));
		else
			checkout(con,records.get(i));		
		}
		
  }//end main

}//end class Lab4A_ex