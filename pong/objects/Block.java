package pong.objects;

import java.awt.Color;
import java.awt.Graphics;

public class Block {
	
	// Dimensions
	private int width;
	private int height;
	
	// Positions
	private int xPos;
	private int yPos;
	
	// Color
	private Color color;
	
	// Constructs a block with no parameters; default position is (100, 150)
	public Block() {
		this(100, 150);
	}
	
	// Constructs a block with position; default dimensions are 10 x 10
	public Block(int xPos, int yPos) {
		this(xPos, yPos, 10, 10);
	}
	
	// Constructs a block with position and dimensions; default color is black
	public Block(int xPos, int yPos, int width, int height) {
		this(xPos, yPos, width, height, Color.BLACK);
	}
	
	// Constructs a block with position, dimensions, and color
	public Block(int xPos, int yPos, int width, int height, Color color) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	// Set the color
	public void setColor(Color color) {
			this.color = color;
	}
	
	// Two blocks are equal if their position and dimensions are equal
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Block)) return false;
		Block block = (Block) obj;
		if ((block.xPos == this.xPos) && (block.yPos == this.yPos) && (block.width == this.width) && (block.height == this.height)) {
			return true;
		}
		return false;
	}
	
	// Checks to see if this block collides with another block by checking the corners
	public boolean collide(Block block) {
		if (this.hasPoint(block.getX() - block.getWidth() / 2, block.getY() - block.getHeight() / 2) ||
			this.hasPoint(block.getX() - block.getWidth() / 2, block.getY() + block.getHeight() / 2) ||
			this.hasPoint(block.getX() + block.getWidth() / 2, block.getY() - block.getHeight() / 2) ||
			this.hasPoint(block.getX() + block.getWidth() / 2, block.getY() + block.getHeight() / 2)) {
			return true;
		}
		return false;
	}
	
	// Checks if the block contains the point specified by the X and Y values
	public boolean hasPoint(int x, int y) {
		if (x > getX() - getWidth() / 2 && x < getX() + getWidth() / 2 &&
			y > getY() - getHeight() / 2 && y < getY() + getHeight() / 2) {
			return true;
		}
		return false;
	}
	
	// Converts a block to string
	@Override
	public String toString() {
		return xPos + " " + yPos + " " + width + " " + height + " " + color;
	}
	
	// Draws the block centered at the position on the window
	public void draw(Graphics window) {
		window.setColor(color);
		window.fillRect(getX() - getWidth() / 2, getY() - getHeight() / 2, getWidth(), getHeight());
	}

	// Sets the position of the block
	public void setPos(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	// Getter and Setter for the X position, Y position, dimensions, and color
	public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	
	public Color getColor() {
		return color;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
}