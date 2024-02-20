package main.Server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;

import Classes.BaccaratEngine;
import Classes.FileHandler;
import Classes.InputHandler;
import Classes.NetworkIO;

public class ServerApp {
  public static void main( String args[] ) {
    int portNum = 8443;
    int noOfDecks = 1;
    ServerSocket serverSock;

    // Setting up server.
    if (args.length > 1) {
      portNum = Integer.valueOf(args[0]);
      noOfDecks = Integer.valueOf(args[1]);
      System.out.println("Setting the following: \nPort number: " + portNum + "\nDeck(s): ." + noOfDecks);
    } else {
      System.out.println("Arguments not provided, setting the following: \nPort number: 8443\nDeck(s):1.");
    }

    try {
      serverSock = new ServerSocket(portNum);
      NetworkIO netIO = new NetworkIO(serverSock.accept());
      System.out.println("Server received connection, starting game.");
      // Initialize game
      game: while (true) {
        BaccaratEngine game = new BaccaratEngine(noOfDecks);
        round: while (true) {
          System.out.println("System awaiting input.");
          InputHandler input = new InputHandler(netIO.read());
          try {
            switch (input.getCommand()) {
              case "login":
                game.login(input.getPlayerName(), input.getAmount());
                netIO.write("Login in as: " + input.getPlayerName());
                break;
              case "bet":
                if (game.isPlayerPoolSufficient(input.getAmount())) {
                  netIO.write("Setting bet: " + input.getAmount());
                  game.bet(input.getAmount());   
                } else {
                  netIO.write("Not enough cash.");
                }
                break;
              case "deal":
                game.deal();
                game.gameCompleted(input.getDealChoice());
                String serverOutput = input.concatLinkedList(game.getPlayerDeck(), game.getBankerDeck());
                System.out.println("Sending the following output to client... " + serverOutput);
                netIO.write(serverOutput);
                break;
              case "exit":
                break game;
              case "Y":
                break round;
              default:
                System.out.println("Invalid input.");
                netIO.write("Invalid input.");
                break;
            }
          } catch (Exception e) {
            // TODO: handle exception
            System.out.println("That's all folks lol");
          }

      }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
