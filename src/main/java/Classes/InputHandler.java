package Classes;

import java.util.LinkedList;

public class InputHandler {
  private String input;
  private String command;
  private String playerName;
  private int amount;
  private String dealChoice;

  public InputHandler(String input) {
    this.input = input;
    String[] inputArr = this.input.split(" ");
    this.command = inputArr[0].trim();
    switch (this.command) {
      case "login":
        this.playerName = inputArr[1].trim();
        this.amount = Integer.valueOf(inputArr[2]);
        break;
      case "deal":
        this.dealChoice = inputArr[1].trim().toUpperCase();
        break;
      case "bet":
        this.amount = Integer.valueOf(inputArr[1]);
      default:
        break;
    }
  }

  public boolean inputIsValid(){
    // making sure input is sanitized to a certain extent
    String[] inputArr = this.input.split(" ");

    if (inputArr.length > 3) {
      return false;
    }
    
    switch (inputArr[0]) {
      case "login":
        if (isInteger(inputArr[2])) {
          return true;
        }
        break; 
      case "bet":
        if (isInteger(inputArr[1])) {
          return true;
        }
        break;
      case "deal":
        if (inputArr.length == 2 || inputArr[1].toUpperCase().contains("P") || inputArr[1].toUpperCase().contains("B")) {
          return true;
        }
      case "Y":
        return true;
      case "N":
        return true;
      case "exit":
        return true;
      default:
        break;
    }
    return false;
  }

  public String getInput() {
    return input;
  }

  public String getCommand() {
    return command;
  }

  public String getPlayerName() {
    return playerName;
  }

  public int getAmount() {
    return amount;
  }

  public String getDealChoice() {
    return dealChoice;
  }

  public String concatLinkedList(Deck deck, Deck deck2) {
    String playerString = "";
    String bankerString = "";
    LinkedList<Card> playerDeck = deck.getDeck();
    LinkedList<Card> bankerDeck = deck2.getDeck();
    for (int i = 0; i < playerDeck.size(); i++) {
      playerString += playerDeck.get(i).getCardValue() + "|";
    }
    for (int i = 0; i < bankerDeck.size(); i++) {
      bankerString += bankerDeck.get(i).getCardValue() + "|";
    }
    return "P|" + playerString + ",B|" + bankerString;
  }
  
  public String parseServerInput() {
    String[] inputArr = this.input.split(",");
    String[] playerArr = inputArr[0].split("\\|");
    String[] bankerArr = inputArr[1].split("\\|");

    // get rid of P/B 
    playerArr[0] = "0";
    bankerArr[0] = "0";

    int playerScore = getScore(playerArr)%10;
    int bankerScore = getScore(bankerArr)%10;
    System.out.println(playerScore + " " + bankerScore);

    if (playerScore > bankerScore) {
      return "Player wins with " + playerScore + " points.";
    } else if (playerScore == bankerScore) {
      return "The game resulted in a draw.";
    }

    return "Banker wins with " + bankerScore + " points.";
  }

  // -------------------- Helper functions ------------------------
  private int getScore(String[] arr) {
    int score = 0;
    for (int i = 0; i < arr.length; i++) {
      int value = Integer.valueOf(arr[i]);
      if (value < 10) {
        score += value;
      }
    }
    return score;
  }

  private boolean isInteger(String no) {
    try {
      int i = Integer.valueOf(no);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
