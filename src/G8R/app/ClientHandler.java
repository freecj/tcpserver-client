package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 */
public class ClientHandler implements Runnable {

	private int timelimit;
	private Socket clntSock;
	private Logger logger;

	private static final String TIMELIMIT = "20000"; // Default limit (ms)
	private static final String TIMELIMITPROP = "Timelimit"; // Property

	/**
	 * @param clntSock
	 * @param logger
	 */
	public ClientHandler(Socket clntSock, Logger logger) {
		this.clntSock = clntSock;
		this.logger = logger;
		// Get the time limit from the System properties or take the default
		timelimit = Integer.parseInt(System.getProperty(TIMELIMITPROP, TIMELIMIT));
	}

	/**
	 * Creates a new ClientHandler thread for the socket provided.
	 * 
	 * @param clientSocket
	 *            the socket to the client.
	 */
	public void handleEchoClient() {
		Context context = new Context();
		context.setState(new G8RPollStep(clntSock, logger));
		while (true) {
			
			if (context.isEndFlag()) {
				break;
			}
			
			context.pull();
		}

	}

	/**
	 * The run method is invoked by the ExecutorService (thread pool).
	 */
	@Override
	public void run() {
		handleEchoClient();
	}

	/**
	 * close socket of client
	 */
	public void close() {
		try {
			if (clntSock != null && !clntSock.isClosed())
				clntSock.close();
		} catch (IOException e) {
			System.err.println("client socket closed failed:");
			System.exit(1);
		}
	}
}
