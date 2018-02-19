package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

/**
 *
 */
public class G8RNullStep extends PollState {

	/**
	 * @param clientSocket
	 * @param logger
	 */
	public G8RNullStep(Socket clientSocket, Logger logger) {
		super(clientSocket, logger);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateMsg() {
		CookieList beforeCookie = g8rRequest.getCookieList();

		try {
			if (functionNameForFood.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length == 1) {
				int repeateValue = 0;
				String foodName = g8rRequest.getParams()[0];
				if (beforeCookie.findName(repeatStr)) {
					repeateValue = Integer.parseInt(beforeCookie.getValue(repeatStr));
				}
				String msString = "";
				repeateValue+=1;
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

				beforeCookie.add(repeatStr, String.valueOf(repeateValue));
				g8rResponse = new G8RResponse(statusOk, functionNameForNull, msString, beforeCookie);
				g8rResponse.encode(socketOut);
				close();
				context.setEndFlag();

			} else {
				String mString = "Poorly formed food mood. " + beforeCookie.getValue(strFirstName) + "'s Food mood>";

				g8rResponse = new G8RResponse(statusError, functionNameForFood, mString, beforeCookie);
				g8rResponse.encode(socketOut);
			}

		} catch (ValidationException e) {

		} catch (IOException e) {
			close();
			context.setEndFlag();

		} catch (Exception e) {
			close();
			context.setEndFlag();
		}

	}

}
