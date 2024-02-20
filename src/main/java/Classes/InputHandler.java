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
    String[] inputArr = parseInput();
    switch (inputArr[0]) {
      case "login":
        this.playerName = inputArr[1];
        this.amount = Integer.valueOf(inputArr[2]);
        break;
      case "deal":
        this.dealChoice = inputArr[1];
        break;
      case "bet":
        this.amount = Integer.valueOf(inputArr[1]);
      default:
        break;
    }
  }

  private String[] parseInput(){
    String[] splitInput = this.input.split(" ");

    if (splitInput.length > 3) {
      System.out.println("Invalid command received.");
      return null;
    }

    return splitInput;
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
    
    for (int i = 0; i < playerDeck.size()-1; i++) {
      playerString += playerDeck.get(i) + "|";
    }

    for (int i = 0; i < bankerDeck.size()-1; i++) {
      bankerString += bankerDeck.get(i) + "|";
    }
    
    return playerString + "," + bankerString;

  }
  
  public String parseServerInput() {
    String[] inputArr = this.input.split(",");
    String[] playerArr = inputArr[0].split("|");
    String[] bankerArr = inputArr[1].split("|");

    int playerScore = getScore(playerArr)%10;
    int bankerScore = getScore(bankerArr)%10;

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
      score += Integer.valueOf(arr[i]);
    }
    return score;
  }
}
