package wordle;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Wordle
{
    public static final int A = 65;
    private String word, wordPath;
    private String[] wordList;
    private int numGuesses, wordLength, currentGuess;
    private char[][] guesses;
    private Color[][] colors;
    private Color[] letters;
    private Display display;
    public Wordle(int wordLength, int numGuesses, String wordPath, String iconPath)
    {
	this.wordLength = wordLength;
	this.numGuesses = numGuesses;
	this.wordPath = wordPath;
	currentGuess = 0;
	guesses = new char[numGuesses][wordLength];
	colors = new Color[numGuesses][wordLength];
	letters = new Color[26];
	
	initialize();
	
	display = new Display(wordLength, numGuesses, colors, guesses, letters, this, iconPath);
    }
    
    private void initialize()
    {
	for (int i = 0; i < numGuesses; i++)
	{
	    for (int j = 0; j < wordLength; j++)
	    {
		colors[i][j] = ColorList.empty;
	    }
	}
	for (int i = 0; i < letters.length; i++)
	{
	    letters[i] = ColorList.empty;
	}

	try
	{
	    initializeWordList(wordPath + wordLength + ".txt");
    	    word = wordList[(int)(Math.random() * wordList.length)];
    	    
//    	    System.out.println("Word is " + word); //cheat here 
	}
	catch (Exception ex) 
	{
	    ex.printStackTrace();
	    System.out.println("Unable to find file.");
	}
    }
    
    private void initializeWordList(String endPath)
    {
        InputStream in = this.getClass().getResourceAsStream(endPath);
        InputStreamReader fr = null;
        try 
        {
            fr = new InputStreamReader(in, "utf-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            System.out.println("InputStreamReader failed");
        }

        BufferedReader br = new BufferedReader(fr);

        ArrayList<String> wordArrayList = new ArrayList<String>();
        String output;
        try 
        {
            while ((output = br.readLine()) != null) 
            {
                wordArrayList.add(output);
            }
        } 
        catch (IOException ex) 
        {
            System.out.println("Could not build output");
        }
        
        wordList = new String[wordArrayList.size()];
        for (int i = 0; i < wordList.length; i++)
        {
            wordList[i] = wordArrayList.get(i);
        }
    }
    
    public void update(String guess)
    {
	if (currentGuess >= numGuesses) return;
	guess = guess.toUpperCase();
	guesses[currentGuess] = guess.toCharArray();
	updateColors(guess, currentGuess);
	display.update(colors, guesses, letters);
	
	currentGuess++;
	if (guess.equals(word))
	{
	    display.updateState("You Won!");
	}
	else if (currentGuess == numGuesses)
	{
	    display.updateState("You Lost. The word was " + word);
	}
    }
    
    public boolean checkGuessValidity(String guess)
    {
	if (guess.length() != wordLength) return false;
	for (int i = 0; i < wordList.length; i++)
	{
	    if (wordList[i].equals(guess))
	    {
		return true;
	    }
	}
	return false;
    }
    
    private void updateColors(String guess, int currentGuess)
    {
	for (int i = 0; i < wordLength; i++)
	{
	    if (word.charAt(i) == guess.charAt(i))
	    {
		colors[currentGuess][i] = ColorList.correct;
		letters[guess.charAt(i) - A] = ColorList.correct;
	    }
	    else
	    {
		colors[currentGuess][i] = ColorList.wrong;
		letters[guess.charAt(i) - A] = ColorList.wrong;
	    }
	}
	
	for (int i = 0; i < wordLength; i++)
	{
	    if (colors[currentGuess][i] == ColorList.correct) continue;
	    for (int j = 0; j < wordLength; j++)
	    {
		if (colors[currentGuess][j] == ColorList.correct) continue;
		if (guess.charAt(i) == word.charAt(j)) 
		{
		    colors[currentGuess][i] = ColorList.misplaced;
		    letters[guess.charAt(i) - A] = ColorList.misplaced;
		}
	    }
	}
    }
}
