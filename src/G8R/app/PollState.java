package G8R.app;

import java.io.IOException;
import java.io.InputStream;
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
		try {
			socketOut = new MessageOutput(clntSock.getOutputStream());
			socketIn = new MessageInput(clntSock.getInputStream());
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

		} catch (ValidationException e) {

		} catch (IOException e) {

		} catch (Exception e) {

		}

	}

	/**
	 * 
	 */
	public abstract void generateMsg();

}
