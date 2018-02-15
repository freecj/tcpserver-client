package G8R.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class G8RServer  {
	private ExecutorService ThreadPool;
	private Logger logger = Logger.getLogger(G8RServer.class.getName());
	private ServerSocket serverSocket;

	public G8RServer(int port, int threadNum) {
		try {
			ThreadPool = Executors.newFixedThreadPool(threadNum);
			serverSocket = new ServerSocket(port);
			while (true) {
				ThreadPool.execute(new ClientHandler(serverSocket.accept(), logger));
			}
		} catch (Exception e) {
			ThreadPool.shutdown();
			System.err.println(" " + e.getMessage());
		}
	}

/*	@Override
	public void run() { // run the service
		try {
			while (true) {
				ThreadPool.execute(new ClientHandler(serverSocket.accept(), logger));
			}
		} catch (IOException ex) {
			ThreadPool.shutdown();
		}
	}*/

	public static void main(String[] args) {
		if ((args.length <= 1) || (args.length > 2)) {
			// Test for correct # of args
			System.err.println("Echo server requires 2 argument: <Server> <thread number>");
			throw new IllegalArgumentException("Parameter(s): <Server> <thread number>");
		}

		int servPort = Integer.parseInt(args[0]);
		int threadNum = Integer.parseInt(args[1]);
	     
		new G8RServer(servPort, threadNum);
		

	}

}
