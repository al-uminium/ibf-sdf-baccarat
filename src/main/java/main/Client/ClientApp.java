package main.Client;

import java.io.IOException;
import java.net.Socket;

import Classes.InputHandler;
import Classes.NetworkIO;

public class ClientApp {
  public static void main(String[] args) {
    String ipAddress = "localhost";
    int portNum = 8443;
    
    if (args.length > 1) {
      String[] argVar = args[0].split(":");
      ipAddress = argVar[0];
      portNum = Integer.valueOf(argVar[1]);
      System.out.println("Attempting to connect to " + ipAddress + ":" + portNum);
    } else {
      System.out.println("No arguments detected. Attempting to connect to " + ipAddress + ":" + portNum);
    }

    try {
      NetworkIO netIO = new NetworkIO(new Socket(ipAddress, portNum));
      while (true) {
        String clientInput = System.console().readLine();
        if (clientInput.equals("exit")) {
          break;
        }
        netIO.write(clientInput);
        String serverInput = netIO.read(); 

        if (serverInput.contains("|")) {
          InputHandler inputHandler = new InputHandler(serverInput);
          inputHandler.parseServerInput();
          break;
        }
      }

    } catch (IOException e) {
      System.out.println("The server that was attempted to connect is not running, or the wrong IP/port is given. Unable to connect.");
      e.printStackTrace();
    }
  }
}
