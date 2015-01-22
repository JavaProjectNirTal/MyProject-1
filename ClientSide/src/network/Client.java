package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import config.ClientProperties;
import config.LoadProperties;
import model.Problem;
import model.Solution;

public class Client {

	private String serverAddress;
	private int port;
	private Socket socket;
	private boolean ClientIsConnected;
	//private String domainDescription;
	
	public Client() {	//default C'tor => reads properties from the XML file
		ClientProperties properties = LoadProperties.readProperties();
		this.port = properties.getServerPort();
		this.serverAddress = properties.getIp();
		socket = null;
		ClientIsConnected = connection();
	}
	
	public boolean connection() {
		try {
			socket = new Socket(serverAddress, port);				//connecting to server
			
		} catch (IOException e) {}
		if (socket != null)
			return true;
		return false;
	}
	
	public boolean getClientIsConnected() {
		return ClientIsConnected;
	}
	/**
	 * Sets a new Client socket, establishing connection with a server.
	 * Sets the Client's Input and Output Streams and sends the Problem to the Server and requesting a Solution
	 * @param problem of this model
	 * @return Solution for this Problem
	 * @exception ClassNotFoundException Class of a serialized object cannot be found
	 * @exception IOException Any of the usual Input/Output related exceptions
	 */
	public Solution getSolution(Problem problem) {

		if (ClientIsConnected) {

			ObjectInputStream in = null;
			ObjectOutputStream out = null;
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				out.writeObject(problem); // Request to solve this specific Problem
				Solution solution = (Solution) in.readObject(); // getting the server's answer (Solution)
				return solution;
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Server is disconnected");
				e.printStackTrace();
			} finally {
				try {
					if (out != null && in != null) {
						out.close();
						in.close();
					}
					if (socket != null && !socket.isClosed())
						socket.close();
				} catch (IOException e) {
				}
			}
		}
		return null; // Failed
	}
	/**
	 * Stops the Client's connection by closing the socket
	 */
	public void stopClient()
	{
		try {
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
		}
	}
}
