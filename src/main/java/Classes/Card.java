package Classes;

public class Card {
  private int cardValue;
  private int suiteValue;
  private String cardIdentifier;

  public Card(int cardValue, int suiteValue) {
    this.cardValue = cardValue;
    this.suiteValue = suiteValue;
    this.cardIdentifier = String.valueOf(cardValue) + "." + String.valueOf(suiteValue);
  }

  public Card(String cardIdentifier) {
    String[] cardValues = cardIdentifier.split("\\.");
    this.cardValue = Integer.valueOf(cardValues[0]);
    this.suiteValue = Integer.valueOf(cardValues[1]);
    this.cardIdentifier = cardIdentifier;
  }

  public int getCardValue() {
    return cardValue;
  }

  public int getSuiteValue() {
    return suiteValue;
  }

  public String getCardIdentifier() {
    return cardIdentifier;
  }
}
