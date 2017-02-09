import java.awt.GridLayout;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Lab5{	

	private static void init_gui(){
		Connection con = sql_connect();
		verify_member(con);
	}//end init_gui()

	private static Connection sql_connect(){
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url ="jdbc:mysql://faure/adday";
			con = DriverManager.getConnection(url,"adday", "830523317");
			return con;}
		catch( Exception e ) {
			e.printStackTrace();
			return null;}
	}//end sql_connect()

	public static void verify_member(Connection con){
		System.out.println("Asking for member id");
		Statement stmt;
		ResultSet rs;
		JTextField memberID_text = new JTextField(5);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.add(new JLabel("Enter member ID: "));
		panel.add(memberID_text);
		int result = JOptionPane.showConfirmDialog(null, panel, "Library Database", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(result == JOptionPane.CANCEL_OPTION)add_member(con);
		else if(result == JOptionPane.OK_OPTION){
			String id = memberID_text.getText();
			System.out.println("Entered member id: " + id);
			try{
				stmt = con.createStatement();
				rs = stmt.executeQuery("SELECT * FROM Member WHERE member_id = " + id);
				if(!rs.next()){
					System.out.println("Member ID not found");
					add_member(con);}
				else{
					System.out.println("Member ID found");
					search_book(con);}
			}catch(Exception e){e.printStackTrace();;}
		}
	}//end verify_member()

	public static void add_member(Connection con){
		System.out.println("Asking for new member info");
		Statement stmt;
		ResultSet rs;
		JTextField first_name_text = new JTextField(30);
		JTextField last_name_text = new JTextField(30);
		JTextField DOB_text = new JTextField(10);
		JTextField gender_text = new JTextField(1);
		String first_name, last_name,DOB,gender;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.add(new JLabel("Enter new member information as required"));
		panel.add(new JLabel(""));
		panel.add(new JLabel("First name: "));
		panel.add(first_name_text);
		panel.add(new JLabel("Last name: "));
		panel.add(last_name_text);
		panel.add(new JLabel("DOB (YYYY-MM-DD): "));
		panel.add(DOB_text);
		panel.add(new JLabel("Gender (M/F): "));
		panel.add(gender_text);
		int result = JOptionPane.showConfirmDialog(null, panel, "Member ID not found", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(result == JOptionPane.CANCEL_OPTION)verify_member(con);
		else if(result == JOptionPane.OK_OPTION) {
			first_name = first_name_text.getText();
			last_name = last_name_text.getText();
			DOB = DOB_text.getText();
			gender = gender_text.getText();
			System.out.println("first name: " + first_name);
			System.out.println("last name: " + last_name);
			System.out.println("DOB: " + DOB);
			System.out.println("gender: " + gender);
			try{
				if(first_name.isEmpty() || last_name.isEmpty() || DOB.isEmpty() || gender.isEmpty()){
					System.out.println("All fields must be filled out");
					add_member(con);}
				stmt = con.createStatement();
				rs = stmt.executeQuery("SELECT MAX(member_id) FROM Member");
				rs.next();
				int new_id = rs.getInt("MAX(member_id)") + 1;
				String update = "INSERT INTO Member VALUES (" + new_id + ",'" + last_name + "','" + first_name + "','" + DOB + "','" + gender + "');";
				stmt.executeUpdate(update);
				System.out.println ("Added new member");
				search_book(con);
			}
			catch(Exception e){e.printStackTrace();}
		}
	}//end add_member()

	public static void search_book(Connection con){
		System.out.println("Asking for book info");
		Statement stmt;
		ResultSet rs;
		JTextField ISBN_text = new JTextField(20);
		JTextField title_text = new JTextField(30);
		JTextField first_name_text = new JTextField(30);
		JTextField last_name_text = new JTextField(30);
		String ISBN,title,first_name,last_name;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.add(new JLabel("Locate a book by one of the following"));
		panel.add(new JLabel(""));
		panel.add(new JLabel("ISBN: "));
		panel.add(ISBN_text);
		panel.add(new JLabel("Title: "));
		panel.add(title_text);
		panel.add(new JLabel("Author: "));
		panel.add(new JLabel(""));
		panel.add(new JLabel("\tFirst name: "));
		panel.add(first_name_text); 
		panel.add(new JLabel("\tLast name: "));
		panel.add(last_name_text);
		panel.add(new JLabel(""));
		panel.add(new JLabel("Fill in both first and last name"));
		int result = JOptionPane.showConfirmDialog(null, panel, "Find Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(result == JOptionPane.CANCEL_OPTION)verify_member(con);
		else if(result == JOptionPane.OK_OPTION) {
			ISBN = ISBN_text.getText();
			title = title_text.getText();
			first_name = first_name_text.getText();
			last_name = last_name_text.getText();
			System.out.println("ISBN: " + ISBN);
			System.out.println("Title: " + title);
			System.out.println("Author: " + first_name + " " + last_name);
			try{
				stmt = con.createStatement();
				if(!ISBN.isEmpty()){
					System.out.println("Searching by ISBN");
					obtain_and_display_result(stmt,ISBN);}
				else if(!title.isEmpty()){
					System.out.println("Searching by title");
					rs = stmt.executeQuery("SELECT ISBN FROM Book WHERE title LIKE '%" + title + "%';");
					parse_result_show_titles(stmt,rs);}
				else if(!first_name.isEmpty() && !last_name.isEmpty()){
					System.out.println("Searching by author");
					rs = stmt.executeQuery("SELECT author_id FROM Author WHERE last_name = '" + last_name + "' AND first_name = " + "'" + first_name + "';");
					if(!rs.next())parse_result_show_titles(stmt,rs);
					rs = stmt.executeQuery("SELECT ISBN FROM Written_by WHERE author_id = " + rs.getInt("author_id") + ";");
					parse_result_show_titles(stmt,rs);}
				else{ 
					System.out.println("No book info input");
					search_book(con);}}
			catch(Exception e){e.printStackTrace();}
		}
	}//end search_book()

	public static void parse_result_show_titles(Statement stmt, ResultSet rs){
		System.out.println("Parsing search result");
		String selected_title = "";
		String selected_ISBN = "";
		try{
			ArrayList <String> ISBNs = new ArrayList<String>();
			while (rs.next()){
				ISBNs.add(rs.getString("ISBN"));
				System.out.println("Added ISBN");}
			if(ISBNs.size() > 1){
				String[] titles = new String[ISBNs.size()];
				for(int i=0;i<ISBNs.size();i++){
					System.out.println("Fetching title for ISBN: " + ISBNs.get(i));
					rs = stmt.executeQuery("SELECT title FROM Book WHERE ISBN = '" + ISBNs.get(i) + "';");
					rs.next();
					titles[i] = rs.getString("title");	
					System.out.println("Added title: " + titles[i]);}	
				while(selected_title.isEmpty()){
					selected_title = (String)JOptionPane.showInputDialog(null,"Select a title: ","Matching Titles", JOptionPane.QUESTION_MESSAGE,null, titles,titles[0]);	
					System.out.println("Selected title: " + selected_title);}    
				rs = stmt.executeQuery("SELECT ISBN FROM Book WHERE title = '" + selected_title + "';");
				rs.next();
				selected_ISBN = rs.getString("ISBN");}
			else if(ISBNs.size() == 1)
				selected_ISBN = ISBNs.get(0);
			System.out.println("Selected ISBN: " + selected_ISBN);
			obtain_and_display_result(stmt,selected_ISBN);
		}
		catch(Exception e){e.printStackTrace();}
	}//end parse_result_show_titles()

	public static void obtain_and_display_result(Statement stmt, String ISBN){
		ResultSet rs;
		try{
			rs = stmt.executeQuery("SELECT total_copies FROM Stored_on WHERE ISBN = '" + ISBN + "';"); 
			String book_status = "";
			if(!rs.next()) book_status = "There is no book in stock with the information entered.";
			else{			
				int sum = 0;
				rs.beforeFirst();
				while (rs.next()) sum += rs.getInt("total_copies");
				System.out.println("Number of copies total: " + sum);
				rs = stmt.executeQuery("SELECT COUNT(ISBN) FROM Borrowed WHERE ISBN = '" + ISBN + "' AND checkin_date = '9999-01-01';"); 
				rs.next();
				int loaned = rs.getInt("COUNT(ISBN)");
				System.out.println("Number of copies loaned: " + loaned);
				sum -= loaned;
				System.out.println("Number of copies available: " + sum);
				if(sum<0)System.out.println("Negative ghostrider, we have a negative!");
				rs = stmt.executeQuery("SELECT title FROM Book WHERE ISBN = '" + ISBN + "';");
				rs.next();
				String title = rs.getString("title");
				book_status = "<html>Title: " + title + "<br><br>";
				if(sum > 0){
					rs = stmt.executeQuery("SELECT lib_name,shelf_num FROM Stored_on WHERE ISBN = '" + ISBN + "';"); 
					while(rs.next())
						book_status += "Library: " + rs.getString("lib_name") + ", Shelf: " + rs.getInt("shelf_num") + "<br><br>";
					book_status += "Total copies available: " + sum + "</html>";}	
				else if(sum == 0)
					book_status += "All copies are currently checked out</html>";	
			}
			JPanel panel = new JPanel();
			panel.add(new JLabel(book_status));
			int result = JOptionPane.showConfirmDialog(null, panel, "Book Information", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
			if(result == JOptionPane.OK_OPTION) init_gui();
		}
		catch(Exception e){e.printStackTrace();}
	}//end obtain_and_display_result()

	public static void main (String[] args){init_gui();}
	
}//end class Lab5
