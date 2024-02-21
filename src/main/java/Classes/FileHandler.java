package Classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.LinkedList;
import java.util.List;

public class FileHandler {

  private FileWriter getFileWriter(File file) throws IOException {
    FileWriter writer;
    // overwrite if exists, create if doesn't
    if (file.exists()) {
      writer = new FileWriter(file, false);
    } else {
      writer = new FileWriter(file);
    }
    return writer;
  }

  public void writeCardDB(Deck currentDeck) {
    try {
      File cardDB = new File("card.db");
      FileWriter writer = getFileWriter(cardDB);      

      for (Card card : currentDeck.getDeck()) {
        writer.write(card.getCardIdentifier() + "\n");
      }
      writer.close();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  public Deck readCardDB() {
    LinkedList<Card> cardDBList = new LinkedList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader("card.db"));
      String line = reader.readLine();
      while (line != null) {
        System.out.println(line);
        cardDBList.add(new Card(line));
        line = reader.readLine();
      }
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new Deck(cardDBList);
  }

  public void writePlayerDB(String playerName, float playerPool) {
    try {
      File playerDB = new File(playerName + ".db");
      System.out.println(playerPool);
      FileWriter writer = getFileWriter(playerDB);
      writer.write(String.valueOf(playerPool));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String readPlayerDB(String playerName) {
    String currentPool = "";
    try {
      BufferedReader reader = new BufferedReader(new FileReader(playerName + ".db"));
      currentPool = reader.readLine();
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return currentPool;
  }

  public void writeToCSV(List<List<String>> gameHistory) {
    File csv = new File("game_history.csv");
    try {
      FileWriter writer = new FileWriter(csv);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
