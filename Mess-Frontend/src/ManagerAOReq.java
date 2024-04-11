public class ManagerAOReq {
	private final String id;
	private final String item;
	private final String status;
	
	public ManagerAOReq (String id, String item, String status) {
		this.id = id;
		this.item = item;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	
	public String getItem() {
		return item;
	}
	
	public String getStatus() {
		return status;
	}
}
