package pong.network;

public interface NetworkConstants {
	
	// Maximum players allowed to connect
	public static final int MAX_PLAYERS = 2;
	
	// Port on which the server runs
	public static final int PORT = 9999;
	
	// Delays for sending and receiving messages on server side
	public static final int SERVER_SEND_DELAY = 5;
	public static final int SERVER_RECEIVE_DELAY = 5;
	public static final int START_DELAY = 5000;
	
	// Delays for sending and receiving message on client side
	public static final int CLIENT_SEND_DELAY = 5;
	public static final int CLIENT_RECEIVE_DELAY = 5;
	
	// Keywords in a message
	public static final String[] KEYWORDS = {"ID", "BALL", "PADDLES_Y", "POINTS"};

}
