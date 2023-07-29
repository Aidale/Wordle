package wordle;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class StartFrame
{
    private JFrame frame;
    private String wordPath, iconPath;
    public StartFrame(String wordPath, String iconPath)
    {
	this.wordPath = wordPath;
	this.iconPath = iconPath;
	initialize();
    }

    private void initialize()
    {
	int minLength = 3, maxLength = 8;
	Integer[] wordLengths = new Integer[maxLength - minLength + 1];
	for (int i = 0; i < wordLengths.length; i++)
	{
	    wordLengths[i] = new Integer(i + minLength);
	}
	
	int minGuess = 1, maxGuess = 12;
	Integer[] guesses = new Integer[maxGuess - minGuess + 1];
	for (int i = 0; i < guesses.length; i++)
	{
	    guesses[i] = new Integer(i + minGuess);
	}
	
	frame = new JFrame();
	frame.setBounds(100, 100, 258, 300);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	frame.getContentPane().setLayout(null);
	
	BufferedImage img = null;
	try
	{
	    img = ImageIO.read(new File(iconPath));
	}
	catch (IOException e)
	{
	    System.out.println("Image icon failed to load");
	}
	
	if (img != null) frame.setIconImage(img);
	
	JLabel wordLengthLabel = new JLabel("Choose the length of the word");
	wordLengthLabel.setBounds(10, 10, 193, 13);
	frame.getContentPane().add(wordLengthLabel);
	
	JComboBox<Integer> wordLengthBox = new JComboBox<Integer>(wordLengths);
	wordLengthBox.setBounds(10, 33, 132, 21);
	frame.getContentPane().add(wordLengthBox);
	
	JLabel guessNumLabel = new JLabel("Choose the number of guesses");
	guessNumLabel.setBounds(10, 89, 185, 13);
	frame.getContentPane().add(guessNumLabel);
	
	JComboBox<Integer> guessNumBox = new JComboBox<Integer>(guesses);
	guessNumBox.setBounds(10, 112, 142, 21);
	frame.getContentPane().add(guessNumBox);
	
	JButton playButton = new JButton("Play");
	playButton.addActionListener(new ActionListener()
	{
	    public void actionPerformed(ActionEvent e) 
	    {
		if (wordLengthBox.getSelectedIndex() == -1 || guessNumBox.getSelectedIndex() == -1) return;
		int wordLength = ((Integer)wordLengthBox.getSelectedItem()).intValue();
		int numGuesses = ((Integer)guessNumBox.getSelectedItem()).intValue();
		
		frame.setVisible(false);
		Wordle wordle = new Wordle(wordLength, numGuesses, wordPath, iconPath);
	    }
	});
	playButton.setBounds(10, 182, 85, 21);
	frame.getContentPane().add(playButton);
    }
}
