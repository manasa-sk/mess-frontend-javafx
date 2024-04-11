import javafx.scene.control.Button;

public class AdminLeave {
    private final String student;
    private final String id;
    private final String dates;
    private final String comment;
    private final Button approveButton;

    public AdminLeave(String student, String id, String dates, String comment,
    		Button button) {
        this.student = student;
        this.id = id;
        this.dates = dates;
        this.comment = comment;
        this.approveButton = button;
    }

	public String getStudent() {
        return student;
    }

    public String getId() {
        return id;
    }
    
    public String getDates() {
        return dates;
    }
    
    public String getComment() {
    	return comment;
    }

    public Button getApproveButton() {
        return approveButton;
    }
}