public class User {
	public int userId;
	public String name;
	public String email;
	public String hostel = "";
	public int messId = 0;
	public Role role;
	
	public enum Role {
			Student,
			Admin,
			Manager
	};
}
