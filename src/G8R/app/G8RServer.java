package G8R.app;



import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Server that send and get G8RMessage
 */
public class G8RServer {
	private ExecutorService ThreadPool;
	private Logger logger = Logger.getLogger(G8RServer.class.getName());
	private ServerSocket serverSocket;

	/**
	 * constructor for server, use Executors newFixedThreadPool as thread pool
	 * @param port
	 * @param threadNum thread pool number
	 */
	public G8RServer(int port, int threadNum) {
		try {

			ThreadPool = Executors.newFixedThreadPool(threadNum);
			// Create a server socket to accept client connection requests
			serverSocket = new ServerSocket();
			//port reuse
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(port));
			// Run forever, accepting and spawning a thread for each connection
			while (true) {
				ThreadPool.execute(new ClientHandler(serverSocket.accept(), logger));
			}
		} catch (Exception e) {
			ThreadPool.shutdown();
			System.err.println(" " + e.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if ((args.length != 2)) {
			// Test for correct # of args
			System.err.println("Echo server requires 2 argument: <Port> <thread number>");
			throw new IllegalArgumentException("Parameter(s): <Port> <thread number>");
		}

		int servPort = Integer.parseInt(args[0]);// Server port
		int threadNum = Integer.parseInt(args[1]);

		new G8RServer(servPort, threadNum);

	}

}
