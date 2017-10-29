package sweets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class WorldOfSweets extends JPanel {    
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 1000;
        
        private HUD hud;
        private SweetState gameState;
	
	public WorldOfSweets() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); // Preferred size affects packing
		this.setFocusable(true); // Focusable so we can use keyboard input
		this.addKeyListener(initKeyAdapter()); // Listens to keys, obv 
		this.addMouseListener(initMouseListener());
                
                hud = new HUD();
                gameState = new SweetState();
                
		running = true;
	}
	
	
	private int targetFPS = 30;
	
	private boolean running = false;
	private int colorState = 1;
	
	
        
	
	public void run() {
		// Basic as boilerplate. This is definitely subject to change.
		// This is the standard basic template for Swing java games.
		// Assuming 30FPS
		
		
		while (running) {
			// Target time is always recalculated in case we want to switch frame rate
			long targetTime = (1000L/targetFPS);
			long startTime = System.currentTimeMillis();
			
			// Perform turn
			tick();
		
			// Redraw screen
			repaint();
			
			// Frame limiting code
			long totalSleepTime = targetTime - (System.currentTimeMillis() - startTime);
			if (totalSleepTime > 0) {
				try {
					Thread.sleep(totalSleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace(); // This shouldn't happen - but in case it does throw an error!
				}
			}
		}
	}
	
	
	public void tick() {
		gameState.makeTurn();
		hud.update(gameState.getDeck(), gameState);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		// Draw all components - typically a loop, should be easy to implement if we use collections of players, tiles, etc
		// We could prematurely optimize and draw only what needs changed, etc. but for now fuck it - just worry about rudimentary stuff
		// ...although our team name implies that we will prematurely optimize:)
                
		try 
		{
                        BufferedImage img = ImageIO.read(new File(Main.getAssetLocale() + "background.jpg"));
			//img = Scalr.r
			g.drawImage(img, 0, 0,WIDTH,HEIGHT, null);
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		drawBoard(g);
                hud.draw(g, WIDTH, HEIGHT);
		
	}
	/**
	 * Generates a zig-zag box pattern for the CandyLand path.
	 * It works by drawing boxes from right to left across the screen,
	 * then drawing a bridge box. It continues this process until 
	 * it reaches the bottom of the screen.
	 * @param The graphics object used to draw to the JPanel
	 * @return Success or failure code;
	 */
	public int drawBoard(Graphics g)
	{
		// Current x and y keep track of our x and y indexes into the Jpanel
		int currentX = 0;
		int currentY = 0;
		
		// X and Y distance are multiplied by the current x or y index to get the total distance for the origin of the rectangle to be drawn.
		int xDistance = WIDTH/10;
		int yDistance = HEIGHT/10;
		
		// The variables that will hold distance times current
		int rowDistance = 0;
		int columnDistance = 0;
		
		// Path state determines whether we should draw a bridge on near x or far x side of the window.
		int pathState = 0; 
		
		while(rowDistance < (HEIGHT - yDistance)) // While we have not reached the bottom of the screen (y).
		{
			rowDistance = currentY * yDistance; // Get the current height we want to draw at.
			
			while(columnDistance < (WIDTH - xDistance)) // While we have not reached the edge of the screen (x).
			{
				
				g.setColor(colorPick());
				columnDistance = currentX * xDistance; // Get the current width we want to draw at.
				g.fillRect(columnDistance,rowDistance, xDistance, yDistance); // Draw a rect at current calculated height and width.
				currentX++;
			}
			currentY++;
			
			// After we have drawn a row we need to draw a row of size one so move down one y index.
			rowDistance = currentY * yDistance;
			
			if(rowDistance < HEIGHT - yDistance) // Make sure we are not off the screen in the y direction
			{
				g.setColor(colorPick());
				
				// Alternate drawing the bridge path on the right and left.
				if(pathState == 0)
				{
					g.fillRect(columnDistance,rowDistance, xDistance, yDistance);
					pathState = 1;
				}
				else
				{
					g.fillRect(0,rowDistance, xDistance, yDistance);
					pathState = 0;
				}
				
			}
			// Move down in y and reset x variable to move left to right again.
			currentY++;
			currentX = 0;
			columnDistance = 0;
		}
		
		

		return 0;
	}
	
	/**
	 * Quick and dirty color state function
	 * @return The color to be applied.
	 */
	private Color colorPick()
	{
		if(colorState == 0)
		{
			colorState = 1;
			return Color.MAGENTA;
			
		}
		if(colorState == 1)
		{
			colorState = 2;
			return Color.red;
			
		}
		if(colorState == 2)
		{
			colorState = 3;
			return Color.green;
			
		}
		if(colorState == 3)
		{
			colorState = 4;
			return Color.orange;
			
		}
		if(colorState == 4)
		{
			colorState = 5;
			return Color.blue;
			
		}
		else
		{
			colorState = 0;
			return Color.yellow;
			
		}
		
	}
	
	// Key and Mouse adapters
	
	private KeyAdapter initKeyAdapter() {
		return new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("Key typed: " + e.getKeyChar());
			}
		};
	}
	
	private MouseAdapter initMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Mouse button " + e.getButton() + "clicked at " + e.getX() + ", " + e.getY());
				
				//Deck click region is (1075 - 1200, 900 - 1000)
				if (e.getX() <= 1200 && e.getX() >= 1075 && e.getY() <= 1000 && e.getY() >= 900) {
					gameState.togglePaused();
				}
			}
		};
	}
}
