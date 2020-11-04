// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  /*
   * The infoType to access login id
   */
  final public static String LOGIN = "login";
  
//Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
  ChatIF serverUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) throws Exception
  {
    super(port);
    this.serverUI = serverUI;
    try {
      listen(); //Start listening for connections
    } 
    catch (Exception ex) {
    	serverUI.display("ERROR - Could not listen for clients!");
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
	  String[] args = msg.toString().split(" ");
	  serverUI.display("Message received: " + msg + " from " + client.getInfo(LOGIN));
	  
	  if(args.length == 2 && args[0].contains("#login")) { // check if already logged in
		  if(client.getInfo(LOGIN) == null) {// log user in
			  client.setInfo(LOGIN, args[1]);
			  serverUI.display("<" + args[1] +"> has logged on.");
			  this.sendToAllClients("<" + args[1] +"> has logged on.");
		  }
		  else { // not logged in, send error message and terminate connection
			  try {
				  client.sendToClient("ERROR: already logged in, terminating connection");
				  close();
			  }
			  catch(IOException e) {}
		  }
	  } else {
		  this.sendToAllClients(client.getInfo(LOGIN) + "> " + msg);
	  }
	  
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	  serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	  serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  public void handleMessageFromServerUI(String message)
  {
	  if(message.charAt(0) == '#') { // check if the message is a command
		  String command = message.substring(1);
		  handleCommandFromSeverUI(command);
	  } else {
		  this.sendToAllClients("SERVER MSG> " +message);
	  }
  }
  
  private void handleCommandFromSeverUI(String command) {
	  String[] args = command.split(" ");
	  if(args[0].contains("quit")) {
		  try {
				close();
			} catch (IOException e) {}
	  } else if(args[0].contains("stop")) {
		  stopListening();
	  } else if(args[0].contains("close")) {
		  stopListening();
		  // Close the client sockets of the already connected clients
	      Thread[] clientThreadList = getClientConnections();
	      for (int i=0; i<clientThreadList.length; i++) {
	    	  try {
	    		  ((ConnectionToClient)clientThreadList[i]).close();
	    	  }
	    	  // Ignore all exceptions when closing clients.
	    	  catch(Exception ex) {}
	      }
	  } else if(args[0].contains("setport")) {
		  if(args.length == 1) // check if there enough parameters 
			  serverUI.display("ERROR: setport requires a 'port' parameter");
		  else {
			  int port;
			  try {
			      port = Integer.parseInt(args[1]); //Get port from command parameter
			      setPort(port);
			  }
			  catch(Throwable t) {
				  serverUI.display("ERROR: port parameter needs to be an integer");
			  }
			  setPort(Integer.parseInt(args[1]));
		  }
	  } else if(args[0].contains("start")) {
		  try {
		      listen();
		  }
		  catch(IOException e) {}
	  }  else if(args[0].contains("getport")) {
		  serverUI.display("Port: " + getPort());
	  } else if(args[0].contains("help")) {
		  serverUI.display("Commands: quit, stop, close, setport <port>, start, getport");
	  } else {
		  serverUI.display("Unrecognized command \"" + args[0] + "\" type #help for list of commands");
	  }
  }
  
  /*
   * Prints message when a client connects to the server
   */
  @Override
  public void clientConnected(ConnectionToClient c) {
	  serverUI.display("A new client is attempting to connect to the server.");
  }
  
  /*
   * Prints message when a client disconnects from the server
   */
  @Override
  public void clientDisconnected(ConnectionToClient c) {
	  serverUI.display(c + " has disconnected from the server.");
  }
}
//End of EchoServer class
