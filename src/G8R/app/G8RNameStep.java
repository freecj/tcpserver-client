package G8R.app;


import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

/**
 *
 */
public class G8RNameStep extends PollState {

	/**
	 * @param clntSock
	 * @param logger
	 */
	public G8RNameStep(Socket clntSock, Logger logger) {
		super(clntSock, logger);
	}

	@Override
	public void generateMsg() {
		// get the cookielist from the request message
		CookieList beforeCookie = g8rRequest.getCookieList();

		try {
			if (functionNameForName.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length == 2) {
				// NameStep command fits
				String[] values = g8rRequest.getParams();
				beforeCookie.add(strFirstName, values[0]);
				beforeCookie.add(strSecondName, values[1]);
				String msString = values[0] + "'s Food mood>";

				g8rResponse = new G8RResponse(statusOk, functionNameForFood, msString, beforeCookie);

				context.setState(new G8RFoodStep(clntSock, logger));
				writerMsg();
			} else if (functionNameForName.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length != 2) {
				// the param number does not match
				String mString = "Poorly formed name. Name (First Last)>";

				g8rResponse = new G8RResponse(statusError, functionNameForName, mString, beforeCookie);

				writerMsg();
			} else {
				// error function name
				generateErrorMsg("Unexpected message");

			}

		} catch (ValidationException e) {
			close();
		} catch (Exception e) {
			close();

		}
	}

}
