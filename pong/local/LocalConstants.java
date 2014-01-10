package pong.local;

import java.awt.*;


// Constants for the local game
public interface LocalConstants {
	
	// Application and window dimensions
	public static int[] WINDOW_DIMENSIONS = {780, 560};
	public static int[] APPLICATION_DIMENSIONS = {800, 600};
	
	// Thread sleeps
	public static final int INITIAL_DELAY = 2000;
	public static final int FRAME_DELAY = 30;
	public static final int POINT_DELAY = 0;
	public static final int POINTS_TO_WIN = 5;
	
	// Left paddle properties
	public static final int[] LEFT_PADDLE_DIMENSIONS = {12, 100};
	public static final int LEFT_PADDLE_X = 50;
	public static final Color LEFT_PADDLE_COLOR = Color.RED;
	public static final int LEFT_PADDLE_SPEED = 5;
	
	// Right paddle properties
	public static final int[] RIGHT_PADDLE_DIMENSIONS = {12, 100};
	public static final int RIGHT_PADDLE_X = WINDOW_DIMENSIONS[0] - 50;
	public static final Color RIGHT_PADDLE_COLOR = Color.RED;
	public static final int RIGHT_PADDLE_SPEED = 5;
	
	// Ball properties
	public static final Color BALL_COLOR = Color.GREEN;
	public static final int[] INITIAL_BALL_SPEED = {7, 2};
	public static final int[] MIN_BALL_SPEED = {5, -6};
	public static final int[] MAX_BALL_SPEED = {9, 6};
	public static final int BALL_SIDE = 13;
	
	// Font of the score
	public static final Font FONT = new Font(Font.SERIF, Font.PLAIN, 30);
	
}
