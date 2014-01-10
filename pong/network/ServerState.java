package pong.network;

import java.util.*;

import javax.swing.JOptionPane;

import pong.local.LocalConstants;
import pong.objects.*;

// Main Pong class for server that is authoritative
public class ServerState implements Runnable, LocalConstants {
	
	// Instance variables
	private Ball ball;
	private Paddle[] paddles = new Paddle[2];
	private boolean active  = true;
	private int[] scores = new int[2];
	long ballSleep = 0;

	public ServerState() {
		// Create the ball and paddle
		ball = new Ball(WINDOW_DIMENSIONS[0] / 2, WINDOW_DIMENSIONS[1] / 2, BALL_SIDE, BALL_SIDE, BALL_COLOR, INITIAL_BALL_SPEED[0], INITIAL_BALL_SPEED[1]);
		paddles[0] = new Paddle(LEFT_PADDLE_X, WINDOW_DIMENSIONS[1] / 2, LEFT_PADDLE_DIMENSIONS[0], LEFT_PADDLE_DIMENSIONS[1], LEFT_PADDLE_COLOR, LEFT_PADDLE_SPEED);
		paddles[1] = new Paddle(RIGHT_PADDLE_X, WINDOW_DIMENSIONS[1] / 2, RIGHT_PADDLE_DIMENSIONS[0], RIGHT_PADDLE_DIMENSIONS[1], RIGHT_PADDLE_COLOR, RIGHT_PADDLE_SPEED);
		new Thread(this).start();
	}
	
	// Process the input of a client
	public void processInput(HashMap<String, String> values) {
		
		// Set the appropriate paddle's position
		int ID = Integer.parseInt(values.get("ID"));
		StringTokenizer s = new StringTokenizer(values.get("PADDLES_Y"), "|");
		String[] paddleY = {s.nextToken(), s.nextToken()};
		paddles[ID - 1].setY(Integer.parseInt(paddleY[ID - 1]));
	}
	
	// Retrieve the necessary information to draw a client screen
	public HashMap<String, String> getOutput(int ID) {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("ID", "" + ID);
		values.put("BALL", "" + ball.getX() + "|" + ball.getY());
		values.put("PADDLES_Y", "" + paddles[0].getY() + "|" + paddles[1].getY());
		values.put("POINTS", "" + scores[0] + "|" + scores[1]);
		return values;
	}
	
	// Updates the Pong game every frame rate
	public void update() {
		if (Calendar.getInstance().getTimeInMillis() > ballSleep) {
			ball.move();
		}
		checkCollisions();
		checkPoint();
	}
	
	// Change the ball's trajectory at random to make the game more unpredictable
	private void changeBallTrajectory(boolean positiveDirection) {
		
		// Sets the ball X and Y speeds to be random integers between the minimum and maximum
		ball.setXSpeed((int) (Math.random() * (MAX_BALL_SPEED[0] + 1 - MIN_BALL_SPEED[0]) + MIN_BALL_SPEED[0]));
		ball.setYSpeed((int) (Math.random() * (MAX_BALL_SPEED[1] + 1 - MIN_BALL_SPEED[1]) + MIN_BALL_SPEED[1]));
		
		// The ball is moving left or right?
		if (positiveDirection) {
			ball.setXSpeed(Math.abs(ball.getXSpeed()));
		} else {
			ball.setXSpeed(- Math.abs(ball.getXSpeed()));
		}
	}
	
	private void checkCollisions() {
		// Collision between ball and left paddle
		if (paddles[0].collide(ball)) {
			if (ball.getXSpeed() < 0) {
				changeBallTrajectory(true);
			}
		}
		
		// Collision between ball and right paddle
		if (paddles[1].collide(ball)) {
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
			ball.setPos(paddles[0].getX() + paddles[0].getWidth() / 2 + ball.getWidth() / 2, paddles[0].getY());
			ball.setXSpeed(- ball.getXSpeed());
			scores[1]++;
		}
		
		// Ball went past right side
		if (ball.getX() - ball.getWidth() > WINDOW_DIMENSIONS[0]) {
			ballSleep = Calendar.getInstance().getTimeInMillis() + POINT_DELAY;
			ball.setPos(paddles[1].getX() - paddles[1].getWidth() / 2 - ball.getWidth() / 2, paddles[1].getY());
			ball.setXSpeed(- ball.getXSpeed());
			scores[0]++;
		}
		
		// A player has generated enough points to win, thus closing the application
		if (scores[1] >= POINTS_TO_WIN) {
			active = false;
			JOptionPane.showMessageDialog(null, "Player 2 has won with " + POINTS_TO_WIN + " points!");
			System.exit(0);
		} else if (scores[0] >= POINTS_TO_WIN) {
			active = false;
			JOptionPane.showMessageDialog(null, "Player 1 has won with " + POINTS_TO_WIN + " points!");
			System.exit(0);
		}

	}
	
	public void run() {
		try {
			// Loop to repaint and then sleep while the game is in session
			while (active) {
				Thread.sleep(FRAME_DELAY);
				update();
			}
		} catch(Exception e) {
			
		}
	}
}
