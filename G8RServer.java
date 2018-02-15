package G8R.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.Level;  
import java.util.logging.Logger; 
public class G8RServer {
	private Logger logger = Logger.getLogger(G8RServer.class.getName());  
	public G8RServer(int port, int threadNum) {
		 try {    
	            ServerSocket serverSocket = new ServerSocket(port);    
	            while (true) {    
	               
	                Socket client = serverSocket.accept();    
	               
	                new HandlerThread(client);    
	            }    
	        } catch (Exception e) {    
	            System.err.println(" " + e.getMessage());    
	        }    
	}
	
	 public static void main(String[] args) {   
		 if ((args.length <= 1) || (args.length > 2)) {
				// Test for correct # of args
			    System.err.println("Echo server requires 2 argument: <Server> <thread number>");
				throw new IllegalArgumentException("Parameter(s): <Server> <thread number>");
			}

			int servPort = Integer.parseInt(args[0]);
			int threadNum = Integer.parseInt(args[1]);
            while (true) {
            	  G8RServer server = new G8RServer(servPort, threadNum);    
            }
	      
	       
	 }    
	 
}
