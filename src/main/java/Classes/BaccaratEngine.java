package Classes;

import java.util.LinkedList;

public class BaccaratEngine {
  private Deck gameDeck;
  private Deck playerDeck;
  private Deck bankerDeck;
  private String player;
  private FileHandler fileHandler;
  private LinkedList<LinkedList<String>> gameHistory; 
  private int noOfDecks;
  private float playerPool;
  private float bet;

  public BaccaratEngine(int noOfDecks) {
    this.noOfDecks = noOfDecks;
    this.gameDeck = new Deck(generateDecks(noOfDecks));
    this.playerDeck = new Deck(true);
    this.bankerDeck = new Deck(true);
    this.fileHandler = new FileHandler();
    this.gameHistory = new LinkedList<>();
    fileHandler.writeCardDB(this.gameDeck);
  }

  public float getBet() {
    return bet;
  }

  public Deck getGameDeck() {
    return gameDeck;
  }

  public Deck getPlayerDeck() {
    return playerDeck;
  }

  public Deck getBankerDeck() {
    return bankerDeck;
  }

  public void setPlayerName(String playerName) {
    this.player = playerName;
  }

  public void setPlayerPool(float playerPool) {
    this.playerPool = playerPool;
  }

  // ----------- COMMANDS ----------------
  public void login(String playerName, float playerPool) {
    this.player = playerName;
    this.playerPool = playerPool;
    System.out.println("Player pool is: " + this.playerPool);
    fileHandler.writePlayerDB(this.player, this.playerPool);
  }

  public void bet(int playerBet) {
    this.bet = playerBet;
  }

  public void deal() {
    // draw
    drawTo(this.playerDeck);
    drawTo(this.playerDeck);
    drawTo(this.bankerDeck);
    drawTo(this.bankerDeck);
    
    // calculate score
    int playerScore = playerDeck.getScore();
    int bankerScore = bankerDeck.getScore();

    // System.out.println("Player score is: " + playerScore + "| " + this.playerDeck.getDeck().size());
    // for (Card card : this.playerDeck.getDeck()) {
    //   System.out.println(card.getCardValue());
    // }
    // System.out.println("Banker score is: " + bankerScore + "| " + this.bankerDeck.getDeck().size());
    // for (Card card : this.bankerDeck.getDeck()) {
    //   System.out.println(card.getCardValue());
    // }
    if (playerScore <= 5) {
      // System.out.println("Player has to draw additional. " + this.playerDeck.getDeck().size());
      drawTo(this.playerDeck);
      // for (Card card : this.playerDeck.getDeck()) {
      //   System.out.println(card.getCardValue());
      // }
    }

    if (bankerScore <= 5) {
      // System.out.println("Banker has to draw additional. " + this.bankerDeck.getDeck().size());
      drawTo(this.bankerDeck);
      // for (Card card : this.bankerDeck.getDeck()) {
      //   System.out.println(card.getCardValue());
      // }
    }
    System.out.println("Finished dealing.");
  }

  // ------------ HELPER FUNCTIONS ---------------

  private LinkedList<Card> generateDecks(int noOfDecks) {
    LinkedList<Card> combinedDecks = new LinkedList<>();
    for (int i = 0; i < noOfDecks; i++) {
      Deck generatedDeck = new Deck(false);
      combinedDecks.addAll(generatedDeck.getDeck());
    }
    return combinedDecks;
  }

  private void drawTo(Deck deck) {
      Card drawnCard = this.gameDeck.drawCard();
      deck.putCardIntoDeck(drawnCard);
      fileHandler.writeCardDB(this.gameDeck);
      System.out.println("Printing current deck size: " + this.gameDeck.getDeck().size());
  }

  public void gameCompleted(String choice) {
    int playerScore = playerDeck.getScore();
    int bankerScore = bankerDeck.getScore();

    // check for 6 card rule
    if ((bankerScore == 6) && (bankerDeck.getDeck().size() > 1)) {
      this.bet = this.bet/2;
    }

    if ((playerScore > bankerScore) && choice.equals("P") || (bankerScore > playerScore) && choice.equals("B")) {
      this.playerPool += this.bet;
      fileHandler.writePlayerDB(this.player, this.playerPool);
      updateGameHistory("P");
    } else {
      this.playerPool -= this.bet;
      fileHandler.writePlayerDB(this.player, this.playerPool);
      updateGameHistory("B");
    }
  }

  public boolean isPlayerPoolSufficient(float bet) {
    if (this.playerPool >= bet) {
      return true;
    }
    return false;
  }

  public void resetRound() {
    this.playerDeck = new Deck(true);
    this.bankerDeck = new Deck(true);
    if (this.gameDeck.getDeck().size() < 6) {
      this.gameDeck = new Deck(generateDecks(noOfDecks));
    }
  }

  private void updateGameHistory(String winner) {
    if (this.gameHistory.size() == 0 || this.gameHistory.size() == 6) {
      LinkedList<String> newGameHistory = new LinkedList<>();
      newGameHistory.add(winner);
      this.gameHistory.add(newGameHistory);
    } else {
      LinkedList<String> currentGameHistory = this.gameHistory.getLast();
      currentGameHistory.add(winner);
    }
  }
}
