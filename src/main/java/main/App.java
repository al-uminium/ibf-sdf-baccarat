package main;

import java.util.ArrayList;
import java.util.List;

import Classes.BaccaratEngine;
import Classes.Card;
import Classes.Deck;
import Classes.FileHandler;
import Classes.InputHandler;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String a = "P|10|10,B|2|5";
        String[] inputArr = a.split(",");
        String[] playerArr = inputArr[0].split("\\|");
        String[] bankerArr = inputArr[1].split("\\|");

        for (int i = 0; i < playerArr.length; i++) {
            System.out.println(playerArr[i]);
        }
    }
}
