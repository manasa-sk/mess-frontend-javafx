public class AdminFB {
	private final String student;
    private final String id;
    private final String comment;

    public AdminFB(String student, String id, String comment) {
        this.student = student;
        this.id = id;
        this.comment = comment;
    }

	public String getStudent() {
        return student;
    }
	
	public String getId() {
		return id;
	}

    public String getComment() {
        return comment;
    }
}
