package pong.local;

import javax.swing.JFrame;

public class LocalGame extends JFrame implements LocalConstants {
	
	public LocalGame(String name) {
		super(name);
	}
	
	public static void main(String args[]) {
		LocalGame game = new LocalGame("Pong");
		game.setSize(APPLICATION_DIMENSIONS[0], APPLICATION_DIMENSIONS[1]);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LocalPong panel = new LocalPong();
		panel.setFocusable(true);			
		game.getContentPane().add(panel);
		game.setVisible(true);
	}
}