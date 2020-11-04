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
    message = message.trim();
    if (message.length() == 0)
      return;
    if (message.charAt(0) == '#')
    {
      try 
      {
        message = message.substring(1);
        String[] commands = message.split(" ");
        if (commands.length == 0) // not command given
        {
          clientUI.display("Not command given");
          return;
        }
        switch (commands[0]) {
          case "quit":
            quit();
            break;
          case "logoff":
            if (!isConnected()) // if not already connected
            {
              clientUI.display("No current connection");
              return;
            }
            closeConnection();
            clientUI.display("Logged off");
            break;
          case "sethost":
            if (isConnected()) // if already connected, display error msg
            {
              clientUI.display("Cannot set host while connected to a server");
              return;
            }
            if (commands.length < 2) // if no host in command
            {
              clientUI.display("Please simplify a host: #sethost <host>");
              return;
            }
            setHost(commands[1]);
            clientUI.display("Host set to " + getHost());
            break;
          case "setport":
            if (isConnected()) // if already connected, display error msg
            {
              clientUI.display("Cannot set port while connected to a server");
              return;
            }
            if (commands.length < 2) // if no port in command
            {
              clientUI.display("Please simplify a port: #setport <port>");
              return;
            }
            setPort(Integer.parseInt(commands[1]));
            clientUI.display("Port set to " + String.valueOf(getPort()));
            break;
          case "login":
            if (isConnected()) // if already connected
            {
              clientUI.display("Already connected");
              return;
            }
            openConnection();
            clientUI.display("Logged in");
            break;
          case "gethost":
            clientUI.display(getHost());
            break;
          case "getport":
            clientUI.display(String.valueOf(getPort()));
            break;
          default:
            clientUI.display("Unknown Command");
            break;
        }
      } 
      catch (Exception e) 
      {
        clientUI.display("An exception " + e.getLocalizedMessage() + " occurred at following location.  Terminating client.");
        e.printStackTrace();
        quit();
      }
      
    }
    else
    {
      try 
      {
        sendToServer(message);
      } 
      catch (IOException e) 
      {
        clientUI.display("Could not send message to server.  Terminating client.");
        quit();
      }
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

  public void connectionClosed()
  {
    //clientUI.display("Connection closed.");
  }

  public void connectionException(Exception exception)
  {
    clientUI.display("The server has shut down. Terminating client.");
    quit();
  }

}
//End of ChatClient class
