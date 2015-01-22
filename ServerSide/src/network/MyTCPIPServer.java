package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.SolutionManager;
import tasks.TaskRunnable;
import config.LoadProperties;
import config.ServerProperties;

public class MyTCPIPServer {
	
	private ServerSocket server;
	private ArrayList<ClientHandler> clients;		//List of Clients that has connected to the server
	private ExecutorService clientsToHandle;		//ThreadPool of Clients to handle
	private Thread thread;
	int maxNumOfClientsToHandle;
	int port;
	private volatile boolean stop;
	
	public MyTCPIPServer() {						//default C'tor => gets the configuration from the XML file
		ServerProperties properties = LoadProperties.readProperties();
		this.port = properties.getPort();
		this.maxNumOfClientsToHandle = properties.getNumOfClients();
		this.stop = false;
		clients = new ArrayList<ClientHandler>();
	}	
	
	public MyTCPIPServer(int port, int numOfClients) {	//C'tor with Args from the User
		this.port = port;
		this.maxNumOfClientsToHandle = numOfClients;
		this.stop = false;
		clients = new ArrayList<ClientHandler>();
	}
	
	public void startServer() {
			try {
				server = new ServerSocket(port);
				System.out.println("Starting Server: IP= "+server.getInetAddress()+" Port= "+this.port+" maxNumOfClients= "+this.maxNumOfClientsToHandle);
				server.setSoTimeout(180000);				//T.O of 3 minute
				clientsToHandle = Executors.newFixedThreadPool(maxNumOfClientsToHandle);
				thread = new Thread(new Runnable() {
					int actualConnections = 0;
					@Override
					public void run() {
						while(!stop) {
							Socket someClient = null;
							try {
								someClient = server.accept();		//waiting for a Client to connect
								if (someClient != null) {
									actualConnections++;
									ClientHandler ch = new MyClientHandler(someClient);	//opening a new CH with that specific client
									clients.add(ch);									//adding it to the List
									System.out.println("\nclient #"+actualConnections+" made a connection\n");	
									clientsToHandle.execute(new TaskRunnable(ch));	
								}
							} catch (IOException e) {}
						}
					}
				});
				
				thread.start();
				
			} catch (IOException e) {
				System.out.println("\nException Occurred: opening the Server-Socket has failed / T.O passed ");
			}
	}
	public void stopServer() {
		stop = true;												//stops the main thread loop
		try {
			for(int i=0; i<clients.size(); i++)
				clients.get(i).stopConversation();					//stopping the search() for each Client's problem
			clientsToHandle.shutdown();								//no new Client's problems are allowed to be accepted
			SolutionManager.getInstance().saveSolutionsInFile();	//saving all the solutions that were already calculated
			server.close();											//close the Server Socket
				} catch (IOException e){}
	}
}
