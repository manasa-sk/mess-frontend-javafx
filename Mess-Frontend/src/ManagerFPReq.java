import java.util.HashMap;

import javafx.scene.control.Button;

public class ManagerFPReq {
	public int reqId;
	public String name;
	public int id;
	public String item;
	public int mealType;
	public ReqStatus status;
	
	public ManagerFPReq (int reqId, String name, int id, String item, int mealType, ReqStatus status) {
		this.reqId = reqId;
		this.name = name;
		this.id = id;
		this.item = item;
		this.mealType = mealType;
		this.status = status;
	}
	
	@SuppressWarnings("serial")
	public static HashMap<Integer, String> mealTypes = new HashMap<Integer, String>(){{
		put(1, "FOOD PACKAGE");
		put(2, "ADD ON");
	}};
	
	public enum ReqStatus {
		PENDING,
		DELIVERED
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getItem() {
		return item;
	}
	
	public ReqStatus getStatus() {
		return status;
	}
	
	public String getType() {
		return mealTypes.get(mealType);
	}
	
	public Button getStatusButton() {
		Button button = new Button(status.toString());
		if(status==ReqStatus.DELIVERED) {
			button.setDisable(true);
		}
		else {
			button.setOnAction(event -> {
				if(JDBCUtils.updateFoodRequestStatus(reqId)) {
					button.setText(ReqStatus.DELIVERED.toString());
					button.setDisable(true);
				}
			});
		}
		
		return button;
	}
}
