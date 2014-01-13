package pong.network;

import java.io.*;
import java.net.*;
import java.util.*;
import pong.local.*;


public class PongServer implements NetworkConstants, LocalConstants {
	
	// The Pong game on the server
	private ServerState game;
	
	private ClientConnection[] connections = new ClientConnection[2];
	
	// Listens and accepts connections
	private ServerSocket serverSocket;
	private AcceptThread acceptThread;
	private int nextID = 1;
	
	public static void main(String[] args) {
		PongServer server = new PongServer(PORT);
	}
	
	// Create a server on a specified port and start the accept thread
	public PongServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			System.out.println("Server could not start on port " + port);
			System.exit(0);
		}
		
		// Start listening and accepting players
		acceptThread = new AcceptThread();
		acceptThread.start();
		System.out.println("Listening for connections on port " + port);

	}
	
	
	// Thread that accepts user connections as long as the maximum number is not reached
	private class AcceptThread extends Thread {
		public void run() {
			// Need two players to play Pong
			for (int i = 0; i < 2; i++) {
				try {
					Socket connection = serverSocket.accept();
					connections[i] = new ClientConnection(connection);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// Initial delay until the game starts
			try {
				sleep(INITIAL_DELAY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Start the game and set all connections open
			game = new ServerState();
			connections[0].setOpen();
			connections[1].setOpen();
		}
	}
	
	// A connection to a client
	private class ClientConnection {
		private int ID;
		private boolean open = false;
		private BufferedReader in;
		private PrintWriter out;
		Thread sendThread = new SendThread();
		Thread receiveThread = new ReceiveThread();
		
		public ClientConnection(Socket connection) {
			
			// Establish the I/O streams
			try {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				out = new PrintWriter(connection.getOutputStream(), true);
			} catch (Exception e) {
				open = false;
				System.out.println("Cannot create a connection with the specified socket.");
			}
			
			// Synchronize assignment of ID
			synchronized (PongServer.this) {
				ID = nextID++;
			}
		}
		
		// Set the connection open to start sending and receiving message
		public void setOpen() {
			open = true;
			out.println("ACTIVE " + ID);
			out.flush();
			sendThread.start();
			receiveThread.start();
		}
		
		private class SendThread extends Thread {
			public void run() {
				// While the connection is open, get the current state of the game and send it to the client
				while (open) {
					try {
						String message = Message.toString(game.getOutput(ID));
						out.println(Message.toString(game.getOutput(ID)));
						// System.out.println("SEND PLAYER " + ID + ": " + message);
						out.flush();
						sleep(SERVER_SEND_DELAY);
					} catch (Exception e) {
						e.printStackTrace();
						open = false;
						System.out.println("SendThread from Player " + ID + " broken");
					}
				}
			}
		}
		
		// Thread for each connection that constantly receives messages
		private class ReceiveThread extends Thread {
			public void run() {
				while (open) {
					try {
						String message = in.readLine();
						HashMap<String, String> values = Message.toTable(message);
						
						// Process the input
						game.processInput(values);
						sleep(SERVER_RECEIVE_DELAY);
					} catch (Exception e) {
						e.printStackTrace();
						open = false;
						System.out.println("ReceiveThread from Player " + ID + " broken");
						System.exit(0);
					}
				}
			}
		}
		
		
		
	}
		
	
}
