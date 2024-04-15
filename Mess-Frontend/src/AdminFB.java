public class AdminFB {
	private final String student;
    private final int id;
    private final int messId;
    private final String comment;

    public AdminFB(String student, int id, int messId, String comment) {
        this.student = student;
        this.id = id;
        this.messId = messId;
        this.comment = comment;
    }

	public String getStudent() {
        return student;
    }
	
	public int getId() {
		return id;
	}

    public String getComment() {
        return comment;
    }
    
    public String getMess() {
    	return JDBCUtils.messName.get(messId);
    }
}
