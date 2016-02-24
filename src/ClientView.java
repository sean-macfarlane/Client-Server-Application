/* File Name: ClientView.java
 * Author: Sean Macfarlane, 040-779-100
 * Course:  CST8221 – JAP, Lab Section: 301
 * Assignment: 2
 * Date: 17 April 2015
 * Professor: Sv. Ranev
 * Purpose: Builds the GUI for the Client
 * Class list: Controller
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * Class for building the Client GUI
 * 
 * @author Sean Macfarlane
 * @version 2.0.0
 * @since 1.8_40
 */
public class ClientView extends JPanel {

	// Variables declaration
	/** JButton to Connect to Server */
	private JButton buttonConnect;
	/** JButton to Send to Server */
	private JButton buttonSend;
	/** JComboBox for a list of Ports. Generates warning because of raw type */
	private JComboBox comboPorts;	//Generates warning because of raw type
	/** JLabel for the Host name */
	private JLabel hostLabel;
	/** JLabel for the Port number */
	private JLabel portLabel;
	/** JTextArea for the Terminal */
	private JTextArea textArea;
	/** JTextfield for the host name */
	private JTextField textHost;
	/** JTextfield for the server request */
	private JTextField textServer;
	/** Array of port names */
	private String ports[] = { "", "8088", "65000", "65535" };
	/** ObjectOutputStream for communicating with the Server */
	private ObjectOutputStream out;
	/** ObjectInputStream for communicating with the Server */
	private ObjectInputStream in;
	/** Socket for Client */
	private Socket client;
	/** Timeout value for establishing a connection to server {@value} */
	private static final int time_out = 10000; // 10 seconds

	// End of variables declaration

	/**
	 * Default constructor. Builds the GUI
	 */
	public ClientView() {
		initComponents();
	}

