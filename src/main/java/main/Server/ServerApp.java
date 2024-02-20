package main.Server;

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
    FileHandler fileHandler = new FileHandler();

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
        // Initialize game
        BaccaratEngine game = new BaccaratEngine(noOfDecks);
        game: while (true) {
          InputHandler input = new InputHandler(netIO.read());

          switch (input.getCommand()) {
            case "login":
              fileHandler.writePlayerDB(input.getPlayerName(), input.getAmount());
              break;
            case "bet":
              game.bet(input.getAmount());
              if (!game.isPlayerPoolSufficient()) {
                break game;
              }
              break;
            case "deal":
              game.deal();
              if (game.isGameWonByPlayer(input.getDealChoice())) {
                String serverOutput = input.concatLinkedList(game.getPlayerDeck(), game.getBankerDeck());
                netIO.write(serverOutput);
                netIO.close();
              }
              break game;
            default:
              break;
          }

          netIO.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

  }
}
