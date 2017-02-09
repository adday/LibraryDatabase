public class Borrowed_by{
	
	private int member_id;
	private String ISBN;
	private String checkout_String;
	private String checkin_String;
	
	public int get_member_id(){
		return member_id;
	}
	
	public String get_ISBN(){
		return ISBN;
	}
	
	public String get_checkout_date(){
		return checkout_String;
	}
	
	public String get_checkin_date(){
		return checkin_String;
	}
	
	public void set_member_id(int member_id){
		this.member_id = member_id;
	}
	
	public void set_ISBN(String ISBN){
		this.ISBN = ISBN;
	}
	
	public void set_checkout_date(String checkout_String){
		this.checkout_String = checkout_String;
	}
	
	public void set_checkin_date(String checkin_String){
		this.checkin_String = checkin_String;
	}
}