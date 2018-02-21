package G8R.app;


import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 */
public class ClientHandler implements Runnable {
	private Socket clntSock;
	private Logger logger;
	/**
	 * @param clntSock
	 * @param logger
	 */
	public ClientHandler(Socket clntSock, Logger logger) {
		this.clntSock = clntSock;
		this.logger = logger;
		
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

	
}
