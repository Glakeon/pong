package pong.network;

import java.util.HashMap;
import java.util.StringTokenizer;

// Message is a class with static methods that convert between String representation and HashMap
public class Message implements NetworkConstants {
	
	// Turns a HashMap of the message into a String to pass between server and client
	public static String toString(HashMap<String, String> message) {
		String result = "";
		for (int i = 0; i < KEYWORDS.length; i++) {
			result += KEYWORDS[i] + " " + message.get(KEYWORDS[i]);
			if (i != KEYWORDS.length - 1) {
				result += " ";
			}
		}
		return result;
	}
	
	// Turns the string back into a HashMap
	public static HashMap<String, String> toTable(String message) {
		HashMap<String, String> map = new HashMap<String, String>();
		StringTokenizer s = new StringTokenizer(message);
		for (int i = 0; i < KEYWORDS.length; i++) {
			map.put(s.nextToken(), s.nextToken());
		}
		return map;
	}
}
