package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

/**
 * G8RFoodStep test whether the request is Foodstep and send the response message.
 */
public class G8RFoodStep extends PollState {

	/**
	 * @param clientSocket
	 * @param logger
	 */
	public G8RFoodStep(Socket clientSocket, Logger logger) {
		super(clientSocket, logger);
	}

	@Override
	public void generateMsg() {

		CookieList beforeCookie = g8rRequest.getCookieList();

		try {
			if (functionNameForFood.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length == 1) {
				// Foodstep command and length of param are right
				int repeateValue = 0;
				String foodName = g8rRequest.getParams()[0];
				if (beforeCookie.findName(repeatStr)) {
					// if there is repeat cookie in the requst message, get it.
					repeateValue = Integer.parseInt(beforeCookie.getValue(repeatStr));
				}
				String msString = "";
				//repeateValue need to plus 1 
				repeateValue += 1;
				switch (foodName) {
				case "Mexican":
					msString = String.format("20%%+%d%% off at Tacopia", repeateValue);
					break;
				case "Italian":
					msString = String.format("25%%+%d%% off at Pastastic", repeateValue);
					break;
				default:
					msString = String.format("10%%+%d%% off at McDonalds", repeateValue);
				}
				//update cookielist repeat value
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

		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception in G8RFoodStep", e);
			close();

		}

	}

}
