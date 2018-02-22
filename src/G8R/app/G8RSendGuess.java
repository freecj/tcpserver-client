package G8R.app;

import java.net.Socket;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

/**
 *
 */
public class G8RSendGuess extends PollState {
	private String strNum = "Num";
	private String strScore = "Score";
	/**
	 * @param clientSocket
	 * @param logger
	 */
	public G8RSendGuess(Socket clientSocket, Logger logger) {
		super(clientSocket, logger);

	}
	/**
	 * test the string is numeric
	 * @param str
	 * @return true if is numeric, otherwise false
	 */
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@Override
	public void generateMsg() {
		// get the cookielist from the request message
		CookieList beforeCookie = g8rRequest.getCookieList();

		try {
			if (functionNameForSendGuess.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length == 1) {
				// SendGuess command and length of param are right
				String guessNum = g8rRequest.getParams()[0];
				if (!isNumeric(guessNum)) {
					// guessNum is not numeric
					generateErrorMsg("malfunction，param is not numeric");
					logger.info("Client " + clntSock.getRemoteSocketAddress()+  "");
				}
				
				if (beforeCookie.findName(strNum) && beforeCookie.findName(strScore)) {
					int setValue = Integer.parseInt(beforeCookie.getValue(strNum));
					if (setValue >=0 && setValue < 10) {
						int numRequest =  Integer.parseInt(guessNum);
						int score = Integer.parseInt(beforeCookie.getValue(strScore)) ;
						if (numRequest == setValue) {
							// correct case
							score+=1;
							beforeCookie.add(strScore, String.valueOf(score));
							g8rResponse = new G8RResponse(statusOk, functionNameForNull, "Correct! Score=" + String.valueOf(score), beforeCookie);
							writerMsg();
							close();
						} else if (numRequest < setValue) {
							//small case
							beforeCookie.add(strScore, String.valueOf(score));
							g8rResponse = new G8RResponse(statusError, functionNameForSendGuess, "Guess too low. Guess (0-9)?>" , beforeCookie);
							writerMsg();
						} else if (numRequest > setValue) {
							//large case
							beforeCookie.add(strScore, String.valueOf(score));
							g8rResponse = new G8RResponse(statusError, functionNameForSendGuess, "Guess too high. Guess (0-9)?>" , beforeCookie);
							writerMsg();
						}
					} else {
						// cookie value of Num is not right
						generateErrorMsg("malfunction, Num is beyond the range 0-9");
						logger.info("Client " + clntSock.getRemoteSocketAddress()+  "");
					}
				} else {
					// no cookiename=Num
					generateErrorMsg("malfunction");
					logger.info("Client " + clntSock.getRemoteSocketAddress()+  "");
				}
				
			} else if (functionNameForFood.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length != 1) {
				String mString = "Poorly formed Guess. Guess (0-9)?>";
				g8rResponse = new G8RResponse(statusError, functionNameForFood, mString, beforeCookie);

				writerMsg();
			} else {
				// error function name
				generateErrorMsg("Unexpected message");
				logger.info("Client " + clntSock.getRemoteSocketAddress()+  "");
			}

		} catch (ValidationException e) {
			close();
		} catch (Exception e) {
			close();
		}
	}

}
