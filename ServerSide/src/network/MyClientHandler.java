package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Model;
import model.MyModel;
import model.Problem;
import model.Solution;

public class MyClientHandler implements ClientHandler {

	private Socket clientSocket; //a socket of a Client
	private Model model;
	
	public MyClientHandler(Socket socket) {
		this.clientSocket = socket;
		this.model = new MyModel();
	}
	
	@Override
	public void startConversation() {
		
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			Problem problem = (Problem) in.readObject();					//reads the Problem from the Client
			model.selectDomain(problem.getDomain());					//sets the Problem that was received from the Client as the Domain
			model.selectAlgorithm(problem.getAlgorithmName());				//sets the Problem that was received from the Client as the Algorithm
			model.solveDomain();											//searching for a solution
			Solution solution = model.getSolution();						//Retrieving the Solution
			out.writeObject(solution);										//sending it back to the Client
			//String domainDescription = ((MyModel)model).getDomainDescription();		//reads the domainDescription from the model
			//out.writeObject(domainDescription);								//sending it back to the Client
		} catch (ClassNotFoundException | IOException e) {
		}
		finally {
			try {
				if (!clientSocket.isClosed()) {
					if (out != null)
						out.close();
					if(in != null)
						in.close();
					clientSocket.close();
				}
			} catch (IOException e) {}
		}
	}
	
	@Override
	public void stopConversation() {
		model.stopDomain();				//stops the search
		if (!clientSocket.isClosed())
			try {
				clientSocket.close();
			} catch (IOException e) {}
	}

	@Override
	public void doTask() {
		startConversation();
		
	}
}
