package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RMessage;
import G8R.serialization.G8RRequest;
import G8R.serialization.G8RResponse;
import G8R.serialization.MessageInput;
import G8R.serialization.MessageOutput;
import G8R.serialization.ValidationException;

/**
 * @author free_cj
 *
 */
public abstract class PollState {
	protected Context context;
	protected G8RRequest g8rRequest;
	protected G8RResponse g8rResponse;
	protected MessageOutput socketOut = null;
	protected MessageInput socketIn = null;
	protected Socket clntSock;
	protected Logger logger;

	protected String strNameStep = "Poll";
	protected String strFirstName = "FName";
	protected String strSecondName = "LName";
	protected String functionNameForName = "NameStep";
	protected String functionNameForNull = "NULL";
	protected String functionNameForFood = "FoodStep";
	protected String statusOk = "OK";
	protected String statusError = "ERROR";
	protected String repeatStr = "Repeat";

	private final String TIMELIMIT = "1000"; // Default limit (ms)
	private final String TIMELIMITPROP = "Timelimit"; // Property
	private int timeLimit;

	/**
	 * @param _context
	 */
	public void setContext(Context _context) {
		this.context = _context;
	}

	/**
	 * @param clientSocket
	 * @param logger
	 */
	public PollState(Socket clientSocket, Logger logger) {
		this.clntSock = clientSocket;
		this.logger = logger;
		timeLimit = Integer.parseInt(System.getProperty(TIMELIMITPROP,TIMELIMIT));
		
		// Get the time limit from the System properties or take the default
		timeLimit = Integer.parseInt(System.getProperty(TIMELIMITPROP, TIMELIMIT));
		try {
			
			socketOut = new MessageOutput(clntSock.getOutputStream());
			socketIn = new MessageInput(clntSock.getInputStream());
			clntSock.setSoTimeout(timeLimit);

		} catch (NullPointerException | IOException e) {
			close();
			context.setEndFlag();
		}

	}

	/**
	 * @return true if right
	 * @throws NullPointerException
	 * @throws IOException
	 * @throws ValidationException
	 */
	public boolean read() {

		G8RMessage temp;
		try {
			
			temp = G8RMessage.decode(socketIn);
			if (temp instanceof G8RRequest) {
				g8rRequest = (G8RRequest) temp;
				return true;
			} else {
				throw new ValidationException("Message is other", "Not Request Msg");
			}
		} catch (ValidationException e1) {
			CookieList beforeCookie = new CookieList();
			try {
				g8rResponse = new G8RResponse(statusError, functionNameForNull, e1.getToken(), beforeCookie);
				g8rResponse.encode(socketOut);
				close();
				context.setEndFlag();
				return false;
			} catch (ValidationException e) {
				close();
				context.setEndFlag();
				return false;
			} catch (IOException e) {
				close();
				context.setEndFlag();
				return false;
			}

		} catch (IOException e1) {
			close();
			context.setEndFlag();
			return false;
		} catch (Exception e) {
			close();
			context.setEndFlag();
			return false;
		}
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

	/**
	 * @param msg
	 */
	public void generateErrorMsg(String msg) {
		CookieList beforeCookie = g8rRequest.getCookieList();
		try {
			g8rResponse = new G8RResponse(statusError, functionNameForNull, msg, beforeCookie);
			g8rResponse.encode(socketOut);
			close();
			context.setEndFlag();
		} catch (ValidationException e) {

		} catch (IOException e) {

		} catch (Exception e) {

		}

	}

	/**
	 * @param msg
	 */
	public void writerMsg() {

		try {
			g8rResponse.encode(socketOut);

		} catch (IOException e) {
			close();
			context.setEndFlag();

		} catch (Exception e) {
			close();
			context.setEndFlag();

		}

	}

	/**
	 * 
	 */
	public abstract void generateMsg();

}
