package pong.objects;

import java.awt.Color;

public class Paddle extends Block {
	
	// Vertical speed of the paddle
	private int speed;
	
	// Constructs a paddle with no parameters; default position is (10, 10)
	public Paddle() {
		this(10, 10);
	}
	
	// Constructs a paddle with position; default speed is 5
	public Paddle(int xPos, int yPos) {
		this(xPos, yPos, 5);
	}
	
	// Constructs a paddle with position and speed; default dimensions are 10 x 10
	public Paddle(int xPos, int yPos, int speed) {
		this(xPos, yPos, 10, 10, speed);
	}
	
	// Constructs a paddle with position, dimensions, and speed; default color is black
	public Paddle(int xPos, int yPos, int width, int height, int speed) {
		this(xPos, yPos, width, height, Color.BLACK, speed);
	}
	
	// Constructs a paddle with position, dimensions, color, and speed
	public Paddle(int xPos, int yPos, int width, int height, Color color, int speed) {
		super(xPos, yPos, width, height, color);
		this.speed = speed;
	}
	
	// Move the paddle up
	public void moveUp() {
		setY(getY() - speed);
	}
	
	// Move the paddle up with an upper barrier
	public void restrictUp(int y) {
		if (getY() - getHeight() / 2 > y) {
			moveUp();
		}
	}
	
	// Move the paddle down
	public void moveDown() {
		setY(getY() + speed);
	}
	
	// Move the paddle down with a bottom barrier
	public void restrictDown(int y) {
		if (getY() + getHeight() / 2 < y) {
			moveDown();
		}
	}
	
	// Set the speed
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	// Get the speed
	public int getSpeed() {
		return speed;
	}
	
	// Converts a paddle to a string
	@Override
	public String toString() {
		return super.toString() + " " + speed;
	}
}