	/**
	 * Initializes the Components and builds the Client GUI
	 */
	private void initComponents() {
		// Creates components
		JPanel paneConnection = new JPanel();
		hostLabel = new JLabel();
		portLabel = new JLabel();
		textHost = new JTextField();
		comboPorts = new JComboBox(ports);
		buttonConnect = new JButton();
		JPanel paneClient = new JPanel();
		textServer = new JTextField();
		buttonSend = new JButton();
		JPanel paneTerminal = new JPanel();
		textArea = new JTextArea(30, 40);
		JPanel topPane = new JPanel();

		// Set Layout and Border Top JPanel Container
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));

		// Set Layout of top half of Top JPanel container
		topPane.setLayout(new BorderLayout());

		// Set Layout and Border of SET CONNECTION box
		paneConnection.setLayout(new BorderLayout());
		paneConnection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.red, 10), "SET CONNECTION"));

		// Set Layout and Components of Top half of SET CONNECTION box
		hostLabel.setDisplayedMnemonic('H');
		hostLabel.setText("Host:");
		hostLabel.setPreferredSize(new Dimension(40, 40));
		textHost.setText("localhost");
		textHost.setMargin(new Insets(0, 5, 0, 0));
		textHost.setPreferredSize(new Dimension(textHost.getColumns(), 20));
		textHost.setCaretPosition(0);
		hostLabel.setLabelFor(textHost);
		textHost.requestFocusInWindow();

		// Uses GridBagLayout for Top Half of SET CONNECTION box so JTextField expands with resize of window
		JPanel top = new JPanel();
		top.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 5, 0, 0);
		top.add(hostLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		top.add(textHost, c);

		// Sets the Components of bottom half of SET CONNECTION box
		portLabel.setDisplayedMnemonic('P');
		portLabel.setText("Port:");
		portLabel.setPreferredSize(new Dimension(40, 40));
		comboPorts.setPreferredSize(new Dimension(100, 21));
		comboPorts.setBackground(Color.WHITE);
		comboPorts.setEditable(true);
		portLabel.setLabelFor(comboPorts);
		buttonConnect.setBackground(Color.red);
		buttonConnect.setMnemonic('C');
		buttonConnect.setText("Connect");
		buttonConnect.setPreferredSize(new Dimension(100, 21));
		buttonConnect.addActionListener(new Controller());
		buttonConnect.setActionCommand("c");

		// Layouts the components of bottom half of SET CONNECTION box
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		bottom.add(portLabel);
		bottom.add(comboPorts);
		bottom.add(buttonConnect);

		// Adds the top and bottom containers of SET CONNECTION panel container
		paneConnection.add(top, BorderLayout.NORTH);
		paneConnection.add(bottom, BorderLayout.SOUTH);
		// Adds SET CONNECTION panel container to top of Top Layer Panel Container
		topPane.add(paneConnection, BorderLayout.NORTH);

		// Sets Border and Layout of CLIENT REQUEST box
		paneClient.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 10), "CLIENT REQUEST"));
		paneClient.setLayout(new GridBagLayout());

		// Sets Components of CLIENT REQUEST box
		textServer.setText("Type a server request line");
		textServer.setPreferredSize(new Dimension(100, 21));
		buttonSend.setMnemonic('S');
		buttonSend.setText("Send");
		buttonSend.setEnabled(false);
		buttonSend.setPreferredSize(new Dimension(80, 20));
		buttonSend.addActionListener(new Controller());
		buttonSend.setActionCommand("s");

		// Uses GridBagLayout so JTextField expands with resize of window
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 0);
		paneClient.add(textServer, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		paneClient.add(buttonSend, c);

		// Adds CLIENT REQUEST panel container to top panel of Top-Layer Panel Container
		topPane.add(paneClient, BorderLayout.SOUTH);
		add(topPane, BorderLayout.NORTH);

		// Layouts TERMINAL panel with components
		Box box = Box.createHorizontalBox();
		paneTerminal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue, 10), "TERMINAL", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		paneTerminal.setLayout(new BorderLayout());
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea); // Adds Scroll Bar to JTextArea
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		box.add(scroll);
		paneTerminal.add(box);
		add(paneTerminal, BorderLayout.CENTER); // Adds TERMINAL panel to CENTER of Top-Layer Panel container
	}// end initComponents()

	/**
	 * Class for handling events generated by the GUI
	 * 
	 * @author Sean Macfarlane
	 * @version 2.0.0
	 * @since 1.8_40
	 */
	private class Controller implements ActionListener {
		/**
		 * Handles when an action is performed and communicates with the Server
		 * 
		 * @param e
		 *          GUI Event
		 */
		public void actionPerformed(ActionEvent e) {
			int port; // Port number
			String host; // Host name
			String request; // Request sent from Client
			String response; // Response received from Server

			// If CONNECT button is pressed
			if (e.getActionCommand().equals("c")) {
				try { // Gets Port Number from JComboBox
					port = Integer.valueOf((String) comboPorts.getSelectedItem());
				} catch (NumberFormatException ex) {
					port = 8088;
				}
				host = textHost.getText();
				if (!host.equals("localhost")) { // Checks if valid host name
					textArea.setText("CLIENT>ERROR: Unknown Host.");
					return;
				}

				try { // Tries to connect to Server and opens Output and Input Streams
					client = new Socket();
					client.connect(new InetSocketAddress(host, port), time_out);
					out = new ObjectOutputStream(client.getOutputStream());
					in = new ObjectInputStream(client.getInputStream());
				} catch (IOException ex) {
					textArea.setText("CLIENT>ERROR: Connection refused: server is not available. Check port or restart server.\n");
					return;
				}
				// If connection successful
				buttonConnect.setEnabled(false);
				buttonConnect.setBackground(Color.BLUE);
				buttonSend.setEnabled(true);
				textArea.setText("Connected to " + client + "\n");
			}

			// If SEND button is pressed
			if (e.getActionCommand().equals("s")) {
				// Gets client request from JTextField
				request = textServer.getText();
				try { // Tries to send request to server
					out.writeObject(request);
				} catch (IOException ex) {
					textArea.append("SERVER>ERROR: an unexpected error has occured\n");
				}
				try {
					response = (String) in.readObject(); // Gets response from server
					if (response.equals("CLS:")) { // If response is CLS> command
						textArea.setText("");
						return;
					} else if (response.equals("SERVER>Connection Closed.")) { // If response is END> command
						// Closes streams and socket
						out.close();
						in.close();
						client.close();
						// Display message
						textArea.append(response);
						textArea.append("\nCLIENT>Connection Closed.\n");
						// reset buttons
						buttonSend.setEnabled(false);
						buttonConnect.setEnabled(true);
						buttonConnect.setBackground(Color.RED);
						return;
					}
					// If other command, displays response
					textArea.append(response + "\n");
				} catch (ClassNotFoundException | IOException ex) {
					textArea.append("SERVER>ERROR: an unexpected error has occured\n");
				}
			}
		}// end actionPerformed()
	}// end Controller
}// end ClientView
