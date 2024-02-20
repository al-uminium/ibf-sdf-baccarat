package Classes;

import java.util.LinkedList;

public class Deck {
  private LinkedList<Card> deck;

  public Deck() {
    this.deck = new LinkedList<>();
  }

  public Deck(LinkedList<Card> deck) {
    this.deck = deck;
  }

  public LinkedList<Card> getDeck() {
    return deck;
  }

  public int getScore() {
    int score = 0;
    for (Card card : this.deck) {
      int cardValue = card.getCardValue();
      score += cardValue;
    }
    return score%10;
  }

  public Card drawCard() {
    return this.deck.removeFirst();
  }

  public void putCardIntoDeck(Card card) {
    this.deck.add(card);
  }
  
  public void printDeck() {
    for (Card card : deck) {
      System.out.println(card.getCardIdentifier());
    }
  }
}
