package pong.network;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.JPanel;

import pong.local.LocalConstants;
import pong.objects.*;

public class ClientState extends JPanel implements Runnable, LocalConstants, KeyListener {
	
	// Instance variables
	private Ball ball;
	private Paddle[] paddles = new Paddle[2];
	private boolean[] keys = new boolean[2];
	private int[] scores = new int[2];
	private long ballSleep = 0;
	
	private int ID;
	
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public ClientState() {
		setDoubleBuffered(true);
		
		// Create the ball and paddle
		ball = new Ball(WINDOW_DIMENSIONS[0] / 2, WINDOW_DIMENSIONS[1] / 2, BALL_SIDE, BALL_SIDE, BALL_COLOR, INITIAL_BALL_SPEED[0], INITIAL_BALL_SPEED[1]);
		paddles[0] = new Paddle(LEFT_PADDLE_X, WINDOW_DIMENSIONS[1] / 2, LEFT_PADDLE_DIMENSIONS[0], LEFT_PADDLE_DIMENSIONS[1], LEFT_PADDLE_COLOR, LEFT_PADDLE_SPEED);
		paddles[1] = new Paddle(RIGHT_PADDLE_X, WINDOW_DIMENSIONS[1] / 2, RIGHT_PADDLE_DIMENSIONS[0], RIGHT_PADDLE_DIMENSIONS[1], RIGHT_PADDLE_COLOR, RIGHT_PADDLE_SPEED);

		// Set up a boolean array that will keep track of whether of not a key to move the paddle is pressed down
		for (int i = 0; i < 2; i++) {
			keys[i] = false;
		}
		
		// Background color
		setBackground(Color.LIGHT_GRAY);
		setVisible(true);
		addKeyListener(this);
	}
	
	public void processInput(HashMap<String, String> values) {

		// Set the ball's position
		StringTokenizer s = new StringTokenizer(values.get("BALL"), "|");
		ball.setPos(Integer.parseInt(s.nextToken()), Integer.parseInt(s.nextToken()));
		
		// Set the other player's paddle position
		s = new StringTokenizer(values.get("PADDLES_Y"), "|");
		String[] paddleY = {s.nextToken(), s.nextToken()};
		paddles[ID % 2].setY(Integer.parseInt(paddleY[ID % 2]));
		
		// Sets the current scores
		s = new StringTokenizer(values.get("POINTS"), "|");
		scores[0] = Integer.parseInt(s.nextToken());
		scores[1] = Integer.parseInt(s.nextToken());
	}
	
	// Retrieve the necessary information to draw a client screen
	public HashMap<String, String> getOutput() {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("ID", "" + ID);
		values.put("BALL", "NONE");
		values.put("PADDLES_Y", "" + paddles[0].getY() + "|" + paddles[1].getY());
		values.put("POINTS", "NONE");
		return values;
	}

	// Synchronize the start times
	public void setStartTime(long time) throws InterruptedException {
		new Thread(this);
		Thread.currentThread();
		
		// Wait remaining time before starting the thread
		Thread.sleep(time - Calendar.getInstance().getTimeInMillis());
		new Thread(this).start();
	}
	
	@Override
	// Paints the window
	public void paint(Graphics window) {
		super.paint(window);
		
		// Set the font with its color
		window.setFont(FONT);
		window.setColor(Color.BLUE);
		
		// Find the size of string in font
		FontMetrics fm   = window.getFontMetrics(FONT);
		Rectangle2D rect = fm.getStringBounds(scores[0] + " | " + scores[1], window);
		int textHeight = (int) rect.getHeight();
		int textWidth = (int) rect.getWidth();

		// Center text horizontally and place it at the bottom
		int x = (WINDOW_DIMENSIONS[0] - textWidth)  / 2;
		int y = WINDOW_DIMENSIONS[1] - textHeight / 2;
		window.drawString(scores[0] + " | " + scores[1], x, y);
		
		// The ball draws and moves if the current time is greater than the sleep time
		ball.draw(window);
		if (Calendar.getInstance().getTimeInMillis() > ballSleep) {
			ball.move();
		}
		
		// Draw the paddles and move them if the keys are pressed down
		paddles[0].draw(window);
		paddles[1].draw(window);
		checkKeys();
	}
	
	private void checkKeys() {
		if (keys[0]) {
			paddles[ID - 1].restrictUp(0);
		}
		if (keys[1]) {
			paddles[ID - 1].restrictDown(WINDOW_DIMENSIONS[1]);
		}
	}

	public void run() {
		try {
			while (true) {
				Thread.currentThread();
				Thread.sleep(FRAME_DELAY);
				repaint();
			}
		} catch(Exception e) {
			
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// The keys are just pressed down
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				keys[0] = true;
				break;
			case KeyEvent.VK_DOWN:
				keys[1] = true;
				break;
			default:
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// The keys are not pressed down anymore
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				keys[0] = false;
				break;
			case KeyEvent.VK_DOWN:
				keys[1] = false;
				break;
			default:
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}	
}