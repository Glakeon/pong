package pong.objects;

import java.awt.Color;

public class Ball extends Block {
	
	// Speeds of the ball in the X and Y direction
	private int xSpeed;
	private int ySpeed;
	
	// Constructs a ball with no parameters; default X and Y positions are 200
	public Ball() {
		this(200, 200);
	}
	
	// Constructs a ball with position specified; default width and height are 10
	public Ball(int xPos, int yPos) {
		this(xPos, yPos, 10, 10);
	}
	
	// Constructs a ball with position and dimensions; default color is black
	public Ball(int x, int y, int width, int height) {
		this(x, y, width, height, Color.BLACK);
	}
	
	// Constructs a ball with position and color; default velocity is [3, 1]
	public Ball(int x, int y, int width, int height, Color color) {
		this(x, y, width, height, color, 3, 1);
	}
	
	// Constructs a block with parameters specified, and sets the speeds
	public Ball(int x, int y, int width, int height, Color color, int xSpeed, int ySpeed) {
		super(x, y, width, height, color);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	// Set the X-Speed of the ball
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	
	// Sets the Y-Speed of the ball
	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	// Returns the X-Speed of the ball
	public int getXSpeed() {
		return xSpeed;
	}
	
	// Returns the Y-Speed of the ball
	public int getYSpeed() {
		return ySpeed;
	}
	
	// Changes the X and Y positions by the speed
	public void move() {
		setX(getX() + xSpeed);
		setY(getY() + ySpeed);
	}
	
	// Two balls are equal if their positions, dimensions, and speeds are equal
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ball)) return false;
		return (super.equals(obj)) && (this.xSpeed == ((Ball) obj).xSpeed) && (this.ySpeed == ((Ball)obj).ySpeed);
	}   
	
	// Converts a ball to a string
	@Override
	public String toString() {
		return super.toString() + " " + xSpeed + " " + ySpeed;
	}
}