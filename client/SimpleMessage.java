package client;

/*
 * Simple message
 */
public class SimpleMessage {
	private String id; 
	private String msg;
	
	public SimpleMessage(String id, String msg) {
		this.id = id;
		this.msg = msg;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	@Override
	public String toString() {
		return this.id + "> " + this.msg;
	}
}
