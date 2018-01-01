import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import javax.swing.Timer;

/**
 * Purpose: Panel in panel that runs the game 
 * 
 * @author Jonathan
 *
 */
public class Gameplay extends JPanel implements KeyListener, ActionListener {
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 310;
	private int ballPosX = 120;
	private int ballPosY = 350;
	private int ballYDir = -2;
	private int ballXDir = -1;
	
	//Handles Map Design operations
	private MapGenerator map;
	
	
	public Gameplay() {
		map = new MapGenerator(2, 7);
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		
		//Background
		g.setColor(Color.BLACK);
		g.fillRect(1, 1, 692, 592);
		
		
		//Drawing Map
		map.draw((Graphics2D)g);
		
		//Borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//The Paddle
		g.setColor(Color.GREEN);
		g.fillRect(playerX, 510, 100, 8);
		
		//scores
		g.setColor(Color.WHITE);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score, 590, 30);
		
		//The Ball
		g.setColor(Color.yellow);
		g.fillOval(ballPosX, ballPosY, 20, 20);
		
		if(totalBricks <= 0) {
			play = false;
			ballXDir = 0;
			ballYDir = 0;
			
			g.setColor(Color.GREEN);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won: , Score: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		
		if(ballPosY > 570) {
			play = false;
			ballXDir = 0;
			ballYDir = 0;
			
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		g.dispose();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		timer.start();
		
		
		//Manage the Balls movement
		if(play) {
			
			//Detect intersection between ball and paddle
			if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 510, 100, 8))){
				ballYDir = -ballYDir;
			}
			
			//Look through each brick to check for intersection with ball
			A: for(int i = 0; i < map.map.length; i++) {
				for(int j = 0; j < map.map[0].length; j++) {
					//Checks value
					if(map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle currBrick = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
						
						if(ballRect.intersects(currBrick)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							System.out.println("Total Bricks: " + totalBricks);
							
							if(ballPosX + 19 <= currBrick.x || ballPosX + 1 >= currBrick.x + currBrick.width) {
								ballXDir = -ballXDir;
							} else {
								ballYDir = -ballYDir;
							}
							
							break A;
						}
					}
				}
			}
			
			ballPosX += ballXDir;
			ballPosY += ballYDir;
			
			if(ballPosX < 0) {
				ballXDir = -ballXDir;
			}
			if(ballPosX > 670) {
				ballXDir = -ballXDir;
			}
			if(ballPosY < 0) {
				ballYDir = -ballYDir;
			}
			
		}
		
		//Shouldn't this be in play?
		//Test to see what's wrong
		repaint();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		//Manging the Player Movement
		switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				if(playerX >= 600) {
					playerX = 600;
				}
				else {
					moveRight();
				}
				
				break;
			case KeyEvent.VK_LEFT:
				if(playerX < 10) {
					playerX = 9;
				}
				else {
					moveLeft();
				}
				
				
				break;
			case KeyEvent.VK_ENTER:
				//Reset the game to the start
				if(!play) {
					play = true;
					ballPosX = 120;
					ballPosY = 350;
					ballXDir = -1;
					ballYDir = -2;
					playerX = 310;
					score = 0;
					totalBricks = 21;
					map = new MapGenerator(3, 7);
					
				}
				
				break;
		}
		
		
	}

	/**
	 * Moves the play paddle right
	 */
	public void moveRight() {
		play = true;
		playerX += 25;
	}
	
	/**
	 * Moves the play paddle left
	 */
	public void moveLeft() {
		play = true;
		playerX -= 25;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	
	
	
}
