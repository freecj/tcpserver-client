package G8R.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import G8R.serialization.G8RMessage;
import G8R.serialization.G8RRequest;
import G8R.serialization.G8RResponse;
import G8R.serialization.MessageInput;
import G8R.serialization.MessageOutput;
import G8R.serialization.ValidationException;

public class ClientHandler implements Runnable {

 private static int timelimit;
	 private Socket clntSock;
	private Logger logger;


	private static final String TIMELIMIT = "20000"; // Default limit (ms)
	private static final String TIMELIMITPROP = "Timelimit"; // Property
	
	public ClientHandler(Socket clntSock, Logger logger) {
		this.clntSock = clntSock;
		this.logger = logger;
		// Get the time limit from the System properties or take the default
		timelimit = Integer.parseInt(System.getProperty(TIMELIMITPROP,TIMELIMIT));
	}
	/**
	 * Creates a new ClientHandler thread for the socket provided.
	 * 
	 * @param clientSocket
	 *            the socket to the client.
	 */
	public static void handleEchoClient(Socket clientSocket, Logger logger) {
		
	/*	long endTime = System.currentTimeMillis() + timelimit;
		try {
			socketOut = new MessageOutput(clientSocket.getOutputStream());
			socketIn = new MessageInput(clientSocket.getInputStream());
			G8RMessage temp = G8RMessage.decode(socketIn);
			if (temp instanceof G8RRequest) {
				g8rRequest = (G8RRequest)temp;
				g8rResponse = new G8RResponse(g8rRequest.coo); 
			} else {
				throw new ValidationException("Message is other", "");
			}
		} catch ( IOException e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		} catch (ValidationException e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		} catch(Exception e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		}
		*/
	}

	/**
	 * The run method is invoked by the ExecutorService (thread pool).
	 */
	@Override
	public void run() {

	
	}
	/**
	 * close socket of client
	 */
	public void close() {
		/*try {
		
			if (clientSocket != null && !clientSocket.isClosed())
				clientSocket.close();
		} catch (IOException e) {
			System.err.println("client socket closed failed:");
			System.exit(1);
		}*/
	}
}
