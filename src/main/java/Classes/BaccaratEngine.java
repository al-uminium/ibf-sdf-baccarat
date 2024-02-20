package Classes;

import java.util.LinkedList;

public class BaccaratEngine {
  private Deck gameDeck;
  private Deck playerDeck;
  private Deck bankerDeck;
  private String player;
  private FileHandler fileHandler;
  private float playerPool;
  private float bet;

  public BaccaratEngine(int noOfDecks) {
    this.gameDeck = new Deck(generateDecks(noOfDecks));
    this.playerDeck = new Deck();
    this.bankerDeck = new Deck();
    this.fileHandler = new FileHandler();
    fileHandler.writeCardDB(this.gameDeck);
  }

  public Deck getGameDeck() {
    return this.gameDeck;
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

  public void setPlayerPool(int playerPool) {
    this.playerPool = playerPool;
  }

  // ----------- COMMANDS ----------------
  public void login(String playerName, int playerPool) {
    setPlayerName(playerName);
    setPlayerPool(playerPool);
    fileHandler.writePlayerDB(playerName, playerPool);
  }

  public void bet(int playerBet) {
    this.bet = playerBet;
  }

  public void deal() {
    // draw
    for (int i = 0; i <= 4; i++) {
      if (i <= 2) {
        drawTo(playerDeck);
      } else {
        drawTo(bankerDeck);
      }
    }

    // calculate score
    int playerScore = playerDeck.getScore();
    int bankerScore = bankerDeck.getScore();

    if (playerScore <= 5) {
      drawTo(playerDeck);
    }

    if (bankerScore <= 5) {
      drawTo(bankerDeck);
    }
  }

  // ------------ HELPER FUNCTIONS ---------------

  private LinkedList<Card> generateDecks(int noOfDecks) {
    LinkedList<Card> combinedDecks = new LinkedList<>();
    for (int i = 0; i < noOfDecks; i++) {
      Deck generatedDeck = new Deck();
      combinedDecks.addAll(generatedDeck.getDeck());
    }
    return combinedDecks;
  }

  private void drawTo(Deck deck) {
    // draws player cards first, then bankers
    Card drawnCard;
    drawnCard = this.gameDeck.drawCard();
    deck.putCardIntoDeck(drawnCard);
    fileHandler.writeCardDB(this.gameDeck);
  }

  public boolean isGameWonByPlayer(String choice) {
    int playerScore = playerDeck.getScore();
    int bankerScore = bankerDeck.getScore();

    // check for 6 card rule
    if ((bankerScore == 6) && (bankerDeck.getDeck().size() > 1)) {
      this.bet = this.bet/2;
    }

    if ((playerScore > bankerScore) && choice.equals("P") || (bankerScore > playerScore) && choice.equals("B")) {
      this.playerPool += this.bet;
      fileHandler.writePlayerDB(this.player, this.playerPool);
      return true;
    } else {
      this.playerPool -= this.bet;
    }

    return false;
  }

  public boolean isPlayerPoolSufficient() {
    if (this.playerPool > this.bet) {
      return true;
    }
    return false;
  }
}
