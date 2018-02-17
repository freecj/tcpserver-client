package G8R.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import G8R.serialization.G8RMessage;
import G8R.serialization.G8RRequest;
import G8R.serialization.G8RResponse;
import G8R.serialization.MessageInput;
import G8R.serialization.MessageOutput;
import G8R.serialization.ValidationException;

public abstract class PollState {
	   protected Context context; 
	   protected G8RRequest g8rRequest;
	   protected G8RResponse g8rResponse;
	   protected MessageOutput socketOut = null;
	   protected MessageInput socketIn = null;
	   protected Socket clntSock;
	   protected Logger logger;
	   public void setContext(Context _context){  
	        this.context = _context;  
	   }  
	   public PollState(Socket clientSocket, Logger logger) {
		   this.clntSock = clntSock;
		   this.logger = logger;
	   }
	   public void read () throws NullPointerException, IOException, ValidationException{
		  
		   socketOut = new MessageOutput(clntSock.getOutputStream());
			socketIn = new MessageInput(clntSock.getInputStream());
			G8RMessage temp = G8RMessage.decode(socketIn);
			if (temp instanceof G8RRequest) {
				g8rRequest = (G8RRequest)temp;
				
			} else {
				throw new ValidationException("Message is other", "");
			}
		  
	   }
	   public void write() {
		   
	   }
	   public abstract void generateOkMsg();  
	   public abstract void generateErrorMsg(String Msg);  
}
