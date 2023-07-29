package wordle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Display extends JPanel 
{
    //SPG = SIZE_PER_GRID
    //SPL = SIZE_PER_LETTER
    //PG = PADDING_GRID
    //PL = PADDING_LETTER
    //XOF = X_OFFSET_LETTERS
    //XL = X_LETTERS
    //YL = Y_LETTERS
    //LS = LETTER_SCALE
    //XPG = X_POS_GRID
    //YPG = Y_POS_GRID
    //XPL = X_POS_LETTERS
    //YPL = Y_POS_LETTERS
    //AG = ARC_GRID
    //AL = ARC_LETTER
    //FH = FIELD_HEIGHT
    
    private Color[][] colors;
    private Color[] letters;
    private char[][] guesses;
    private JFrame jframe;
    private int MARGIN, PL, PG, SPG, SPL, XOL, WIDTH, HEIGHT, XL, YL, AG, AL;
    private float LS;
    private int[] XPG, YPG, XPL, YPL;
    private int wordLength, numGuesses;
    private Font font;
    private Graphics2D g2d;
    private Wordle wordle;
    private JLabel state;
    private String iconPath;
    public Display(int wordLength, int numGuesses, Color[][] colors, char[][] guesses, Color[] letters, Wordle wordle, String iconPath)
    {
	this.wordLength = wordLength;
	this.numGuesses = numGuesses;
	this.colors = colors;
	this.guesses = guesses;
	this.letters = letters;
	this.wordle = wordle;
	this.iconPath = iconPath;

	initialize();
    }
    
    private void initialize()
    {
	LS = 0.5f;
	MARGIN = 20;
	PG = 10;
	SPG = 50;
	AG = 15;
	XL = 6;
	YL = 5;
	
	PL = (int)(PG * LS);
	SPL = (int)(SPG * LS);
	AL = (int)(AG * LS);
	
	int FH = 25;
	int HEIGHT_FIELD = 2 * MARGIN + 3 * FH + 2 * PL;
	int WIDTH_GRID = 2 * MARGIN + wordLength * (PG + SPG) - PG;
	int HEIGHT_GRID = 2 * MARGIN + numGuesses * (PG + SPG) - PG;
	int WIDTH_LETTERS = 2 * MARGIN + XL * (PL + SPL) - PL;
	int HEIGHT_LETTERS = 2 * MARGIN + YL * (PL + SPL) - PL;
	WIDTH = WIDTH_GRID + WIDTH_LETTERS;
	HEIGHT = (int)(Math.max(HEIGHT_GRID, HEIGHT_LETTERS + HEIGHT_FIELD));
	
	XOL = WIDTH_GRID;
	
	XPG = new int[wordLength];
	for (int x = 0; x < XPG.length; x++) XPG[x] = MARGIN + x * (SPG + PG);
	YPG = new int[numGuesses];
	for (int y = 0; y < YPG.length; y++) YPG[y] = MARGIN + y * (SPG + PG);
	XPL = new int[XL];
	for (int x = 0; x < XPL.length; x++) XPL[x] = MARGIN + x * (SPL + PL) + XOL;
	YPL = new int[YL];
	for (int y = 0; y < YPL.length; y++) YPL[y] = MARGIN + y * (SPL + PL);
	
	font = new Font("TimesRoman", Font.PLAIN, 18);
	
	jframe = new JFrame("Wordle");
	jframe.setSize(WIDTH + 16, HEIGHT + 40);
	jframe.setResizable(false);	
	jframe.setVisible(true);	
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.add(this);
	
	BufferedImage img = null;
	try
	{
	    img = ImageIO.read(new File(iconPath));
	}
	catch (IOException e)
	{
	    System.out.println("Image icon failed to load");
	}
	
	if (img != null) jframe.setIconImage(img);
	
	JTextField textField = new JTextField();
	textField.setBounds(XOL + MARGIN, HEIGHT_LETTERS + MARGIN, WIDTH_LETTERS - 2 * MARGIN, FH);
	add(textField);
	
	JButton button = new JButton("Guess");
	button.setBounds(XOL + MARGIN, HEIGHT_LETTERS + MARGIN + FH + PL, WIDTH_LETTERS - 2 * MARGIN, FH);
	add(button);
	button.addActionListener(new ActionListener() 
	{
	    public void actionPerformed(ActionEvent evt)
	    {
		String guess = textField.getText().toUpperCase();
		if (wordle.checkGuessValidity(guess))
		{
		    wordle.update(textField.getText());
		    textField.setText("");
		}
	    }
	});
	
	state = new JLabel();
	state.setBounds(XOL + MARGIN, HEIGHT_LETTERS + MARGIN + 2 * (FH + PL), WIDTH_LETTERS - 2 * MARGIN, FH);
	add(state);
	
	setLayout(new BorderLayout());
    }
    public void updateState(String update)
    {
	state.setText(update);
    }
    
    public void update(Color[][] colors, char[][] guesses, Color[] letters)
    {
	this.colors = colors;
	this.guesses = guesses;
	this.letters = letters;
	
	repaint();
    }
    
    public void paintComponent(Graphics g)
    {
	g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setFont(font);
	
        drawBackground(g2d);
	drawGrid(g2d);
	drawLetters(g2d);
    }
    
    private void drawGrid(Graphics2D g2d)
    {
	for (int y = 0; y < numGuesses; y++)
	{
	    for (int x = 0; x < wordLength; x++)
	    {   
		g2d.setColor(colors[y][x]);
		g2d.fillRoundRect(XPG[x], YPG[y], SPG, SPG, AG, AG);
//		g2d.fillRect(XPG[x], YPG[y], SPG, SPG);
		
		g2d.setColor(ColorList.letter);
		Rectangle rect = new Rectangle(XPG[x], YPG[y], SPG, SPG);
		drawCenteredString(g2d, guesses[y][x] + "", rect);
	    } 
	}
    }
    
    private void drawLetters(Graphics2D g2d)
    {
	for (int y = 0; y < YL; y++)
	{
	    for (int x = 0; x < XL; x++)
	    {
		int letterNum = y * XL + x;
		if (letterNum >= letters.length) return;
		
		g2d.setColor(letters[letterNum]);
		g2d.fillRoundRect(XPL[x], YPL[y], SPL, SPL, AL, AL);
//		g2d.fillRect(XPL[x], YPL[y], SPL, SPL);
		
		g2d.setColor(ColorList.letter);
		Rectangle rect = new Rectangle(XPL[x], YPL[y], SPL, SPL);
		drawCenteredString(g2d, (char)(letterNum + Wordle.A) + "", rect);
	    }
	}
    }
    private void drawBackground(Graphics2D g2d)
    {
	g2d.setColor(ColorList.background);
	g2d.fillRect(0, 0, WIDTH, HEIGHT);
    }
    private void drawCenteredString(Graphics g2d, String text, Rectangle rect) 
    {
	FontMetrics metrics = g2d.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(text, x, y);
    }
}