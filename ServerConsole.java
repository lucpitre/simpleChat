import java.io.IOException;
import java.util.Scanner;

import client.ChatClient;
import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	
	/** 
	 * The instance of the server
	 */
	EchoServer server;
	
	/**
	 * Scanner to read from the console
	 */
	Scanner fromConsole; 
	
	public ServerConsole(int port) {
		try {
			server = new EchoServer(port, this);
	    } 
	    catch(Exception exception) {
	    	System.out.println("Error: Can't setup connection!" + " Terminating server.");
	    	System.exit(1);
	    }
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	}
	
	public void accept() 
	  {
	    try {
	    	String message;

	    	while (true) {
	    		message = fromConsole.nextLine();
	    		server.handleMessageFromServerUI(message);
	    	}
	    } 
	    catch (Exception ex) {
	    	System.out.println("Unexpected error while reading from console!");
	    }
	  }
	
	public static void main(String[] args) {
		int port;

	    try {
	    	port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t) {
	    	port = DEFAULT_PORT; //Set port to 5555
	    }
	    
	    
	    ServerConsole server= new ServerConsole(port);
	    server.accept();  //Wait for console data
	}

	@Override
	public void display(String message) {
		System.out.println(message);
		
	}

}
	