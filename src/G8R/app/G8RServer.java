package G8R.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class G8RServer {
	 public static final int port = 80;
	public G8RServer() {
		// TODO Auto-generated constructor stub
	}
	 public void init() {    
	        try {    
	          
	            ServerSocket serverSocket = new ServerSocket(port);    
	            while (true) {    
	               
	                Socket client = serverSocket.accept();    
	               
	                new HandlerThread(client);    
	            }    
	        } catch (Exception e) {    
	            System.out.println(" " + e.getMessage());    
	        }    
	    }    
	 public static void main(String[] args) {    
		 System.out.println("Server...\n");    
	        G8RServer server = new G8RServer();    
	        server.init();    
	 }    
	 private class HandlerThread implements Runnable {    
	        private Socket socket;    
	        public HandlerThread(Socket client) {    
	            socket = client;    
	            new Thread(this).start();    
	        }    

	        public void run() {    
	            try {    
	              
	                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
	                String clientInputStr = input.readLine();
	                System.out.println("cleint msg: " + clientInputStr);    

	              
	                PrintStream out = new PrintStream(socket.getOutputStream());    
	                System.out.print("input");    
	              
	                String s = new BufferedReader(new InputStreamReader(System.in)).readLine();    
	                out.println(s);    

	                out.close();    
	                input.close();    
	            } catch (Exception e) {    
	                System.out.println(": " + e.getMessage());    
	            } finally {    
	                if (socket != null) {    
	                    try {    
	                        socket.close();    
	                    } catch (Exception e) {    
	                        socket = null;    
	                        System.out.println(":" + e.getMessage());    
	                    }    
	                }    
	            }   
	        }    
	    }    
}
