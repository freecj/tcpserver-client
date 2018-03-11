/************************************************
*
* Author: <Jian Cao>
* Assignment: <Programe 3 >
* Class: <CSI 4321>
*
************************************************/
package G8R.app;

import java.net.Socket;
import java.util.logging.Logger;

/**
 * which is a thread that service each clients
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
	 */
	public void handleEchoClient() {
		Context context = new Context();
		context.setState(new G8RPollStep(clntSock, logger));
		while (true) {
			if (context.isEndFlag()) {
				// if the context is in the final state, break.
				break;
			}
			// send message and turn to the right state
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
