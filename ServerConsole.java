import java.util.Scanner;

import client.*;
import common.*;

public class ServerConsole implements ChatIF 
{
    EchoServer echoServer;
    Scanner fromConsole;

    public ServerConsole(EchoServer echoServer)
    {
        this.echoServer = echoServer;
        fromConsole = new Scanner(System.in);
    }

    public void handleMessageFromServerUI(String message)
    {
        try 
        {
            message = message.trim();
            if (message.length() == 0)
                return;
            if (message.charAt(0) == '#')
            {
                message = message.substring(1);
                String[] commands = message.split(" ");
                if (commands.length == 0) // not command given
                {
                    display("Not command given");
                    return;
                }
                switch (commands[0]) {
                    case "quit":
                        quit();
                        break;
                    case "stop":
                        echoServer.stopListening();
                        break;
                    case "close":
                        echoServer.close();
                        break;
                    case "start":
                        echoServer.listen();
                        break;
                    case "setport":
                        if (echoServer.isListening()) // if is listening, display error msg
                        {
                            display("Cannot set port while server is open");
                            return;
                        }
                        if (commands.length < 2) // if no port in command
                        {
                            display("Please simplify a port: #setport <port>");
                            return;
                        }
                        echoServer.setPort(Integer.parseInt(commands[1]));
                        display("Port set to " + String.valueOf(echoServer.getPort()));
                        break;
                    case "getport":
                        display(String.valueOf(echoServer.getPort()));
                        break;
                    default:
                        display("Unknown Command");
                        break;
                }
            }
            else
            {
                display(message);
                echoServer.sendToAllClients(message);
            }      
        } 
        catch (Exception e) 
        {
            display("An exception " + e.getLocalizedMessage()
                    + " occurred at following location.");
            e.printStackTrace();
        }
    }

    /**
     * start to handle input from server ui
     */
    public void activate()
    {
        try 
        {
            String msg;
            while (true) 
            {
                msg = fromConsole.nextLine();
                handleMessageFromServerUI(msg);
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    private void quit()
    {
        try 
        {
            echoServer.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        System.exit(0);
    }

    @Override
    public void display(String message) 
    {
        System.out.println("SERVER MSG> " + message);
    }
    
}
