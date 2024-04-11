import javafx.scene.control.Button;

public class AdminLeaveAppShort {
    private final String student;
    private final String id;
    private final String dates;
    private final Button approveButton;

    public AdminLeaveAppShort(String student, String id, String dates,
    		Button button) {
        this.student = student;
        this.id = id;
        this.dates = dates;
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

    public Button getApproveButton() {
        return approveButton;
    }
}