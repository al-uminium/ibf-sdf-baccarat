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
      System.out.println("Connected to server. Type in your input...");

      while (true) {
        InputHandler clientInput = new InputHandler(System.console().readLine("> "));
        String serverInput = null;

        if (clientInput.inputIsValid()) {
          if (clientInput.getInput().equals("exit")) {
            netIO.close();
            break;
          }
          netIO.write(clientInput.getInput());
          serverInput = netIO.read();
          System.out.println(serverInput);
          // Check if game is finished. 
          if (serverInput.contains("|")) {
            InputHandler inputHandler = new InputHandler(serverInput);
            String msg = inputHandler.parseServerInput();
            System.out.println(msg);
            System.out.println("Round has finished. Continue? Type in Y for Yes or N for No.");
          }
        } else {
          netIO.write("Invalid response given");
        }
      }
    } catch (IOException e) {
      System.out.println("The server that was attempted to connect is not running, or the wrong IP/port is given. Unable to connect.");
    }
  }
}
