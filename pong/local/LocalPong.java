package pong.local;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import pong.objects.*;
import javax.swing.*;

public class LocalPong extends JPanel implements Runnable, LocalConstants, KeyListener {
	
	// Instance variables
	private Ball ball;
	private Paddle leftPaddle, rightPaddle;
	private boolean[] keys = new boolean[4];
	private boolean gameInSession  = true;
	private int leftScore = 0, rightScore = 0;
	long ballSleep = Calendar.getInstance().getTimeInMillis();

	public LocalPong() {
		
		// Draw the image off line and upload it for better graphics
		setDoubleBuffered(true);
		
		// Create the ball and paddle
		ball = new Ball(WINDOW_DIMENSIONS[0] / 2, WINDOW_DIMENSIONS[1] / 2, BALL_SIDE, BALL_SIDE, BALL_COLOR, INITIAL_BALL_SPEED[0], INITIAL_BALL_SPEED[1]);
		leftPaddle = new Paddle(LEFT_PADDLE_X, WINDOW_DIMENSIONS[1] / 2, LEFT_PADDLE_DIMENSIONS[0], LEFT_PADDLE_DIMENSIONS[1], LEFT_PADDLE_COLOR, LEFT_PADDLE_SPEED);
		rightPaddle = new Paddle(RIGHT_PADDLE_X, WINDOW_DIMENSIONS[1] / 2, RIGHT_PADDLE_DIMENSIONS[0], RIGHT_PADDLE_DIMENSIONS[1], RIGHT_PADDLE_COLOR, RIGHT_PADDLE_SPEED);
		
		// Set up a boolean array that stores key presses
		for (int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
		
		// Sets the background color and makes the window visible
		setBackground(Color.LIGHT_GRAY);
		setVisible(true);
		
		// Starts the thread and adds key listeners
		addKeyListener(this);
		new Thread(this).start();
	}
	
	@Override
	// Updates the window every frame rate
	public void update(Graphics window) {
		paint(window);
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
		Rectangle2D rect = fm.getStringBounds(leftScore + " | " + rightScore, window);
		int textHeight = (int) rect.getHeight();
		int textWidth = (int) rect.getWidth();

		// Center text horizontally and place it at the bottom
		int x = (WINDOW_DIMENSIONS[0] - textWidth)  / 2;
		int y = WINDOW_DIMENSIONS[1] - textHeight / 2;
		window.drawString(leftScore + " | " + rightScore, x, y);
		
		// The ball draws and moves if the current time is greater than the sleep time
		ball.draw(window);
		if (Calendar.getInstance().getTimeInMillis() > ballSleep) {
			ball.move();
		}
		
		// Draw the paddles and move them if the keys are pressed down
		leftPaddle.draw(window);
		rightPaddle.draw(window);
		checkKeys();
		
		// Check for collisions and points
		checkCollisions();
		checkPoint();
	}
	
	// If any keys are pressed down, move the paddles with restrictions
	private void checkKeys() {
		if (keys[0]) {
			leftPaddle.restrictUp(0);
		}
		if (keys[1]) {
			leftPaddle.restrictDown(WINDOW_DIMENSIONS[1]);
		}
		if (keys[2]) {
			rightPaddle.restrictUp(0);
		}
		if (keys[3]) {
			rightPaddle.restrictDown(WINDOW_DIMENSIONS[1]);
		}
	}
	
	// Change the ball's trajectory at random to make the game more unpredictable
	private void changeBallTrajectory(boolean positiveDirection) {
		ball.setXSpeed((int) (Math.random() * (MAX_BALL_SPEED[0] + 1 - MIN_BALL_SPEED[0]) + MIN_BALL_SPEED[0]));
		ball.setYSpeed((int) (Math.random() * (MAX_BALL_SPEED[1] + 1 - MIN_BALL_SPEED[1]) + MIN_BALL_SPEED[1]));
		if (positiveDirection) {
			ball.setXSpeed(Math.abs(ball.getXSpeed()));
		} else {
			ball.setXSpeed(- Math.abs(ball.getXSpeed()));
		}
	}
	
	private void checkCollisions() {
		// Collision between ball and left paddle
		if (leftPaddle.collide(ball)) {
			if (ball.getXSpeed() < 0) {
				changeBallTrajectory(true);
			}
		}
		
		// Collision between ball and right paddle
		if (rightPaddle.collide(ball)) {
			if (ball.getXSpeed() > 0) {
				changeBallTrajectory(false);
			}
		}
		
		// Collision between ball and the top or bottom of the window
		if(!(ball.getY() > ball.getHeight() / 2 && ball.getY() < WINDOW_DIMENSIONS[1] - ball.getHeight() / 2)) {
			ball.setYSpeed(- ball.getYSpeed());
		}
	}
	
	private void checkPoint() {
		
		// Ball went past left side
		if (ball.getX() + ball.getWidth() / 2 < 0) {
			ballSleep = Calendar.getInstance().getTimeInMillis() + POINT_DELAY;
			ball.setPos(leftPaddle.getX() + leftPaddle.getWidth() / 2 + ball.getWidth() / 2, leftPaddle.getY());
			ball.setXSpeed(- ball.getXSpeed());
			rightScore++;
		}
		
		// Ball went past right side
		if (ball.getX() - ball.getWidth() > WINDOW_DIMENSIONS[0]) {
			ballSleep = Calendar.getInstance().getTimeInMillis() + POINT_DELAY;
			ball.setPos(rightPaddle.getX() - rightPaddle.getWidth() / 2 - ball.getWidth() / 2, rightPaddle.getY());
			ball.setXSpeed(- ball.getXSpeed());
			leftScore++;
		}
		
		// A player has generated enough points to win, thus closing the application
		if (rightScore >= POINTS_TO_WIN) {
			gameInSession = false;
			JOptionPane.showMessageDialog(null, "Player 2 has won with " + POINTS_TO_WIN + " points!");
			System.exit(0);
		} else if (leftScore >= POINTS_TO_WIN) {
			gameInSession = false;
			JOptionPane.showMessageDialog(null, "Player 1 has won with " + POINTS_TO_WIN + " points!");
			System.exit(0);
		}

	}
	public void run() {
		try {
			
			// Displays the welcome instructions and then puts the ball to sleep
			JOptionPane.showMessageDialog(null, "Welcome to Local Pong, a two player arcade game! The goal of this game is to get " + POINTS_TO_WIN + " points.\n"
					+ "To earn a point, a player must hit the ball past another person's paddle.");
			JOptionPane.showMessageDialog(null, "Player 1 uses the W key to move the left paddle up, and the S key to move it down.\n"
					+ "Player 2 uses the I key to move the right paddle up, and the K key to move it down.\n");
			JOptionPane.showMessageDialog(null, "The game will start " + INITIAL_DELAY / 1000.0 + " seconds after the button is clicked. Good luck!");
			ballSleep = Calendar.getInstance().getTimeInMillis() + INITIAL_DELAY;
			
			// Loop to repaint and then sleep while the game is in session
			while (gameInSession) {
				Thread.sleep(FRAME_DELAY);
				repaint();
			}
			
		} catch(Exception e) {
			
		}
	}
	
	// When a key is pressed, check to see if a paddle is affected
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				keys[0] = true;
				break;
			case KeyEvent.VK_S:
				keys[1] = true;
				break;
			case KeyEvent.VK_I:
				keys[2] = true;
				break;
			case KeyEvent.VK_K:
				keys[3] = true;
				break;
			default:
				break;
		}
	}
	
	// When a key is released, check to see if a paddle is affected
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				keys[0] = false;
				break;
			case KeyEvent.VK_S:
				keys[1] = false;
				break;
			case KeyEvent.VK_I:
				keys[2] = false;
				break;
			case KeyEvent.VK_K:
				keys[3] = false;
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