// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
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
  
  private static final String LOGIN_ID = "loginID";
  private static final String[] bonk = 
  {
    "                    ``..` ````...-:/+/`                                                             ",
    "                   :ssysssyyysssssssso/`                                                            ",
    "                 `/hyyhyssysysssssssss+:`                                                           ",
    "                `+hdssssyysssssooooooo++//`               *BONK*                    .-.             ",
    "               .oyhdhyhyyssssooossoooo++sd+                                       .+ss+.            ",
    "              `oyyyyyyyysoossydmmmhsosooshs                                     ./sso//.            ",
    "            `-ossssssssso+/::/oyyo+shhhhhys`                                ` `/ssso+:`             ",
    "         `./syyyyyssysso++//::::///ossyyhhhs:`                              `:ssso+:`               ",
    "       `-+ssssyyyyyyyssso++//::://+oooosyddmdy.                           `-ossso:`                 ",
    "     `-+ssssssssyyyhyyyysoo+///osysoooooydmmmm-                         `-+ssso:`                   ",
    "    `/yyhysssssssssyyyyyyyss+/+syddhysssshddy/`                      ``.+ssso:``                    ",
    "   `/yhhhhhysssooooooooooooo+//+oosyyyys/-.`                        ``/ssss+-.-::-``````            ",
    "   :syhhhyyyhyssoo+++/////////:::/++o++:`                       ```.:osssyyyhhhhhhyso/-```          ",
    "  .sssyyhdyyyyssooo++++////:::::::::::/-                       ``-/ossssyyyyyhhhhddddhyo:``         ",
    " .shyyyyyhhhysso++++++//////::::::::::/-                       `:ossyyyyhyysoyhhhddddhhhy+.`        ",
    "`sddhyyyyyhhyyyso/////:::/:::::::::////:                    ```/osyhhhhhyo+//osyhhhhhhhhhho.`       ",
    "-yhhhhyyyyyhyyyyss+///::///:::::::/++++:                   ``:osydddhhyysso+++osyyyhhhhhhhh+`       ",
    ".yyhhhyyyyyyhhyyyyso++////////////+++++:                  `-osydddhyyyyyyys++oossyyyhhhyhhhh+. `    ",
    ".yyyhyyyyyyyhhyyyysssoo+++//////++//+++/                `-+ssdmmdhyssyyhdyo+++ossyyyhhhyhhhhhs.``   ",
    "`ohhhyyhyyyyhhhhyyssssoo++++++oo++////+/`              .+s+./NNNmyssyyddhs+++oossyyhhhhhhhhhhhs-`   ",
    " `+yyyyhhhhhhdddhysssso++oooooyhyo//////`            ./s+.``:mmmdyyyhddhyo+++oossyyyhhhhhhhhhhhs.`  ",
    "   -ohhhhhhhddddyyssoo++oooosyhhhs+////+++/::-.``  `/s+.`   `:ossyhhhhyso++++oossyyhhhhhhhhyhhhy/`  ",
    "    `:shhhhhddddyyssoo+++++oosyhhyo+////:/++++oo+//so-`       ```:syysoo+++++ossyhhhhhhhhhyyyyyhs.` ",
    "       ./shdddddyyyyso++///++++osyyso+//.   ``.-/so-`           `-oooooo++ooosyyhhhhhhhhhhyyyyyhh/``",
    "          `-/ohdhyhyso+++////////+sso++++/::---os:`             `-ooo++oooossyyhhhhhhhhyyyyyyyhhdo` ",
    "              `.:+yyyso++/-..````````./+++++oo++os-             `-ooo+++++ossyyyyyyyyyyyyyyyyhhddy. ",
    "                  -yyso+.              .:////++++:`             `-+++++++++oossssssssssssyyhhhhhdd/`",
    "                  `oyso+-                `-://::-               `-++++++++++oooooooooossyyhhhhhhdd+`",
    "                  `oysoo/`                                       .::::::::::://///////+++++oooooss: "
  };

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    try 
    {
      String message = msg.toString().trim();
      Object id = client.getInfo(LOGIN_ID);
      if (id == null) 
      {
        if (message.startsWith("#login")) 
        {
          int loginID = Integer.parseInt(message.substring(6).trim());// 6 -> # l o g i n
          client.setInfo(LOGIN_ID, loginID);
        } 
        else 
        {
          client.sendToClient("You need to send the login command first *BONK*");
          bonk(client);
          client.close();
        }
      } 
      else 
      {
        if (message.startsWith("#login")) 
        {
          client.sendToClient("You have already logged in, get out of here. *BONK*");
          bonk(client);
          client.close();
        }
        else
        {
          System.out.println("Message received: " + msg + " from " + client + " with id " + id);
          this.sendToAllClients(id + "> " + msg);
        }
      }
      
    } catch (Exception e) {
      // ignore
    }
  }

  private void bonk(ConnectionToClient client)
  {
    try 
    {
      for (int i = 0; i < bonk.length; i++) {
        client.sendToClient(bonk[i]);
      }
    } catch (Exception e) 
    {
    }
    
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  public void clientConnected(ConnectionToClient client)
  {
    System.out.println("This is a nice message, a client " + client + " just connected!");
  }

  public void clientDisconnected(ConnectionToClient client)
  {
    System.out.println("This is another nice message, a client just disconnected!");
  }

  public void clientException(ConnectionToClient client, Throwable exception)
  {
    System.out.println("This is another nice message, a client just disconnected!");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    ServerConsole sc = new ServerConsole(sv);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    sc.activate();
  }
}
//End of EchoServer class
