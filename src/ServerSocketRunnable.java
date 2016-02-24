/* File Name: ServerSocketRunnable.java
 * Author: Sean Macfarlane, 040-779-100
 * Course:  CST8221 – JAP, Lab Section: 301
 * Assignment: 2
 * Date: 17 April 2015
 * Professor: Sv. Ranev
 * Purpose: ServerSocket that communicates with Client
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ServerSocket that communicates with Client
 * 
 * @author Sean Macfarlane
 * @version 1.0.0
 * @since 1.8_40
 */
public class ServerSocketRunnable implements Runnable {
	// Variables Declaration
	/** Command string sent by Client */
	private String request;
	/** Parsed Command */
	private String command;
	/** Index of Greater-than sign in command */
	private int gtPosition;
	/** Array of string commands*/
	private String commands[] = { "END>", "ECHO>", "TIME>", "DATE>", "?>", "CLS>" };
	/** Format for displaying Time */
	private final SimpleDateFormat TIME_FORMAT;
	/** Format for displaying Date */
	private final SimpleDateFormat DATE_FORMAT;
	/** Socket for connection to Server */
	private Socket socket;
	/** OuputStream for communicating with Client */
	private ObjectOutputStream out;
	/** InputStream for communicating with Client */
	private ObjectInputStream in;

	/**
	 * Constructor
	 * 
	 * @param socket
	 *          Server Socket
	 */
	public ServerSocketRunnable(Socket socket) {
		DATE_FORMAT = new SimpleDateFormat("dd MMMMM yyyy");
		TIME_FORMAT = new SimpleDateFormat("hh:mm:ss aaa");
		this.socket = socket;
	}

	/**
	 * Receives string commands from Client and responds to them appropriately
	 */
	@Override
	public void run() {

		try {
			// Opens output and input streams
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			// Loops until END> command is received
			while (true) {
				request = (String) in.readObject(); // Gets command string
				gtPosition = request.indexOf('>'); // Locates the index position of '>' in command string

				if (gtPosition != -1) { // If the character '>' is in the command string it parses the command up to it
					command = request.substring(0, gtPosition + 1);
				} else { // If the character '>' is not found, invalid command
					command = "ERROR";
				}

				if (command.equals(commands[0])) { // If the command is END>
					System.out.println("Server Socket: Closing client connection...");
					out.writeObject("SERVER>Connection Closed.");
					break;
				} else if (command.equals(commands[1])) { // If the command is ECHO>
					out.writeObject("SERVER>" + request.replace('>', ':'));
				} else if (command.equals(commands[2])) { // If the command is TIME>
					out.writeObject("SERVER>TIME: " + TIME_FORMAT.format(new Date()));
				} else if (command.equals(commands[3])) { // If the command is DATE>
					out.writeObject("SERVER>DATE: " + DATE_FORMAT.format(new Date()));
				} else if (command.equals(commands[4])) { // If the command is ?>
					out.writeObject("SERVER>AVAILABLE SERVICES:\nEND\nECHO\nTIME\nDATE\n?\nCLS\n");
				} else if (command.equals(commands[5])) { // If the command is CLS>
					out.writeObject("CLS:");
				} else { // Otherwise invalid command
					out.writeObject("SERVER>ERROR: Unrecognized command");
				}
				Thread.sleep(100);
			}
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			System.out.println("Server Socket: Closing client connection...");
		}
	}
}
