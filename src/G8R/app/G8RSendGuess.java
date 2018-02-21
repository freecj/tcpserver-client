package G8R.app;

import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

public class G8RSendGuess extends PollState {

	public G8RSendGuess(Socket clientSocket, Logger logger) {
		super(clientSocket, logger);

	}

	@Override
	public void generateMsg() {
		// get the cookielist from the request message
		CookieList beforeCookie = g8rRequest.getCookieList();

		try {
			if (functionNameForSendGuess.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length == 1) {
				// SendGuess command and length of param are right
				
				if (beforeCookie.findName("Num")) {
					
				}
				int repeateValue = 0;
				String foodName = g8rRequest.getParams()[0];
				String msString = "";
			
				
				beforeCookie.add(repeatStr, String.valueOf(repeateValue));
				g8rResponse = new G8RResponse(statusOk, functionNameForNull, msString, beforeCookie);
				writerMsg();
				close();

			} else if (functionNameForFood.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length != 1) {
				String mString = "Poorly formed food mood. " + beforeCookie.getValue(strFirstName) + "'s Food mood>";
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
