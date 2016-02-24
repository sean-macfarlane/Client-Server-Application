/* File Name: Server.java
 * Author: Sean Macfarlane, 040-779-100
 * Course:  CST8221 – JAP, Lab Section: 301
 * Assignment: 2
 * Date: 17 April 2015
 * Professor: Sv. Ranev
 * Purpose: Main class for Server
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main Class of Server.
 * 
 * @author Sean Macfarlane
 * @version 1.0.0
 * @since 1.8_40
 */
public class Server {

	/** Port number initialized as default port, but can be changed if specified at launch */
	private static int port = 8088;

	/**
	 * The main method. Starts the Server
	 * 
	 * @param args
	 *          Specified Port Number
	 * @throws IOException
	 *           Catches any I/O exceptions when establishing server
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket; // ServerSocket for communication with Client

		if (args.length > 0) { // If a port number is specified at launch
			try {
				port = Integer.valueOf(args[0]);
				System.out.println("Using port: " + port);
			} catch (NumberFormatException e) {
				System.out.println(args[0] + " invalid port");
				System.out.println("Using default port: " + port);
			}
		} else { // Otherwise use default port
			System.out.println("Using default port: " + port);
		}

		serverSocket = new ServerSocket(port); // Creates the server socket bound to the port chosen
		try {
			while (true) {
				Socket socket = serverSocket.accept(); // Establishes connection to Client
				System.out.println("Connecting to a client " + socket);
				Runnable r = new ServerSocketRunnable(socket); // Establishes communication with Client
				Thread t = new Thread(r); // Starts the server as a separate Thread
				t.start();
			}
		} finally {
			serverSocket.close();
		}
	}// end main()
}// end Server
