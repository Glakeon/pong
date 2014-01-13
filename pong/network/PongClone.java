package pong.network;

import java.io.*;
import java.net.*;
import java.util.*;
import pong.local.*;
import javax.swing.JFrame;


// Client for the Pong game
public class PongClone extends JFrame implements LocalConstants, NetworkConstants {

	private static ClientState game;
	private int ID;
	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;
	private boolean active = false;
	private Thread sendThread = new SendThread();
	private Thread receiveThread = new ReceiveThread();
	
	public PongClone() {
		// Set up the window
		super("Pong");
		setSize(APPLICATION_DIMENSIONS[0], APPLICATION_DIMENSIONS[1]);
		game = new ClientState();
		game.setFocusable(true);			
		getContentPane().add(game);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	// Set a player active to start sending and receiving messages
	public void setActive() {
		active = true;
		System.out.println("Player set active");
		
		// Start sending and receiving from server
		sendThread.start();
		receiveThread.start();
		game.run();
	}
	
	// Get the ID of the player
	public int getID() {
		return ID;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, NumberFormatException, InterruptedException {
		// Connect to the server
		socket = new Socket("localhost", PORT);
		
		// Write and read from the server through sockets
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		// Create a new instance of a client
		PongClone client = new PongClone();
		
		// Wait for the ACTIVE method to start the client
		while (true) {
			String message = in.readLine();
			if (message != null) {
				if (message.startsWith("ACTIVE")) {
					client.ID = Integer.parseInt(message.substring("ACTIVE".length() + 1));
					game.setID(Integer.parseInt(message.substring("ACTIVE".length() + 1)));
					client.setActive();
					break;
				}
			}
		}
	}
	
	// Thread to keep sending messages
	private class SendThread extends Thread {
		public void run() {
			while (active) {
				try {
					String message = Message.toString(game.getOutput());
					// System.out.println("SEND: " + message);
					out.println(message);
					out.flush();
					sleep(CLIENT_SEND_DELAY);
				} catch (Exception e) {
					e.printStackTrace();
					active = false;
					System.out.println("SendThread of Player " + ID + " broken");
				}
			}
		}
	}
	
	// Thread that constantly receives messages
	private class ReceiveThread extends Thread {
		public void run() {
			while (active) {
				try {
					String message = in.readLine();
					if (message != null) {
						// System.out.println("RECEIVE: " + message);
						HashMap<String, String> values = Message.toTable(message);
						ID = Integer.parseInt(values.get("ID"));
						game.processInput(values);
					}
					sleep(CLIENT_RECEIVE_DELAY);
				} catch (Exception e) {
					e.printStackTrace();
					active = false;
					System.out.println("ReceiveThread of Player " + ID + " broken");
				}
			}
		}
	}
}
