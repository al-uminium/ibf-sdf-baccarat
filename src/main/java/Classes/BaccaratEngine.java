package Classes;

import java.util.LinkedList;

public class BaccaratEngine {
  private Deck gameDeck;
  private Deck playerDeck;
  private Deck bankerDeck;
  private String player;
  private FileHandler fileHandler;
  private LinkedList<String> gameHistory; 
  private int noOfDecks;
  private float playerPool;
  private float bet;

  public BaccaratEngine(int noOfDecks) {
    this.noOfDecks = noOfDecks;
    this.gameDeck = new Deck(generateDecks(noOfDecks));
    this.playerDeck = new Deck(true);
    this.bankerDeck = new Deck(true);
    this.fileHandler = new FileHandler();
    
    fileHandler.writeCardDB(this.gameDeck);
    if (fileHandler.csvFileExists()) {
      this.gameHistory = fileHandler.readCSV();
    } else {
      this.gameHistory = new LinkedList<>();
    }
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

    if (playerScore <= 5) {
      drawTo(this.playerDeck);
    }

    if (bankerScore <= 5) {
      drawTo(this.bankerDeck);
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
      updateGameHistory("P");
      fileHandler.writePlayerDB(this.player, this.playerPool);
    } else {
      this.playerPool -= this.bet;
      updateGameHistory("B");
      fileHandler.writePlayerDB(this.player, this.playerPool);
    }
    fileHandler.writeToCSV(this.gameHistory);
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
    this.gameHistory.add(winner);
  }
}
