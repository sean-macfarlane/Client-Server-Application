/* File Name: Client.java
 * Author: Sean Macfarlane, 040-779-100
 * Course:  CST8221 – JAP, Lab Section: 301
 * Assignment: 2
 * Date: 17 April 2015
 * Professor: Sv. Ranev
 * Purpose: Main class for Client GUI
 */
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * Main Class of Client GUI.
 * 
 * @author Sean Macfarlane
 * @version 2.0.0
 * @since 1.8_40
 */
public class Client {
	/**
	 * The main method. Builds and displays Client GUI
	 * 
	 * @param args
	 *          not used
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setTitle("Macfarlane's Client"); // Set title of frame
				frame.setMinimumSize(new Dimension(600, 550)); // Set minimum size of frame
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation of frame
				frame.setContentPane(new ClientView());	//builds Client GUI
				frame.setLocationByPlatform(true); // Set location of frame relative to Platform
				frame.setVisible(true); // show frame
			}
		});
	}
}// end Client
