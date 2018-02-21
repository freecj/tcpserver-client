package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RMessage;
import G8R.serialization.G8RRequest;
import G8R.serialization.G8RResponse;
import G8R.serialization.MessageInput;
import G8R.serialization.MessageOutput;
import G8R.serialization.ValidationException;

/**
 * Poll command abstact class. have the read, write, generateMsg shared function
 *
 */
public abstract class PollState {
	// server do the action in the context
	protected Context context;
	protected G8RRequest g8rRequest;
	protected G8RResponse g8rResponse;
	protected MessageOutput socketOut = null;
	protected MessageInput socketIn = null;
	protected Socket clntSock;
	protected Logger logger;

	protected String strNamePoll = "Poll";
	protected String functionNameForName = "NameStep";
	protected String functionNameForNull = "NULL";
	protected String functionNameForFood = "FoodStep";
	protected String statusOk = "OK";
	protected String statusError = "ERROR";
	protected String strFirstName = "FName";
	protected String strSecondName = "LName";
	protected String repeatStr = "Repeat";

	private final String TIMELIMIT = "20000"; // Default limit (ms)
	private final String TIMELIMITPROP = "Timelimit"; // Property
	private int timeLimit;

	protected String functionNameForSendGuess = "SendGuess";
	protected String strNameGuess = "Guess";
	/**
	 * set the new context withe the new state to the new context.
	 * 
	 * @param _context
	 *            which is modified since the state change.
	 */
	public void setContext(Context _context) {
		this.context = _context;
	}

	/**
	 * Constructor which get the clientSocket and logger, create the MessageOutput
	 * and MessageInput for decoding and encoding.
	 * 
	 * @param clientSocket
	 *            client socket
	 * @param logger
	 */
	public PollState(Socket clientSocket, Logger logger) {
		this.clntSock = clientSocket;
		this.logger = logger;

		try {

			socketOut = new MessageOutput(clntSock.getOutputStream());
			socketIn = new MessageInput(clntSock.getInputStream());
			// Get the time limit from the System properties or take the default
			timeLimit = Integer.parseInt(System.getProperty(TIMELIMITPROP, TIMELIMIT));

		} catch (NullPointerException | IOException e) {
			// socket close
			close();

		}

	}

	/**
	 * decode message from the client, assign it for g8rRequest
	 * 
	 * @throws NullPointerException
	 * @throws IOException
	 * @throws ValidationException
	 * @return true if the type of message is G8RRequest. otherwise false.
	 */
	public boolean read() {
		
		G8RMessage temp;
		try {
			clntSock.setSoTimeout(timeLimit);
			temp = G8RMessage.decode(socketIn);
			if (temp instanceof G8RRequest) {
				// the type of message from the socket is G8RRequest
				g8rRequest = (G8RRequest) temp;
				return true;
			} else {
				// otherwise throw exception
				throw new ValidationException("Message is other", "Not Request Msg");
			}
		} catch (ValidationException e1) {
			CookieList beforeCookie = new CookieList();
			System.out.println(e1.getToken());
			try {
				// if there is ValidationException, server need send NULL comand to end the
				// connection.
				g8rResponse = new G8RResponse(statusError, functionNameForNull, "Bad version", beforeCookie);
				g8rResponse.encode(socketOut);
				close();
				return false;
			} catch (ValidationException e) {
				close();
				return false;
			} catch (IOException e) {
				close();
				return false;
			}

		} catch (IOException e1) {
			close();
			return false;
		} catch (Exception e) {
			close();
			return false;
		}
	}

	/**
	 * close socket of client
	 */
	public void close() {
		context.setEndFlag();
		try {
			logTerminateMsg();
			if (clntSock != null && !clntSock.isClosed())
				clntSock.close();

		} catch (IOException e) {
			System.err.println("client socket closed failed:");
			System.exit(1);
		}
	}

	/**
	 * When the server get the wrong things from the client, it need send the NULL
	 * function to the client to close the connection with the client.
	 * 
	 * @param msg
	 *            wrong information in the g8rResponse.
	 */
	public void generateErrorMsg(String msg) {
		CookieList beforeCookie = g8rRequest.getCookieList();
		try {
			g8rResponse = new G8RResponse(statusError, functionNameForNull, msg, beforeCookie);
			writerMsg();
			close();

		} catch (ValidationException e) {
			close();
		} catch (Exception e) {
			close();
		}
	}

	/**
	 * send response message
	 */
	public void writerMsg() {
		try {
			clntSock.setSoTimeout(timeLimit);
			g8rResponse.encode(socketOut);
			logMsg();
		} catch (IOException e) {
			close();

		} catch (Exception e) {
			close();
		}

	}

	public void logMsg() {
		logger.info("<" + clntSock.getRemoteSocketAddress() + ">:" + "<" + clntSock.getPort() + ">-" + "<"
				+ Thread.currentThread().getId() + "> [Received:<" + g8rRequest.toString() + ">|Sent: <"
				+ g8rResponse.toString() + ">]" + System.getProperty("line.separator"));

	}

	public void logTerminateMsg() {
		logger.info("<" + clntSock.getRemoteSocketAddress() + ">:" + "<" + clntSock.getPort() + ">-" + "<"
				+ Thread.currentThread().getId() + "> ***client terminated" + System.getProperty("line.separator"));

	}

	/**
	 * state change and send response message
	 */
	public abstract void generateMsg();

}
