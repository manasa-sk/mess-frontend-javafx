import javafx.scene.control.Button;

public class AdminLeave {
	private final int lid;
	private final String student;
    private final int sid;
    private final int messId;
    private final String dates;
    private final String comment;

    public AdminLeave(int lid, String student, int sid, int messId, String dates, String comment) {
    	this.lid = lid;
    	this.student = student;
        this.sid = sid;
        this.messId = messId;
        this.dates = dates;
        this.comment = comment;
    }

	public String getStudent() {
        return student;
    }

    public int getId() {
        return sid;
    }
    
    public String getMess() {
    	return JDBCUtils.messName.get(messId);
    }
    
    public String getDates() {
        return dates;
    }
    
    public String getComment() {
    	return comment;
    }

    public Button getApproveButton() {
    	Button button = new Button("APPROVE");
    	button.setOnAction(event -> {
    		if(JDBCUtils.approveLeave(lid)) {
    			button.setText("APPROVED");
    			button.setDisable(true);
    		}
    	});
    	return button;
    }
}