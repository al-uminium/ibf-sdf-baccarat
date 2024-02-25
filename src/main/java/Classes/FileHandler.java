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
import java.util.Scanner;

public class FileHandler {
  private File csv;
  private File cardDB;
  private File playerDB;

  public FileHandler() {
    this.csv = new File("src" + File.separator + "website" + File.separator + "game_history.csv");
    this.cardDB = new File("card.db");
  }

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

  public void writeToCSV(LinkedList<String> gameHistory) {
    try {
      FileWriter writer = new FileWriter(csv);
      String gameHistoryString = gameHistoryListToStr(gameHistory);
      writer.write(gameHistoryString);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public LinkedList<String> readCSV() {
    LinkedList<String> previousHistory = new LinkedList<>();
    try {
      Scanner scan = new Scanner(csv).useDelimiter(",");
      while (scan.hasNext()) {
        previousHistory.add(scan.next());
      }
      scan.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } 
    return previousHistory;
  }

  public boolean csvFileExists() {
    if (csv.isFile()){
      return true;
    }
    return false;
  }

  private String gameHistoryListToStr(List<String> gameHistory) {
    String msg = "";

    for (int i = 0; i < gameHistory.size(); i++) {
      if ((i % 5) == 0 && (i != 0)) {
        msg += gameHistory.get(i) + "\n";
      } else {
        msg += gameHistory.get(i) + ",";        
      }
    }

    return msg;
  }
}
