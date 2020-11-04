// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  if(message.charAt(0) == '#') { // check if the message is a command
		  String command = message.substring(1);
		  handleCommandFromClientUI(command);
	  } else {
		  try {
		      sendToServer(message);
		  }
		  catch(IOException e) {
		      clientUI.display("Could not send message to server.  Terminating client.");
		      quit();
		  }
	  }
  }
  
  /*
   * This method handles commands coming from the UI
   */
  private void handleCommandFromClientUI(String command) {
	  String[] args = command.split(" ");
	  if(args[0] == "quit") {
		  quit();
	  } else if(args[0] == "logoff") {
		  try {
		      closeConnection();
		  }
		  catch(IOException e) {}
	  } else if(args[0] == "sethost") {
		  if(args.length == 1) // check if there enough parameters 
			  System.out.println("ERROR: sethost requires a 'host' parameter");
		  else setHost(args[1]);
	  } else if(args[0] == "setport") {
		  if(args.length == 1) // check if there enough parameters 
			  System.out.println("ERROR: sethost requires a 'port' parameter");
		  else {
			  int port;
			  try {
			      port = Integer.parseInt(args[1]); //Get port from command parameter
			      setPort(port);
			  }
			  catch(Throwable t) {
			      System.out.println("ERROR: port parameter needs to be an integer");
			  }
			  setHost(args[1]);
		  }
	  } else if(args[0] == "login") {
		  try {
		      openConnection();
		  }
		  catch(IOException e) {}
	  } else if(args[0] == "gethost") {
		  System.out.println("Host: " + getHost());
	  } else if(args[0] == "getport") {
		  System.out.println("Host: " + getPort());
	  } else if(args[0] == "help") {
		  System.out.println("Commands: quit, logoff, sethost <host>, setport <port>, login, gethost, getport");
	  } else {
		  System.out.println("Unrecognized command \"" + args[0] + "\" type #help for list of commands");
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /*
   * This method is called when the connection to the server is closed
   */
  @Override
  public void connectionClosed() {
	  System.out.println("Server has shut down.");
	  quit();
  }
  
  /* 
   * This method is called when the connection to the server fails
   */
  @Override
  public void connectionException(Exception e) {
	  System.out.println("Connection to server has failed: " + e);
	  quit();
  }
}
//End of ChatClient class
