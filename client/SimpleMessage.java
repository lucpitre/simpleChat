/*
 * Simple message
 */
public class SimpleMessage {
	private Boolean clientMsg; // true if message is sent from a client, false if sent from server
	private String msg;
	
	public SimpleMessage(Boolean clientMsg, String msg) {
		this.clientMsg = clientMsg;
		this.msg = msg;
	}
	
	public Boolean getClientMsg() {
		return this.clientMsg;
	}
	
	public String getMsg() {
		return this.msg;
	}
}
