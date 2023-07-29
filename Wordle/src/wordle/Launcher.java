package wordle;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Launcher
{
    public static void main(String[] args)
    {
	String wordPath = "/wordle/len";
	String iconPath = "/wordle/wordle_icon.png";
	try
	{
	    cheat();
	}
	catch (Exception ex) 
	{
	    System.out.println("File not found");
	}
	
	EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		try
		{
		    StartFrame window = new StartFrame(wordPath, iconPath);
		} 
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	});
    }
    
    public static void cheat() throws Exception
    {
	BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Aidan\\Documents\\Other\\words\\len6.txt"));
//	BufferedReader in = new BufferedReader(new FileReader("res/len5.txt"));
	
	for (String s = in.readLine(); s != null; s = in.readLine())
	{
	    int p = s.indexOf("P");
	    int e = s.indexOf("E");
	    if (p == 0 && e == 1 && s.length() == 5)
	    {
		System.out.println(s);
	    }
	}
	in.close();
    }
    
    public static void sortWordLength() throws Exception
    {
	//min: 3, max: 8
	int num = 4341;
	for (int i = 3; i <= 8; i++)
	{
	    System.out.println(i + " start");
	    BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Aidan\\Documents\\Other\\words\\all.txt"));
	    PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\Aidan\\Documents\\Other\\words\\len" + i + ".txt"));
	    
	    for (int j = 0; j < num; j++)
	    {
		String word = in.readLine();
		if (word.length() == i)
		{
		    out.println(word);
		}
	    }
	    
	    in.close();
	    out.close();
	    System.out.println(i + " end");
	}
    }
    
    public static void sortWords() throws Exception
    {
	BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Aidan\\Documents\\Other\\words\\all_sorted.txt"));
	PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\Aidan\\Documents\\Other\\words\\all_sorted2.txt"));
	
	int num = 4341;
	String[] words = new String[num];
	
	for (int i = 0; i < num; i++)
	{
	    words[i] = in.readLine().toUpperCase();
	}
	
	Arrays.sort(words);
	
	for (int i = 0; i < num; i++)
	{
	    out.println(words[i]);
	}
	
	in.close();
	out.close();
    }
    
    public static void findWords() throws Exception
    {
	BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Aidan\\Documents\\Other\\words\\all.txt"));
	PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\Aidan\\Documents\\Other\\words\\all_sorted.txt"));
	StringTokenizer st;
	
	while (true)
	{
	    String line = in.readLine();
	    if (line == null) break;
	    st = new StringTokenizer(line);
	    while (st.hasMoreTokens())
	    {
		out.println(st.nextToken());
	    }
	}

	in.close();
	out.close();
    }

}
