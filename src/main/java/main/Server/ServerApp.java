package main.Server;

import java.io.IOException;
import java.net.ServerSocket;

import Classes.BaccaratEngine;
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
      System.out.println("Setting the following: \nPort number: " + portNum + "\nDeck(s): " + noOfDecks);
    } else {
      System.out.println("Arguments not provided, setting the following: \nPort number: 8443\nDeck(s): 1");
    }

    try {
      serverSock = new ServerSocket(portNum);
      // Initialize game
      NetworkIO netIO = new NetworkIO(serverSock.accept());
      System.out.println("Server received connection, starting game.");
      BaccaratEngine game = new BaccaratEngine(noOfDecks);
      boolean gameRoundOver = false;
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
              if (gameRoundOver) {
                System.out.println("Invalid option chosen");
                netIO.write("Please type in Y or N first.");
                break;
              } else if ((game.getBet() > 0) && game.isPlayerPoolSufficient(game.getBet())){
                game.deal();
                game.gameCompleted(input.getDealChoice());
                String serverOutput = input.concatLinkedList(game.getPlayerDeck(), game.getBankerDeck());
                System.out.println("Sending the following output to client... " + serverOutput);
                netIO.write(serverOutput);
                gameRoundOver = true;
                break;
              } else {
                netIO.write("Unable to deal because bet was not set or insufficient funds.");
                break;
              }
            case "exit":
              netIO.write("Exiting. Thanks for playing!");
              netIO.close();
              break round;
            case "Y":
              netIO.write("Starting new round... If you wish to make a new bet, you can, else the previous bet will be used.");
              gameRoundOver = false;
              game.resetRound();
              break;
            case "N":
              netIO.write("Thanks for playing!");
              netIO.close();
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
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
